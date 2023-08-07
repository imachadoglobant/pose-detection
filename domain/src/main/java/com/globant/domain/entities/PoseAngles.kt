package com.globant.domain.entities

import java.math.RoundingMode
import java.text.DecimalFormat

data class PoseAngles(
    val angles: List<Joint>
) {
    companion object {
        val df = DecimalFormat("#.#")

        init {
            df.roundingMode = RoundingMode.HALF_UP
        }
    }

    override fun toString(): String {
        return angles
            .map { joint ->
                "${joint.jointAngle.javaClass.simpleName} = ${df.format(joint.angle)}"
            }
            .reduce { acc, s ->
                "$acc, $s"
            }
    }
}
