package com.capstone.ccal.ui.search

import android.icu.text.CaseMap.Title
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.ccal.CalApplication
import com.capstone.ccal.R
import com.capstone.ccal.model.BookDetailItem
import com.capstone.ccal.ui.component.AlertSnackBar
import com.capstone.ccal.ui.component.BookItemSearch
import com.capstone.ccal.ui.component.ColumnTitleText
import com.capstone.ccal.ui.component.HorizontalItemDivider
import com.capstone.ccal.ui.component.ProgressWithText
import com.capstone.ccal.ui.home.HomeSections
import com.capstone.ccal.ui.home.MainBottomBar
import com.capstone.ccal.ui.navigation.MainDestination
import com.capstone.ccal.ui.theme.customFont
import kotlin.system.exitProcess


private val titleHeight = 48.dp
private val titleOffset = 0.dp

private val searchTabHeight = 48.dp
private val searchTabOffset = titleOffset + titleHeight
private val searchTabMinOffset = titleOffset

private val searchCollectionOffset = searchTabHeight + searchTabOffset
private val searchCollectionMinOffset = searchTabHeight

@Composable
fun SearchTab(
    onDetailClick: (String) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            MainBottomBar(
                tabs = HomeSections.entries.toTypedArray(),
                currentRoute = HomeSections.SEARCH.route,
                navigateToRoute = onNavigateToRoute)
        },
        modifier = modifier
    ) { paddingValues ->

        val scroll = rememberScrollState(initial = 0)

        val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.provideFactory(
            SearchRepo()
        ))

        //버퍼링 프로세스
        val isLoading by searchViewModel.loadingProgressState
        if (isLoading) {
            ProgressWithText(
                color = MaterialTheme.colorScheme.primary
            )
        }

        val errorOn by searchViewModel.errorMessageOn
        val errorMessage by searchViewModel.messageResult
        val messageAlpha by animateFloatAsState(
            targetValue = if (errorOn) 1f else 0f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
        )

        if (errorOn) { // 상태에 따라나 스낵바가 나타나도록 함
//        Log.d("seki", "Error Occured message : $errorMessage")
            AlertSnackBar(
                text = errorMessage,
                errorOn = errorOn,
                modifier = Modifier
                    .alpha(messageAlpha)
                    .padding(16.dp)
                    .zIndex(1f)
            )
        }

        Search(
            viewModel = searchViewModel,
            scroll = scroll,
            onDetailClick = onDetailClick,
            Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun Search(
    viewModel: SearchViewModel,
    scroll: ScrollState,
    onDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {

        val searchRes by viewModel.searchResult.observeAsState()
//
//        Title(
//            titleString = stringResource(id = R.string.search_main_title),
//            scrollProvider = { scroll.value }
//        )

        SearchHead(
            viewModel = viewModel,
            scroll = scroll,
            scrollProvider = { scroll.value },
            Modifier.zIndex(1f)
        )

        SearchItemCollection(
            bookItemList = searchRes?.collectionList,
            onDetailClick = onDetailClick,
            scroll = scroll,
            scrollProvider = { scroll.value }
        )
    }
}

@Composable
private fun Title(
    titleString: String,
    scrollProvider:() -> Int,
    modifier: Modifier = Modifier
) {
//    val maxOffset = with(LocalDensity.current) { titleOffset.toPx() }
//    val minOffset = with(LocalDensity.current) { -titleOffset.toPx() }

    Box(
        modifier = modifier
            .height(titleHeight)
//            .offset {
//                val scroll = scrollProvider()
//                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
//                IntOffset(x = 0, y = (offset.toInt()))
//            }
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
private fun SearchHead(
    viewModel: SearchViewModel,
    scroll: ScrollState,
    scrollProvider: () -> Int,
    modifier: Modifier = Modifier
) {
    val inputHint = stringResource(id = R.string.search_hint)
    var searchInputText by rememberSaveable { mutableStateOf("") }

    val maxOffset = with(LocalDensity.current) { searchTabOffset.toPx() }
    val minOffset = with(LocalDensity.current) { searchTabMinOffset.toPx() }

    Column(
        modifier
//            .background(Color.Black)
//            .verticalScroll(scroll)
//            .offset {
//                val scrollValue = scrollProvider()
//                val offset = (maxOffset - scrollValue).coerceAtLeast(minOffset)
//                IntOffset(x = 0, y = (offset.toInt()))
//            }
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
            ,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current

            OutlinedTextField(
                placeholder = { ColumnTitleText(inputHint) },
                value = searchInputText,
                onValueChange = { input -> searchInputText = input },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                ),
                trailingIcon = {
                    Row() {
                        Icon(
                            imageVector = Icons.Default.Search,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { viewModel.requestWithKeyword(searchInputText) }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.Clear,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    searchInputText = ""
                                }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                },
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp
                    )
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.requestWithKeyword(searchInputText)
                    keyboardController?.hide()
                })
            )
        }
    }
}

@Composable
private fun SearchItemCollection(
    bookItemList: List<BookDetailItem>?,
    onDetailClick: (String) -> Unit,
    scroll: ScrollState,
    scrollProvider: () -> Int,
    modifier: Modifier = Modifier
) {
//    val maxOffset = with(LocalDensity.current) { searchCollectionOffset.toPx() }
//    val minOffset = with(LocalDensity.current) { searchCollectionMinOffset.toPx() }

    Spacer(modifier = Modifier.height(16.dp))

    HorizontalItemDivider()

    if (bookItemList.isNullOrEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .background(
                    Color.Transparent
//                    color = MaterialTheme.colorScheme.primary,
//                    shape = RoundedCornerShape(
//                        topStart = 32.dp,
//                        topEnd = 32.dp
//                    ),
                )
        ) {
            ColumnTitleText(text = stringResource(id = R.string.search_result_hint))
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxSize()
                .background(
                    Color.Transparent
//                    color = MaterialTheme.colorScheme.primary,
//                    shape = RoundedCornerShape(
//                        topStart = 32.dp,
//                        topEnd = 32.dp
//                    ),
                )
        ) {
            itemsIndexed(bookItemList) { index, item ->
                BookItemSearch(
                    item = item,
                    onClick = { onDetailClick(item.bookId) })
            }
        }
    }
}