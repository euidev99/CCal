package com.capstone.ccal.ui.home

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import java.util.Random
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.ccal.CalApplication
import com.capstone.ccal.R
import com.capstone.ccal.model.BookCategoryDto
import com.capstone.ccal.model.BookTypeCollection
import com.capstone.ccal.ui.component.BookCategoryCollection
import com.capstone.ccal.ui.component.BookItemVertical
import com.capstone.ccal.ui.component.DotHorizontalDivider
import com.capstone.ccal.ui.component.LazyRowWithSnap
import com.capstone.ccal.ui.component.MyHorizontalDivider
import com.capstone.ccal.ui.component.PagingBookItem
import com.capstone.ccal.ui.component.ProgressWithText
import com.capstone.ccal.ui.theme.ForestGreen
import com.capstone.ccal.ui.theme.Gold
import com.capstone.ccal.ui.theme.GoldSand
import com.capstone.ccal.ui.theme.Grey66
import com.capstone.ccal.ui.theme.LightYellow
import com.capstone.ccal.ui.theme.OliveGreen
import com.capstone.ccal.ui.theme.PastelGreen
import com.capstone.ccal.ui.theme.PastelGreenLight
import com.capstone.ccal.ui.theme.SandGreen
import com.capstone.ccal.ui.theme.SandStone
import com.capstone.ccal.ui.theme.customFont
import kotlin.system.exitProcess


/**
 * 메인 네비게이션에서 호출하는 부분
 * 뷰 동작 이벤트 가져옴
 */
@Composable
fun Feed(
    onDetailClick: (String) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            MainBottomBar(
                tabs = HomeSections.entries.toTypedArray(),
                currentRoute = HomeSections.FEED.route,
                navigateToRoute = onNavigateToRoute)
        },
        modifier = modifier
//            .background(
//                GoldSand
//            )
//            .drawBehind {
//                repeat(30) {
//                    val x = Random()
//                        .nextInt(size.width.toInt())
//                        .toFloat()
//                    val y = Random()
//                        .nextInt(size.height.toInt())
//                        .toFloat()
//                    val radius = Random().nextFloat() * 110f
//                    drawCircle(Color.White, radius, Offset(x, y))
//                }
//            },
//            .background(MaterialTheme.colorScheme.background)
    ) { paddingValues ->

        var exit by remember { mutableStateOf(false) }
        var exitPressedTime by remember { mutableStateOf(0L) }
        val exitString = stringResource(id = R.string.common_confirm_exit)
        BackHandler { // 백프레스 이벤트
            if (exit) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - exitPressedTime < 2000) {
                    exitProcess(0)
                } else {
                    Toast.makeText(CalApplication.ApplicationContext(), exitString, Toast.LENGTH_SHORT).show()
                    exitPressedTime = currentTime
                }
            } else {
                exit = true
                exitPressedTime = System.currentTimeMillis()
                Toast.makeText(CalApplication.ApplicationContext(), exitString, Toast.LENGTH_SHORT).show()
            }
        }

        val feedViewModel: FeedViewModel = viewModel(factory = FeedViewModel.provideFactory(FeedRepo()))
        val feedRes by feedViewModel.feedRes.observeAsState()
        val categoryRes by feedViewModel.categoryRes.observeAsState()

        // 중복 호출을 막기 위함
        LaunchedEffect(Unit) {
            if (feedRes == null) {
                feedViewModel.fetchData()
            }
        }

        val scroll = rememberScrollState(initial = 0)
        var selectedCategoryIndex by remember { mutableIntStateOf(-1) }

        Feed(
            scroll = scroll,
            selectedCategoryIndex = selectedCategoryIndex,
            onSelectCategory = { index ->
                if (index != selectedCategoryIndex) {
                    selectedCategoryIndex = index
                }
            },
            feedCollection = feedRes?.collectionList,
            categoryCollection = categoryRes?.categoryList,
            onDetailClick = onDetailClick,
            Modifier
                .padding(paddingValues)//MaterialTheme.colorScheme.background)
        )
    }
}


@Composable
private fun Feed(
    scroll: ScrollState,
    selectedCategoryIndex: Int,
    onSelectCategory: (Int) -> Unit,
    feedCollection: List<BookTypeCollection>?,
    categoryCollection: List<BookCategoryDto>?,
    onDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
//            .drawBehind {
//                repeat(15) {
//                    val x = Random()
//                        .nextInt(size.width.toInt())
//                        .toFloat()
//                    val y = Random()
//                        .nextInt(size.height.toInt())
//                        .toFloat()
//                    val radius = Random().nextFloat() * 200f
//                    drawCircle(Color.White, radius, Offset(x, y))
//                }
//            },
    ) {

        Title(
            titleString = stringResource(id = R.string.main_title),
            scrollProvider = { scroll.value },
        )

        categoryCollection?.let {
            BookCategoryCollection(
                collection = categoryCollection,
                selectedIndex = selectedCategoryIndex,
                onToggle = onSelectCategory,
                scrollProvider = { scroll.value },
                modifier = Modifier.zIndex(1f)
            )
        }

        if (feedCollection == null) {
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
            FeedCollections(
                feedCollections = feedCollection,
                onDetailClick = onDetailClick,
                scroll = scroll,
                scrollProvider = { scroll.value },
                modifier = Modifier.zIndex(0f)
            )
        }
    }
}

