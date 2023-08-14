package com.globant.domain.usecases

import android.graphics.Bitmap
import com.globant.domain.entities.Pose
import com.globant.domain.repositories.PoseAngleDetector

class GetJointAngles(
    private val poseAngleDetector: PoseAngleDetector
) {
    suspend operator fun invoke(imageBitmap: Bitmap): Pose =
        poseAngleDetector.getPoseAngles(imageBitmap = imageBitmap)
}