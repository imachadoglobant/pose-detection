package com.globant.domain.repositories

import android.graphics.Bitmap
import com.globant.domain.entities.Pose

interface PoseAngleDetector {

    suspend fun getPoseAngles(imageBitmap: Bitmap): Pose
}