package com.globant.data.repositories

import android.graphics.Bitmap
import com.globant.data.mapper.toPoseLandmark
import com.globant.domain.entities.Joint
import com.globant.domain.entities.JointAngle
import com.globant.domain.entities.Pose
import com.globant.domain.repositories.PoseAngleDetector
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.atan2
import com.google.mlkit.vision.pose.Pose as MLKitPose

class PoseAngleDetectorImpl(
    val options: PoseDetectorOptionsBase
): PoseAngleDetector {

    override suspend fun getPoseAngles(imageBitmap: Bitmap): Pose =
        processImage(imageBitmap = imageBitmap)
            .let { pose ->
                Pose(
                    jointList = JointAngle
                        .values()
                        .map { jointAngle ->
                            Joint(
                                jointAngle = jointAngle,
                                angle = getAngle(mlkitPose = pose, jointAngle = jointAngle)
                            )
                        }
                )
            }

    private suspend fun processImage(
        imageBitmap: Bitmap
    ): MLKitPose = suspendCoroutine { continuation ->
        val detector = PoseDetection.getClient(options)

        detector.process(
            InputImage.fromBitmap(imageBitmap, 0)
        )
            .addOnSuccessListener { pose ->
                pose?.let { continuation.resume(it) }
                    ?: continuation.resumeWithException(NullPointerException("Pose is null"))
            }
            .addOnFailureListener { e -> continuation.resumeWithException(e) }
            .addOnCompleteListener { pose->
                detector.close()
            }
            .addOnCanceledListener {
                detector.close()
            }

    }

    private fun getAngle(
        mlkitPose: MLKitPose,
        jointAngle: JointAngle
    ): Double {
        val firstLandmark = jointAngle.firstLandmarkType.toPoseLandmark(mlkitPose)
        val midLandmark = jointAngle.midLandmarkType.toPoseLandmark(mlkitPose)
        val lastLandmark = jointAngle.lastLandmarkType.toPoseLandmark(mlkitPose)

        return getAngleBetweenPoseLandmark(
            firstLandmark,
            midLandmark,
            lastLandmark
        )
    }

    private fun getAngleBetweenPoseLandmark(
        firstPoint: PoseLandmark?,
        midPoint: PoseLandmark?,
        lastPoint: PoseLandmark?
    ): Double {
        if (firstPoint == null || midPoint == null || lastPoint == null) {
            return 0.0
        }

        var result = Math.toDegrees(
            atan2(
                (lastPoint.position.y - midPoint.position.y).toDouble(),
                (lastPoint.position.x - midPoint.position.x).toDouble()
            )
                    -
                    atan2(
                        (firstPoint.position.y - midPoint.position.y).toDouble(),
                        (firstPoint.position.x - midPoint.position.x).toDouble()
                    )
        )

        result = Math.abs(result) // Angle should never be negative
        if (result > 180) {
            result = 360.0 - result // Always get the acute representation of the angle
        }
        return result
    }

}