package com.globant.domain.entities

enum class JointAngle(
    val firstLandmarkType: PoseLandmarkType,
    val midLandmarkType: PoseLandmarkType,
    val lastLandmarkType: PoseLandmarkType,
) {
    RightKnee(
        firstLandmarkType = PoseLandmarkType.RIGHT_ANKLE,
        midLandmarkType = PoseLandmarkType.RIGHT_KNEE,
        lastLandmarkType = PoseLandmarkType.RIGHT_HIP
    ),
    LeftKnee(
        firstLandmarkType = PoseLandmarkType.LEFT_ANKLE,
        midLandmarkType = PoseLandmarkType.LEFT_KNEE,
        lastLandmarkType = PoseLandmarkType.LEFT_HIP
    ),
    RightHip(
        firstLandmarkType = PoseLandmarkType.RIGHT_KNEE,
        midLandmarkType = PoseLandmarkType.RIGHT_HIP,
        lastLandmarkType = PoseLandmarkType.RIGHT_SHOULDER
    ),
    LeftHip(
        firstLandmarkType = PoseLandmarkType.LEFT_KNEE,
        midLandmarkType = PoseLandmarkType.LEFT_HIP,
        lastLandmarkType = PoseLandmarkType.LEFT_SHOULDER
    ),
    RightBack(
        firstLandmarkType = PoseLandmarkType.RIGHT_HIP,
        midLandmarkType = PoseLandmarkType.RIGHT_SHOULDER,
        lastLandmarkType = PoseLandmarkType.RIGHT_EAR
    ),
    LeftBack(
        firstLandmarkType = PoseLandmarkType.LEFT_HIP,
        midLandmarkType = PoseLandmarkType.LEFT_SHOULDER,
        lastLandmarkType = PoseLandmarkType.LEFT_EAR
    )
}

