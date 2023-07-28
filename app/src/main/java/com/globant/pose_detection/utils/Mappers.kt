package com.globant.pose_detection.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun ContentResolver.uriToBitmap(imageUri: Uri): Bitmap =
    if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images
            .Media.getBitmap(this, imageUri)

    } else {
        val source = ImageDecoder
            .createSource(this, imageUri)
        ImageDecoder.decodeBitmap(source)
    }