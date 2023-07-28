package com.globant.data.mapper

import com.globant.domain.entities.PoseLandmarkType
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

fun PoseLandmarkType.getPoseLandmarkId(): Int =
    when(this){
        PoseLandmarkType.NOSE -> PoseLandmark.NOSE
        PoseLandmarkType.LEFT_EYE -> PoseLandmark.LEFT_EYE
        PoseLandmarkType.LEFT_EYE_INNER -> PoseLandmark.LEFT_EYE_INNER
        PoseLandmarkType.LEFT_EYE_OUTER -> PoseLandmark.LEFT_EYE_OUTER
        PoseLandmarkType.RIGHT_EYE_INNER -> PoseLandmark.RIGHT_EYE_INNER
        PoseLandmarkType.RIGHT_EYE -> PoseLandmark.RIGHT_EYE
        PoseLandmarkType.RIGHT_EYE_OUTER -> PoseLandmark.RIGHT_EYE_OUTER
        PoseLandmarkType.LEFT_EAR -> PoseLandmark.LEFT_EAR
        PoseLandmarkType.RIGHT_EAR -> PoseLandmark.RIGHT_EAR
        PoseLandmarkType.LEFT_MOUTH -> PoseLandmark.LEFT_MOUTH
        PoseLandmarkType.RIGHT_MOUTH -> PoseLandmark.RIGHT_MOUTH
        PoseLandmarkType.LEFT_SHOULDER -> PoseLandmark.LEFT_SHOULDER
        PoseLandmarkType.RIGHT_SHOULDER -> PoseLandmark.RIGHT_SHOULDER
        PoseLandmarkType.LEFT_ELBOW -> PoseLandmark.LEFT_ELBOW
        PoseLandmarkType.RIGHT_ELBOW -> PoseLandmark.RIGHT_ELBOW
        PoseLandmarkType.LEFT_WRIST -> PoseLandmark.LEFT_WRIST
        PoseLandmarkType.RIGHT_WRIST -> PoseLandmark.RIGHT_WRIST
        PoseLandmarkType.LEFT_PINKY -> PoseLandmark.LEFT_PINKY
        PoseLandmarkType.RIGHT_PINKY -> PoseLandmark.RIGHT_PINKY
        PoseLandmarkType.LEFT_INDEX -> PoseLandmark.LEFT_INDEX
        PoseLandmarkType.RIGHT_INDEX -> PoseLandmark.RIGHT_INDEX
        PoseLandmarkType.LEFT_THUMB -> PoseLandmark.LEFT_THUMB
        PoseLandmarkType.RIGHT_THUMB -> PoseLandmark.RIGHT_THUMB
        PoseLandmarkType.LEFT_HIP -> PoseLandmark.LEFT_HIP
        PoseLandmarkType.RIGHT_HIP -> PoseLandmark.RIGHT_HIP
        PoseLandmarkType.LEFT_KNEE -> PoseLandmark.LEFT_KNEE
        PoseLandmarkType.RIGHT_KNEE -> PoseLandmark.RIGHT_KNEE
        PoseLandmarkType.LEFT_ANKLE -> PoseLandmark.LEFT_ANKLE
        PoseLandmarkType.RIGHT_ANKLE -> PoseLandmark.RIGHT_ANKLE
        PoseLandmarkType.LEFT_HEEL -> PoseLandmark.LEFT_HEEL
        PoseLandmarkType.RIGHT_HEEL -> PoseLandmark.RIGHT_HEEL
        PoseLandmarkType.LEFT_FOOT_INDEX -> PoseLandmark.LEFT_FOOT_INDEX
        PoseLandmarkType.RIGHT_FOOT_INDEX -> PoseLandmark.RIGHT_FOOT_INDEX
    }

fun PoseLandmarkType.toPoseLandmark(pose: Pose): PoseLandmark? =
    pose.getPoseLandmark(getPoseLandmarkId())