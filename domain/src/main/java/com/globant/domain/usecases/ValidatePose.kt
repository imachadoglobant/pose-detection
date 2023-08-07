package com.globant.domain.usecases

import com.globant.domain.entities.Pose
import com.globant.domain.entities.PoseAngles

class ValidatePose {

    companion object {
        const val TOLERANCE = 0.2
    }

    operator fun invoke(pose: Pose, poseAngles: PoseAngles): Boolean =
        pose
            .jointList
            .map { joint ->
                val jointAngle = poseAngles
                    .angles
                    .firstOrNull { j -> j.jointAngle == joint.jointAngle }
                    ?: return@map false

                return@map joint.angle in
                        (jointAngle.angle * (1.0 - TOLERANCE))..(jointAngle.angle * (1.0 + TOLERANCE))

            }
            .reduce { acc, current ->
                acc && current
            }

}