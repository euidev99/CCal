/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.capstone.ccal.ui.detail

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.ccal.R
import com.capstone.ccal.common.formatPrice
import com.capstone.ccal.model.BookDetailItem
import com.capstone.ccal.ui.component.AuthorDetailPopup
import com.capstone.ccal.ui.component.BookImage
import com.capstone.ccal.ui.component.MyHorizontalDivider
import com.capstone.ccal.ui.util.mirroringBackIcon
import kotlin.math.max
import kotlin.math.min

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun BookDetail(
    bookId: String,
    upPress: () -> Unit
) {
    val detailViewModel: DetailViewModel = viewModel(factory = DetailViewModel.provideFactory(
        BookRepository()
    ))

    val book by detailViewModel.feedRes.observeAsState()

    var authorDetailVisible by rememberSaveable { mutableStateOf(false) }
    var selectedAuthorIndex by rememberSaveable { mutableStateOf(0) }

    //데이터 가져오기
    LaunchedEffect(Unit) {
        if (book == null) {
            detailViewModel.fetchData(bookId)
        }
    }

    if (book == null) {
        // progress
        Box(Modifier.fillMaxSize()) {
            Text(text = "loading ")
        }
    } else {
        Box(Modifier.fillMaxSize()) {
            val scroll = rememberScrollState(0)
            Header()
            Body(
                scroll,
                book,
                onSelectAuthor = { index ->
                    authorDetailVisible = true
                    if (index != selectedAuthorIndex) selectedAuthorIndex = index
                }
            )
            Title(book!!) {
                scroll.value
            }
            Image(book!!.bookImageUrl) { scroll.value }
            Up(upPress)
//        CartBottomBar(modifier = Modifier.align(Alignment.BottomCenter))
            Log.d("seki", "scroll value : ${scroll.value}")
        }
    }

    //디테일 중에서, 작가 정보 선택한 경우
    AnimatedVisibility(
        visible = authorDetailVisible,
        enter = slideInVertically() + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        AuthorDetailPopup(
            item = book?.authorList?.get(selectedAuthorIndex),
//            itemCollection = detailData.data.partyPolicyList,
            onDismiss = { authorDetailVisible = false }
        )
    }
}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(listOf(Color.Magenta, Color.Yellow, Color.White)),
                alpha = 0.4f,
//                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
//            .background(Brush.horizontalGradient(JetsnackTheme.colors.tornado1))
    )
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = mirroringBackIcon(),
            tint = MaterialTheme.colorScheme.surfaceTint,
            contentDescription = stringResource(R.string.back_description)
        )
    }
}


@Composable
private fun Body(
//    related: List<SnackCollection>,
    scroll: ScrollState,
    book: BookDetailItem?,
    onSelectAuthor: (Int) -> Unit
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
//                .clickable {
////                    onSelectAuthor()
////                    Log.d("seki", "clicked")
//                }
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))
            Surface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(TitleHeight))

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.detail_description_title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(16.dp))
                    var seeMore by remember { mutableStateOf(true) }
                    book?.let {
                        Text(
                            text = it.bookDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis,
                            modifier = HzPadding
                        )
                    }
                    val textButton = if (seeMore) {
                        stringResource(id = R.string.detail_open)
                    } else {
                        stringResource(id = R.string.detail_close)
                    }
                    Text(
                        text = textButton,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .heightIn(20.dp)
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clickable {
                                seeMore = !seeMore
                            }
                    )
                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.detail_author_name),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = HzPadding
                            .clickable {
                                onSelectAuthor(0)
                            }
                    )
                    Spacer(Modifier.height(4.dp))
                    book?.let {
                        Text(
                            text = it.authorList.get(0).authorName,//stringResource(R.string.ingredients_list),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = HzPadding
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    MyHorizontalDivider()

                    Spacer(Modifier.height(300.dp))

//                    related.forEach { snackCollection ->
//                        key(snackCollection.id) {
//                            SnackCollection(
//                                snackCollection = snackCollection,
//                                onSnackClick = { },
//                                highlight = false
//                            )
//                        }
//                    }

                    Spacer(
                        modifier = Modifier
                            .padding(bottom = BottomBarHeight)
                            .navigationBarsPadding()
                            .height(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Title(book: BookDetailItem, scrollProvider: () -> Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = book.bookName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = HzPadding
        )
        Text(
            text = book.mainCategory,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = HzPadding
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = formatPrice(book.price),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = HzPadding
        )

        Spacer(Modifier.height(8.dp))
        MyHorizontalDivider()
    }
}

@Composable
private fun Image(
    imageUrl: String,
    scrollProvider: () -> Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }

    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = HzPadding.statusBarsPadding()
    ) {
        BookImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFractionProvider: () -> Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            (constraints.maxWidth - imageWidth), // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}

//@Composable
//private fun CartBottomBar(modifier: Modifier = Modifier) {
//    val (count, updateCount) = remember { mutableStateOf(1) }
//    JetsnackSurface(modifier) {
//        Column {
//            JetsnackDivider()
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .navigationBarsPadding()
//                    .then(HzPadding)
//                    .heightIn(min = BottomBarHeight)
//            ) {
//                QuantitySelector(
//                    count = count,
//                    decreaseItemCount = { if (count > 0) updateCount(count - 1) },
//                    increaseItemCount = { updateCount(count + 1) }
//                )
//                Spacer(Modifier.width(16.dp))
//                JetsnackButton(
//                    onClick = { /* todo */ },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = stringResource(R.string.add_to_cart),
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center,
//                        maxLines = 1
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Preview("default")
//@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Preview("large font", fontScale = 2f)
//@Composable
//private fun SnackDetailPreview() {
//    JetsnackTheme {
//        SnackDetail(
//            snackId = 1L,
//            upPress = { }
//        )
//
//    }
//}
