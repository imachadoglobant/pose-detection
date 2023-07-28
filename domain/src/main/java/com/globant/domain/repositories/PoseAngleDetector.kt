package com.globant.domain.repositories

import android.graphics.Bitmap
import com.globant.domain.entities.PoseAngles

interface PoseAngleDetector {

    suspend fun getPoseAngles(imageBitmap: Bitmap): PoseAngles
}