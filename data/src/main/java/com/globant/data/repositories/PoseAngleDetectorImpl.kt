package com.globant.data.repositories

import android.graphics.Bitmap
import com.globant.data.mapper.toPoseLandmark
import com.globant.domain.entities.Joint
import com.globant.domain.entities.JointAngle
import com.globant.domain.entities.PoseAngles
import com.globant.domain.repositories.PoseAngleDetector
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import com.google.mlkit.vision.pose.PoseLandmark
import java.lang.Math.atan2
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PoseAngleDetectorImpl(
    val options: PoseDetectorOptionsBase
): PoseAngleDetector {

    override suspend fun getPoseAngles(imageBitmap: Bitmap): PoseAngles =
        processImage(imageBitmap = imageBitmap)
            .let { pose->
                PoseAngles(
                    angles = JointAngle
                        .values()
                        .map { jointAngle ->
                            Joint(
                                jointAngle = jointAngle,
                                angle = getAngle(pose = pose, jointAngle = jointAngle)
                            )
                        }
                )
            }

    private suspend fun processImage(
        imageBitmap: Bitmap
    ): Pose = suspendCoroutine{continuation->
        val detector = PoseDetection.getClient(options)

        detector.process(
                InputImage.fromBitmap(imageBitmap, 0)
            )
            .addOnSuccessListener { pose->
                pose?.let { continuation.resume(it) }
                    ?: continuation.resumeWithException(NullPointerException("Pose is null"))
            }
            .addOnFailureListener { e -> continuation.resumeWithException(e)}
            .addOnCompleteListener { pose->
                detector.close()
            }
            .addOnCanceledListener {
                detector.close()
            }

    }

    private fun getAngle(
        pose: Pose,
        jointAngle: JointAngle
    ): Double {
        val firstLandmark = jointAngle.firstLandmarkType.toPoseLandmark(pose)
        val midLandmark = jointAngle.midLandmarkType.toPoseLandmark(pose)
        val lastLandmark = jointAngle.lastLandmarkType.toPoseLandmark(pose)

        return getAngleBetweenPoseLandmark(
            requireNotNull(firstLandmark),
            requireNotNull(midLandmark),
            requireNotNull(lastLandmark)
        )
    }

    private fun getAngleBetweenPoseLandmark(firstPoint: PoseLandmark, midPoint: PoseLandmark, lastPoint: PoseLandmark): Double {
        var result = Math.toDegrees(
            atan2(
                (lastPoint.getPosition().y - midPoint.getPosition().y).toDouble(),
                (lastPoint.getPosition().x - midPoint.getPosition().x).toDouble()
            )
            -
            atan2(
                (firstPoint.getPosition().y - midPoint.getPosition().y).toDouble(),
                (firstPoint.getPosition().x - midPoint.getPosition().x).toDouble()
            )
        )

        result = Math.abs(result) // Angle should never be negative
        if (result > 180) {
            result = 360.0 - result // Always get the acute representation of the angle
        }
        return result
    }

}