package com.globant.domain.usecases

import android.graphics.Bitmap
import com.globant.domain.entities.PoseAngles
import com.globant.domain.entities.PoseLandmarkType
import com.globant.domain.repositories.PoseAngleDetector

class GetJointAngles(
    private val poseAngleDetector: PoseAngleDetector
) {
    operator suspend fun invoke(imageBitmap: Bitmap): PoseAngles =
        poseAngleDetector.getPoseAngles(imageBitmap = imageBitmap)
}