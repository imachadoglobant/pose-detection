package com.globant.data.repositories

import com.globant.domain.entities.Joint
import com.globant.domain.entities.JointAngle
import com.globant.domain.entities.Pose
import com.globant.domain.repositories.PoseRepository
import java.util.UUID

class PresetPoseRepositoryImpl : PoseRepository {

    companion object {
        val posesList = listOf(
            Pose(
                id = UUID.randomUUID().toString(),
                name = "Sitting",
                jointList = listOf(
                    Joint(
                        jointAngle = JointAngle.RightKnee,
                        angle = 80.0
                    ),
                    Joint(
                        jointAngle = JointAngle.LeftKnee,
                        angle = 80.0
                    ),
                    Joint(
                        jointAngle = JointAngle.RightHip,
                        angle = 100.0
                    ),
                    Joint(
                        jointAngle = JointAngle.LeftHip,
                        angle = 100.0
                    ),
                    Joint(
                        jointAngle = JointAngle.RightBack,
                        angle = 160.0
                    ),
                    Joint(
                        jointAngle = JointAngle.LeftBack,
                        angle = 160.0
                    )
                )
            )
        )
    }

    override suspend fun getPoses(): List<Pose> = posesList

}