package com.capstone.ccal.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun CircularImage(
    drawableId: Int,
    size: Dp
) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = null, // Provide appropriate content description
            modifier = Modifier
                .size(size)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Fit
        )
    }
}