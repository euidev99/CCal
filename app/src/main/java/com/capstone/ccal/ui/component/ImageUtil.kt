package com.capstone.ccal.ui.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.capstone.ccal.common.FirebaseStorageUtils
import com.capstone.ccal.common.ImageLoader
import com.capstone.ccal.common.ImageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 책 디테일 페이지 이미지 로드 유틸
 */
@Composable
fun BookMainImage(
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
        val painter: Painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = imageUrl).apply(block = fun ImageRequest.Builder.() {
                transformations()
            }).build()
        )

        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

//@Composable
//fun BookImageRound(
//    imageUrl: String,
//    contentDescription: String?,
//    modifier: Modifier = Modifier,
//    elevation: Dp = 0.dp
//) {
//    Surface(
//        color = Color.LightGray,
//        shadowElevation = elevation,
//        shape = RoundedCornerShape(8.dp),
//        modifier = modifier
//    ) {
//        var _imageUrl by remember { mutableStateOf<String?>(null) }
//        val scope = rememberCoroutineScope()
//
//        val currentImageUrl by rememberUpdatedState(newValue = imageUrl)
//
//        LaunchedEffect(currentImageUrl) {
//            if (imageUrl.isNotEmpty()) {
//                scope.launch {
//                    try {
//                        val url = withContext(Dispatchers.IO) {
//                            FirebaseStorageUtils.getImageUrl(imageUrl)
//                        }
//                        _imageUrl = url
//                    } catch (e: Exception) {
//                        Log.d("BookImageRound", "Image Error message : $e")
//                    }
//                }
//            }
//        }
//
//        _imageUrl?.let { url ->
//            ImageLoader(imageUrl = url)
//        }
//    }
//}

@Composable
fun BookImageRound(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    Surface(
        color = Color.LightGray,
        shadowElevation = elevation,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
//        ImageLoader(imageUrl = imageUrl, modifier = Modifier)

        val painter: Painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = imageUrl).apply(block = fun ImageRequest.Builder.() {
                transformations()
            }).build()
        )

        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