@Composable
private fun Title(
    titleString: String,
    scrollProvider:() -> Int,
    modifier: Modifier = Modifier
) {
    val maxOffset = with(LocalDensity.current) { 0.dp.toPx() }
    val minOffset = with(LocalDensity.current) { -48.dp.toPx() }

    Box(
        modifier = modifier
            .height(48.dp)
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = (offset.toInt()))
            }
            .padding(paddingValues = PaddingValues(start = 12.dp, end = 12.dp))
    ) {
        Text(
            text = titleString,
            fontSize = 24.sp,
            fontFamily = customFont,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun FeedCollections(
    feedCollections: List<BookTypeCollection>,
    onDetailClick: (String) -> Unit,
    scroll: ScrollState,
    scrollProvider: () -> Int,
    modifier: Modifier = Modifier
) {
    val maxOffset = with(LocalDensity.current) { 96.dp.toPx() }
    val minOffset = with(LocalDensity.current) { 40.dp.toPx() }

    Column(
        modifier
            .verticalScroll(scroll)
            .offset {
                val scrollValue = scrollProvider()
                val offset = (maxOffset - scrollValue).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = (offset.toInt()))
            }
    ) {
        Log.d("seki", "scroll ${scroll.value}" )

        for ((index, collection) in feedCollections.withIndex()) {
            when (collection.collectionType) {
                0 -> {
                    BookCollection(
                        collection = collection,
                        onDetailClick = onDetailClick,
                        index = index,
                        scrollValue = { scroll.value }
                    )
                }
                1 -> {
                    BookPagingCollection(
                        collection = collection,
                        onDetailClick = onDetailClick,
                        index = index,
                        scrollValue = { scroll.value }
                    )
                }
                2 -> {
//                    PromotionSection(
//                        collection = collection,
//                        onDetailClick = onDetailClick,
//                        index = index,
//                        scrollValue = { scroll.value }
//                    )
                }
            }
        }
        
        Spacer(modifier = modifier.height(56.dp))
    }
}

@Composable
private fun PromotionSection(
    collection: BookTypeCollection,
    onDetailClick: (String) -> Unit,
    index: Int,
    scrollValue:() -> Int,
    modifier: Modifier = Modifier
) {
    val item = collection.itemList[0]

    Column(
        modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(
                if (index % 3 == 0) OliveGreen
                else if (index % 3 == 1) SandGreen
                else if (index % 3 == 2) ForestGreen
                else GoldSand
            )
            .drawBehind {
                repeat(5) { index ->
                    val x = (index + 1) * size.width / 6 // 수직 위치 계산
                    drawLine(
                        color = Color.White,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 20f
                    )
                }
            }
            .clickable { onDetailClick(item.bookId) },
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center

    ) {
        Column(
            Modifier
                .fillMaxWidth(0.2f)
//                .background(Color.Gray)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}


/**
 * 책 기본 아이템
 */
@Composable
private fun BookCollection(
    collection: BookTypeCollection,
    onDetailClick: (String) -> Unit,
    index: Int,
    scrollValue:() -> Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = collection.collectionName,
        fontSize = 20.sp,
        fontFamily = customFont,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(
            horizontal = 12.dp,
            vertical = 12.dp
        )
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
    ) {
        itemsIndexed(collection.itemList) { index, item ->
            Column {
                BookItemVertical(
                    item = item,
                    onClick = {
                        onDetailClick(item.bookId)
                        Log.d("seki", "Collection item Clicked ${item.bookId}")
                    })
            }
        }
    }

    DotHorizontalDivider(index)
}

@Composable
private fun BookPagingCollection(
    collection: BookTypeCollection,
    onDetailClick: (String) -> Unit,
    index: Int,
    scrollValue:() -> Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = collection.collectionName,
        fontSize = 20.sp,
        fontFamily = customFont,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,//MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(
            horizontal = 12.dp,
            vertical = 12.dp
        )
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
    ) {
        itemsIndexed(collection.itemList) { index, item ->
            Column {
                PagingBookItem(
                    item = item,
                    onClick = {
                        onDetailClick(item.bookId)
                        Log.d("seki", "Collection item Clicked ${item.bookId}")
                    })
            }
        }
    }

    DotHorizontalDivider(index)
}



