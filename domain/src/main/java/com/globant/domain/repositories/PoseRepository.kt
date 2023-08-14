package com.globant.domain.repositories

import com.globant.domain.entities.Pose

interface PoseRepository {
    suspend fun getPoses(): List<Pose>
}