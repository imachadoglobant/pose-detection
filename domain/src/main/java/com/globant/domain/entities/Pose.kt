package com.globant.domain.entities

data class Pose(
    val id: String,
    val name: String,
    val jointList: List<Joint>
)
