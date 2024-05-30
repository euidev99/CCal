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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.ccal.R
import com.capstone.ccal.common.formatPrice
import com.capstone.ccal.model.BookDetailItem
import com.capstone.ccal.ui.component.AuthorDetailPopup
import com.capstone.ccal.ui.component.BookImageRound
import com.capstone.ccal.ui.component.BookMainImage
import com.capstone.ccal.ui.component.MyHorizontalDivider
import com.capstone.ccal.ui.component.ProgressWithText
import com.capstone.ccal.ui.theme.customFont
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
private val HzPadding = Modifier.padding(horizontal = 12.dp)

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

    var shoppingScreenVisible by rememberSaveable { mutableStateOf(false) }

    //데이터 가져오기
    LaunchedEffect(Unit) {
        if (book == null) {
            detailViewModel.fetchData(bookId)
        }
    }

    if (book == null) {
        // progress
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ProgressWithText(
                text = stringResource(id = R.string.detail_loading),
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            val scroll = rememberScrollState(0)
            Header()
            Body(
                imageList = book!!.bookImageList,
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
            DetailBottomBar(
                onClick = { shoppingScreenVisible = true },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
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

    //디테일 안에서, 하단 바 선택했을 경우
    AnimatedVisibility(
        visible = shoppingScreenVisible,
        enter = slideInVertically() + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        book?.let {
            ShoppingCartScreen(
                bookDetailItem = it,
                onDismiss = { shoppingScreenVisible = false }
            )
        }
    }
}

/**
 * 하단 바텀 바
 */
@Composable
private fun DetailBottomBar(
    onClick : () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .height(BottomBarHeight)
            .background(MaterialTheme.colorScheme.onBackground)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.detail_move_unity),
            fontFamily = customFont,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Icon(
            imageVector = Icons.Default.PlayArrow,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = ""
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
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.onBackground,
                        MaterialTheme.colorScheme.onSurface,
                        MaterialTheme.colorScheme.background
                    )
                ),
                alpha = 0.4f,
            )
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
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                shape = CircleShape
            ).zIndex(5f)
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
    imageList: List<String>,
    scroll: ScrollState,
    book: BookDetailItem?,
    onSelectAuthor: (Int) -> Unit,
    modifier : Modifier = Modifier.background(Color.Transparent)
) {
    Column(
        modifier = modifier
    ) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
//                .clickable {
////                    onSelectAuthor()
////                    Log.d("seki", "clicked")
//                }
        )
        Column(
            modifier = modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))
            Surface(modifier.fillMaxWidth()) {
                Column(
                    modifier = modifier.background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                ) {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(TitleHeight))

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.detail_description_title),
                        fontFamily = customFont,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(16.dp))
                    var seeMore by remember { mutableStateOf(true) }
                    book?.let {
                        Text(
                            text = it.bookDescription,
                            fontFamily = customFont,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = if (seeMore) 1 else Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis,
                            modifier = HzPadding
                        )
                    }
                    val textButton = if (seeMore) {
                        stringResource(id = R.string.detail_open)
                    } else {
                        stringResource(id = R.string.detail_close)
                    }

                    Button(
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface, // 버튼의 배경색을 변수에 연결합니다.
                            contentColor = MaterialTheme.colorScheme.primary // 텍스트 색상을 지정합니다.
                        ), // 배경색 지정
                        onClick = {
                            seeMore = !seeMore
                        }
                    ) {
                        Text(
                            text = textButton,
                            textAlign = TextAlign.Center,
                            fontFamily = customFont,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.detail_author_name),
                        fontFamily = customFont,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
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
                            fontFamily = customFont,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = HzPadding
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    MyHorizontalDivider()

//                    Spacer(Modifier.height(300.dp))

                    book?.bookImageList?.let {
                        BookListCollection(
                            stringResource(id = R.string.detail_thumbnail),
                            bookItemsUrl = it
                        )
                    }

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
private fun BookListCollection(
    collectionName: String,
    bookItemsUrl: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {

        Spacer(Modifier.height(16.dp))

        Text(
            text = collectionName,
            fontSize = 20.sp,
            fontFamily = customFont,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier.padding(
                horizontal = 12.dp,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(items = bookItemsUrl, key =  { imageUrl -> imageUrl }) { imageUrl ->
                    BookImageRound(
                        imageUrl = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .width(200.dp)
                            .height(150.dp)
                    )
//                }
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
            fontFamily = customFont,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Black,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = HzPadding
        )
        Text(
            text = book.mainCategory,
            fontFamily = customFont,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = HzPadding
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = (book.price.toString() + " 포인트"),
            fontFamily = customFont,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
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
        BookMainImage(
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

