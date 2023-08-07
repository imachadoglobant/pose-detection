package com.globant.domain.usecases

import com.globant.domain.entities.Pose
import com.globant.domain.repositories.PoseRepository

class GetPoses(
    private val poseRepository: PoseRepository
) {
    suspend operator fun invoke(): List<Pose> =
        poseRepository.getPoses()
}