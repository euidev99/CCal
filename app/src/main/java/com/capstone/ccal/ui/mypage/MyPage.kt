package com.capstone.ccal.ui.mypage

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.ccal.CalApplication
import com.capstone.ccal.R
import com.capstone.ccal.common.UserRepository
import com.capstone.ccal.ui.component.ColumnTitleText
import com.capstone.ccal.ui.home.HomeSections
import com.capstone.ccal.ui.home.MainBottomBar
import com.capstone.ccal.ui.theme.DeepSkyBlue


private val titleHeight = 48.dp
private val mainContentOffset = titleHeight.value
private val bottomBarHeight = 48.dp
private val bottomOffset = bottomBarHeight.value
private val profileSectionHeight = 200.dp
private val profileBackgroundHeight = 180.dp

private val minProfileSectionHeight = 100.dp
private val bodyMaxOffset = titleHeight + profileSectionHeight + 50.dp
private val bodyMinOffset = profileBackgroundHeight + 12.dp

/**
 * 메인 네비게이션에서 호출하는 부분
 * 뷰 동작 이벤트 가져옴
 */
@Composable
fun MyPageTab(
    onDetailClick: (String) -> Unit, // 디테일 클릭하면 이동하게 쓸 경우
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            MainBottomBar(
                tabs = HomeSections.entries.toTypedArray(),
                currentRoute = HomeSections.MYPAGE.route,
                navigateToRoute = onNavigateToRoute)
        },
        modifier = modifier
    ) { paddingValues ->

        val myViewModel: MyPageViewModel = viewModel(factory = MyPageViewModel.provideFactory(MyPageRepo()))
//        val feedRes by feedViewModel.feedRes.observeAsState()

        val scroll = rememberScrollState(initial = 0)


        BackgroundBlue() //배경 색

        Title(
            titleString = stringResource(id = R.string.my_page_title),
            scrollProvider = { scroll.value }
        )

        LoginInfo(scroll = scroll)

        MyPage(
            scroll = scroll,
            scrollProvider = {scroll.value},
            Modifier.padding(paddingValues)
        )

    }
}

@Composable
private fun MyPage(
    scroll: ScrollState,
    scrollProvider:() -> Int,
    modifier: Modifier = Modifier
) {

    var offset by remember { mutableStateOf(bodyMaxOffset) }

    val maxOffset = with(LocalDensity.current) { bodyMaxOffset.toPx() }
    val minOffset = with(LocalDensity.current) { bodyMinOffset.toPx() }

    LaunchedEffect(scroll.value) {
        val scrollValue = scroll.value
        offset = (bodyMaxOffset - (scrollValue / 5).dp).coerceIn(bodyMinOffset, bodyMaxOffset)
    }

    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(offset)
//                .offset {
//                    val scroll = scrollProvider()
//                    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
//                    IntOffset(x = 0, y = (offset.toInt()))
//                }
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Column(
                modifier
                    .background(
                        Color.White
                    )
                    .fillMaxWidth()
            ) {

                SelectRowItem(
                    icon = Icons.Default.Star,
                    text = stringResource(id = R.string.my_page_my_book),
                    itemClick = { }
                )

                SelectRowItem(
                    icon = Icons.Default.Info,
                    text = stringResource(id = R.string.my_page_app_info),
                    itemClick = { }
                )

                SelectRowItem(
                    icon = Icons.Default.Add,
                    text = stringResource(id = R.string.my_page_get_point),
                    itemClick = { }
                )

                SelectRowItem(
                    icon = Icons.Default.Clear,
                    text = stringResource(id = R.string.my_page_logout),
                    itemClick = { }
                )

                SelectRowItem(
                    icon = Icons.Default.Clear,
                    text = stringResource(id = R.string.my_page_logout),
                    itemClick = { }
                )
            }
        }
    }
}

@Composable
private fun BackgroundBlue(
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .background(
                color = DeepSkyBlue,
                shape = RoundedCornerShape(
                    bottomStart = 48.dp,
                    bottomEnd = 48.dp
                ),
            )
//            .border(
//                width = 4.dp, // 테두리 두께
//                color = SandGreen, // 테두리 색상
//                shape = RoundedCornerShape(48.dp) // 모서리 라운드 처리와 일치시킵니다.
//            )
            .height(profileBackgroundHeight)
            .fillMaxWidth()
    ) {

    }
}

@Composable
private fun ProfileSection(

) {

}

@Composable
private fun LoginInfo(
    scroll: ScrollState,
    modifier: Modifier = Modifier
) {
    var infoHeight by remember { mutableStateOf(profileSectionHeight) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(24.dp)
    ) {
        val userEmail = UserRepository.getEmail(CalApplication.ApplicationContext())

        Spacer(modifier = modifier.height(titleHeight))
        
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
//                .verticalScroll(scroll)
                .padding(
                    top = 8.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 8.dp
                )
                .fillMaxWidth()
                .height(infoHeight)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(32.dp)
                )
                .border(
                    width = 2.dp, // 테두리 두께
                    color = Color.Black, // 테두리 색상
                    shape = RoundedCornerShape(32.dp) // 모서리 라운드 처리와 일치시킵니다.
                )
        ) {
            Column {
                ColumnTitleText(text = stringResource(id = R.string.login_email))
                Text(
                    text = "사용자 이메일",
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = null,
                modifier
                    .size(75.dp)
                    .background(DeepSkyBlue)
            )
        }
    }

    LaunchedEffect(scroll.value) {
        val scrollValue = scroll.value
        infoHeight = (profileSectionHeight - (scrollValue / 5).dp).coerceIn(minProfileSectionHeight, profileSectionHeight)
    }
}

@Composable
private fun SelectRowItem(
    icon: ImageVector = Icons.Default.Done,
    text: String = "",
    itemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
        ,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp)
                .background(
                    color = DeepSkyBlue,
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 2.dp, // 테두리 두께
                    color = Color.Black, // 테두리 색상
                    shape = RoundedCornerShape(24.dp) // 모서리 라운드 처리와 일치시킵니다.
                )
                .height(64.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = modifier.width(16.dp))
            Icon(
                imageVector = icon,
                contentDescription = null
            )
            Spacer(modifier = modifier.width(20.dp))
            ColumnTitleText(text = text, color = Color.White)
            Spacer(modifier = modifier.width(16.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))
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
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,//MaterialTheme.colorScheme.primary,
            modifier = modifier
                .align(Alignment.Center)
        )
    }
}




