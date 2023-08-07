package com.globant.pose_detection.viewmodels

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.globant.data.repositories.PoseAngleDetectorImpl
import com.globant.data.repositories.PresetPoseRepositoryImpl
import com.globant.domain.usecases.GetJointAngles
import com.globant.domain.usecases.GetPoses
import com.globant.domain.usecases.ValidatePose
import com.globant.pose_detection.utils.uriToBitmap
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class ImagePoseAnglesViewModel constructor(
    val getPoses: GetPoses,
    val getJointAngles: GetJointAngles,
    val validatePose: ValidatePose
) : ViewModel() {

    private val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val imageBitmap = _imageBitmap.asStateFlow()

    val poseAngles = imageBitmap
        .filterNotNull()
        .filterNot { bitmap ->
            bitmap.byteCount == 0
        }
        .map { bitmap ->
            getJointAngles(bitmap)
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5_000
            ),
            replay = 1
        )

    val validatedFirstPose = poseAngles
        .filterNotNull()
        .map { poseAngles ->
            validatePose(
                pose = getPoses().first(),
                poseAngles = poseAngles
            )
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5_000
            ),
            replay = 1
        )

    fun setImage(contentResolver: ContentResolver, imageUri: Uri?) {
        viewModelScope.launch {
            imageUri?.let { uri -> contentResolver.uriToBitmap(imageUri = uri) }
                ?.let { bitmap ->
                    _imageBitmap.emit(bitmap)
                }
        }
    }

}

class ImagePoseAnglesViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImagePoseAnglesViewModel::class.java)) {
            val poseDetectorOptions = PoseDetectorOptions
                .Builder()
                .setDetectorMode(PoseDetectorOptions.SINGLE_IMAGE_MODE)
                .build()
            val poseAngleDetector = PoseAngleDetectorImpl(poseDetectorOptions)

            @Suppress("UNCHECKED_CAST")
            return ImagePoseAnglesViewModel(
                GetPoses(PresetPoseRepositoryImpl()),
                GetJointAngles(poseAngleDetector),
                ValidatePose()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}