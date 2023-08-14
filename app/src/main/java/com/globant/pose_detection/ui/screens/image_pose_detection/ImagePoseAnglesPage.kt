package com.globant.pose_detection.ui.screens.image_pose_detection

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.globant.pose_detection.ui.components.CheckIconContainer
import com.globant.pose_detection.ui.components.CollapsingPoseDetailsContainer
import com.globant.pose_detection.ui.components.ImageContainer
import com.globant.domain.entities.Pose
import com.globant.pose_detection.ui.components.PoseDetails
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

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom

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

    FloatingActionButton(
        onClick = {
            launcher.launch("image/*")
        },
        modifier = modifier.padding(16.dp)
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Image")
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

        val localFirstPoseValidation = firstPoseValidation ?: false

        imageBitmap?.let { bitmap ->

            ImageContainer(
                imageBitmap = bitmap.asImageBitmap(),
                isValid = localFirstPoseValidation
            )
        }

        firstPoseValidation?.let { validationResult ->
            CheckIconContainer(
                isValid = validationResult,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        processedPose?.let { pose ->
            CollapsingPoseDetailsContainer(pose = pose)
        }

    }
}
