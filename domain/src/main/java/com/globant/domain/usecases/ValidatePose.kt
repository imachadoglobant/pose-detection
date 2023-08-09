package com.globant.domain.usecases

import com.globant.domain.entities.Pose

class ValidatePose {

    companion object {
        private const val TOLERANCE = 0.2
    }

    operator fun invoke(pose: Pose, processedPose: Pose): Boolean =
        pose
            .jointList
            .map { joint ->
                val jointAngle = processedPose
                    .jointList
                    .firstOrNull { j -> j.jointAngle == joint.jointAngle }
                    ?: return@map false

                return@map joint.angle in
                        (jointAngle.angle * (1.0 - TOLERANCE))..(jointAngle.angle * (1.0 + TOLERANCE))

            }
            .reduce { acc, current ->
                acc && current
            }

}