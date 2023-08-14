package com.globant.pose_detection.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.globant.domain.entities.Pose
import java.text.DecimalFormat

// Image Components
@Composable
fun ImageContainer(isValid: Boolean, imageBitmap: ImageBitmap) {
    val borderColor = if (isValid) Color.Green else Color.Red
    val borderWidth = 2.dp

    Box(
        modifier = Modifier
            .fillMaxHeight(0.33f)
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .border(border = BorderStroke(borderWidth, borderColor))
                .padding(8.dp),
            contentScale = ContentScale.Fit
        )
    }
}

// Check Pose Component
@Composable
fun CheckIconContainer(isValid: Boolean, modifier: Modifier) {
    val icon = if (isValid) Icons.Default.CheckCircle else Icons.Default.Warning
    val iconColor = if (isValid) Color.Green else Color.Red
    val text = if (isValid) "Correct Pose" else "Wrong Pose"
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .align(CenterVertically)
                .padding(4.dp)
        )
        Text(
            text = text, modifier = Modifier
                .padding(4.dp)
                .align(CenterVertically)
        )
    }
}

// Details Components
@Composable
fun CollapsingPoseDetailsContainer(pose: Pose) {
    CollapsibleColumn {
        PoseDetails(pose = pose)
    }
}

@Composable
fun CollapsibleColumn(
    panel: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (expanded) {
                Text("Hide Details")
            } else {
                Text("Show Details")
            }
        }

        if (expanded) {
            panel()
        }
    }
}

@Composable
fun PoseDetails(pose: Pose) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HeaderText("Pose Name: ${pose.name}")
        pose.jointList.forEachIndexed { _, joint ->
            ValueText(title = joint.jointAngle.name, value = joint.angle)
        }
    }
}

@Composable
fun HeaderText(value: String) {
    Text(
        text = value,
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(bottom = 2.dp)
    )
}

@Composable
fun ValueText(title: String, value: Double) {
    val decimalFormat = DecimalFormat("#.00")
    val formattedValue = decimalFormat.format(value)
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                append("$title: ")
            }
            append(formattedValue)
        }, fontSize = 14.sp
    )
}
