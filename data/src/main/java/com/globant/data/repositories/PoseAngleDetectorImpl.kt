package com.globant.data.repositories

import android.graphics.Bitmap
import com.globant.data.mapper.toPoseLandmark
import com.globant.domain.entities.PoseAngles
import com.globant.domain.repositories.PoseAngleDetector
import com.globant.domain.entities.PoseLandmarkType
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
                    leftKnee = getAngle(
                        pose = pose,
                        firstLandmarkType = PoseLandmarkType.LEFT_ANKLE,
                        midLandmarkType = PoseLandmarkType.LEFT_KNEE,
                        lastLandmarkType = PoseLandmarkType.LEFT_HIP
                    ),
                    rightKnee = getAngle(
                        pose = pose,
                        firstLandmarkType = PoseLandmarkType.RIGHT_ANKLE,
                        midLandmarkType = PoseLandmarkType.RIGHT_KNEE,
                        lastLandmarkType = PoseLandmarkType.RIGHT_HIP
                    ),
                    leftHip = getAngle(
                        pose = pose,
                        firstLandmarkType = PoseLandmarkType.LEFT_KNEE,
                        midLandmarkType = PoseLandmarkType.LEFT_HIP,
                        lastLandmarkType = PoseLandmarkType.LEFT_SHOULDER
                    ),
                    rightHip = getAngle(
                        pose = pose,
                        firstLandmarkType = PoseLandmarkType.RIGHT_KNEE,
                        midLandmarkType = PoseLandmarkType.RIGHT_HIP,
                        lastLandmarkType = PoseLandmarkType.RIGHT_SHOULDER
                    ),
                    leftShoulder = getAngle(
                        pose = pose,
                        firstLandmarkType = PoseLandmarkType.LEFT_HIP,
                        midLandmarkType = PoseLandmarkType.LEFT_SHOULDER,
                        lastLandmarkType = PoseLandmarkType.LEFT_ELBOW
                    ),
                    rightShoulder = getAngle(
                        pose = pose,
                        firstLandmarkType = PoseLandmarkType.RIGHT_HIP,
                        midLandmarkType = PoseLandmarkType.RIGHT_SHOULDER,
                        lastLandmarkType = PoseLandmarkType.RIGHT_ELBOW
                    ),
                    leftElbow = getAngle(
                        pose = pose,
                        firstLandmarkType = PoseLandmarkType.LEFT_SHOULDER,
                        midLandmarkType = PoseLandmarkType.LEFT_ELBOW,
                        lastLandmarkType = PoseLandmarkType.LEFT_WRIST
                    ),
                    rightElbow = getAngle(
                        pose = pose,
                        firstLandmarkType = PoseLandmarkType.RIGHT_SHOULDER,
                        midLandmarkType = PoseLandmarkType.RIGHT_ELBOW,
                        lastLandmarkType = PoseLandmarkType.RIGHT_WRIST
                    )
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
        firstLandmarkType: PoseLandmarkType,
        midLandmarkType: PoseLandmarkType,
        lastLandmarkType: PoseLandmarkType
    ): Double {
        return requireNotNull(
            pose?.let { pose ->
                val firstLandmark = firstLandmarkType.toPoseLandmark(pose)
                val midLandmark = midLandmarkType.toPoseLandmark(pose)
                val lastLandmark = lastLandmarkType.toPoseLandmark(pose)

                getAngleBetweenPoseLandmark(
                    requireNotNull(firstLandmark),
                    requireNotNull(midLandmark),
                    requireNotNull(lastLandmark)
                )
            }
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