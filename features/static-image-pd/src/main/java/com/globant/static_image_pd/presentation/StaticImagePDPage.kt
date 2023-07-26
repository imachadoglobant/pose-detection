package com.globant.static_image_pd.presentation

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StaticImagePDPage(
    modifier: Modifier = Modifier
){
    val imageUri = remember {
        mutableStateOf<Uri?>(null)
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImagePanel(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            imageUri = imageUri.value
        )
        ImageSelector(
            onImageSelected = {uri->
                imageUri.value = uri
            }
        )
    }
}

@Preview(showBackground = false, widthDp = 480, heightDp = 854)
@Composable
fun StaticImagePDPagePreview(){
    MaterialTheme {
        StaticImagePDPage()
    }
}

@Composable
fun ImageSelector(
    modifier: Modifier = Modifier,
    onImageSelected: (Uri)->Unit
){

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.run(onImageSelected)
    }

    Button(
        modifier = modifier,
        onClick = {
            launcher.launch("image/*")
        }
    ) {
        Text(text = "Select Image")
    }
}

@Composable
fun ImagePanel(
    modifier: Modifier = Modifier,
    imageUri: Uri? = null
){
    val context = LocalContext.current
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                bitmap = ImageDecoder.decodeBitmap(source)
            }

            bitmap?.let { btm ->
                val imageBitmap = btm.asImageBitmap()

                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

            }
        } ?: Text(text = "Select image")
    }
}
