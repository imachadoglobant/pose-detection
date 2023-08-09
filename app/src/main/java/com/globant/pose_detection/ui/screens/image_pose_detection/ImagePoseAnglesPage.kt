package com.globant.pose_detection.ui.screens.image_pose_detection

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.globant.domain.entities.Pose
import com.globant.pose_detection.utils.toFormattedString
import com.globant.pose_detection.utils.uriToBitmap
import com.globant.pose_detection.viewmodels.ImagePoseAnglesViewModel
import com.globant.pose_detection.viewmodels.ImagePoseAnglesViewModelFactory

@Composable
fun ImagePoseAnglesPage(
    modifier: Modifier = Modifier,
    staticImagePDViewModel: ImagePoseAnglesViewModel =
        viewModel(factory = ImagePoseAnglesViewModelFactory())
) {
    val context = LocalContext.current

    val imageBitmap by remember(staticImagePDViewModel.imageBitmap) {
        staticImagePDViewModel.imageBitmap
    }.collectAsState()

    val processedPose by remember(staticImagePDViewModel.processedPose) {
        staticImagePDViewModel.processedPose
    }.collectAsState(initial = null)

    val firstPoseValidation by remember(staticImagePDViewModel.validatedFirstPose) {
        staticImagePDViewModel.validatedFirstPose
    }.collectAsState(initial = null)

    val firstPoseValidation by remember(staticImagePDViewModel.validatedFirstPose) {
        staticImagePDViewModel.validatedFirstPose
    }.collectAsState(initial = null)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImagePanel(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            imageBitmap = imageBitmap,
            processedPose = processedPose,
            firstPoseValidation = firstPoseValidation
        )
        ImageSelector(
            onImageSelected = { uri ->
                context.contentResolver.uriToBitmap(uri)
                    .let { bitmap ->
                        staticImagePDViewModel.setImage(bitmap = bitmap)
                    }
            }
        )
    }
}

@Composable
fun ImageSelector(
    modifier: Modifier = Modifier,
    onImageSelected: (Uri) -> Unit
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.run(onImageSelected)
    }

    Button(
        modifier = modifier,
        onClick = {
            launcher.launch("image/*")
        }
    ) {
        Text(text = "Select Image")
    }
}

@Composable
fun ImagePanel(
    modifier: Modifier = Modifier,
    imageBitmap: Bitmap?,
    processedPose: Pose? = null,
    firstPoseValidation: Boolean? = null
) {
    Column(modifier = modifier) {
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )
        }

        processedPose?.let { pose ->
            Text(text = "Pose: ${pose.toFormattedString()}")
        }

        firstPoseValidation?.let { validationResult ->
            Text(text = "First Pose validation result: $validationResult")
        }

        firstPoseValidation?.let { validationResult ->
            Text(text = "First Pose validation result: $validationResult")
        }
    }


}