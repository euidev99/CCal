package com.capstone.ccal.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.capstone.ccal.R
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

/**
 * 책 디테일 페이지 이미지 로드 유틸
 */
@Composable
fun BookImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    Surface(
        color = Color.LightGray,
        shadowElevation = elevation,
        shape = CircleShape,
        modifier = modifier
    ) {
        GlideImage( // CoilImage, FrescoImage
            imageModel = imageUrl,
            modifier = modifier,
            contentDescription = contentDescription,
            // shows an indicator while loading an image.
//            loading = {},
            // shows an error text if fail to load an image.
            failure = {
//                Text(text = "image request failed.")
            },
            previewPlaceholder = R.drawable.magic_book_icon,

            shimmerParams = ShimmerParams(
                baseColor = Color.DarkGray,
                highlightColor = Color.LightGray,
                durationMillis = 350,
                dropOff = 0.65f,
                tilt = 20f
            )
        )
    }
}