package com.globant.pose_detection.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.globant.domain.entities.Pose
import java.math.RoundingMode
import java.text.DecimalFormat

fun ContentResolver.uriToBitmap(imageUri: Uri): Bitmap =
    if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images
            .Media.getBitmap(this, imageUri)

    } else {
        val source = ImageDecoder
            .createSource(this, imageUri)
        ImageDecoder.decodeBitmap(source)
    }

fun Pose.toFormattedString(): String {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.HALF_UP

    val angles = jointList
        .map { joint ->
            "${joint.jointAngle.name} = ${df.format(joint.angle)}"
        }
        .reduce { acc, s ->
            "$acc, $s"
        }

    return "id: $id, name: $name, jointList: [$angles]"
}