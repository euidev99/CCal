package com.capstone.ccal.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.capstone.ccal.R
import com.google.firebase.storage.FirebaseStorage
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseStorageUtils {
    suspend fun getImageUrl(path: String): String {
        return try {
            val url = path
            val storage: FirebaseStorage =
                FirebaseStorage.getInstance("gs://calcalcal-6c20c.appspot.com")
            val storageReference = storage.reference
            val pathReference = storageReference.child("$url")

            pathReference.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.d("seki", "Image Load Exception : $e")
            return ""
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ImageUtil(imageUrl: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }

    Log.d("asd","asdasd")

    coroutineScope.launch {
        try {
            val bitmap = withContext(Dispatchers.IO) {
                val futureTarget = Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                futureTarget.get()
            }
            imageBitmap.value = bitmap.asImageBitmap()
        } catch (e: Exception) {
            Log.e("GlideImage", "Error loading image", e)
        }
    }

    imageBitmap.value?.let {
        Image(
            bitmap = it,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

//@Composable
//fun ImageLoader(
//    imageUrl: String,
//    contentDescription: String?,
//    modifier: Modifier = Modifier
//) {
//    GlideImage(
//        imageModel = imageUrl,
//        contentDescription = contentDescription,
//        modifier = modifier
//    )
//}
//@Composable
//fun ImageLoader(imageUrl: String) {
//    val context = LocalContext.current
//    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
//
//    Log.d("asd","aaaaa")
//
//    val currentImageUrl by rememberUpdatedState(newValue = imageUrl)
//
//    LaunchedEffect(currentImageUrl) {
//        Log.d("asd","bbbbb")
//        try {
//            val bitmap = withContext(Dispatchers.IO) {
//                val futureTarget = Glide.with(context)
//                    .asBitmap()
//                    .load(imageUrl)
//                    .submit()
//                futureTarget.get()
//            }
//            imageBitmap.value = bitmap.asImageBitmap()
//        } catch (e: Exception) {
//            Log.e("GlideImage", "Error loading image", e)
//        }
//    }
//
//    imageBitmap.value?.let {
////        Image(
////            bitmap = it,
////            contentDescription = null,
////            modifier = Modifier.fillMaxSize(),
////            contentScale = ContentScale.Crop
////        )
//        GlideImage(
//            imageModel = imageUrl,
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.Crop
//        )
//    }
//}

//@Composable
//fun ImageLoader(
//    imageUrl: String,
//    modifier: Modifier = Modifier) {
//
//    AsyncImage(
//        model = imageUrl,
//        contentDescription = null,
//        modifier = modifier,
//        contentScale = ContentScale.Crop
//    )
//}
@Composable
fun ImageLoader(imageUrl: String, modifier: Modifier = Modifier) {
    val painter: Painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            transformations()
        }
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}


