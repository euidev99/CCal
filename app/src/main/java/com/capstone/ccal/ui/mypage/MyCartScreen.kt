package com.capstone.ccal.ui.mypage

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.ccal.R
import com.capstone.ccal.model.Order
import com.capstone.ccal.ui.component.AlertSnackBar
import com.capstone.ccal.ui.component.ColumnTitleText
import com.capstone.ccal.ui.component.HorizontalItemDivider
import com.capstone.ccal.ui.component.OrderItem
import com.capstone.ccal.ui.component.ProgressWithText
import com.capstone.ccal.ui.util.mirroringBackIcon

/**
 * 상품 구매 팝업
 */
@Composable
fun MyCartScreen(
    onDismiss: () -> Unit, //닫기
    onDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: MyCartViewModel = viewModel(
        factory = MyCartViewModel.provideFactory(
            MyCartRepository()
        )
    )

    //버퍼링 프로세스
    val isLoading by viewModel.loadingProgressState

    if (isLoading) {
        ProgressWithText(
            text = "목록을 가져오는 중입니다.",
            color = MaterialTheme.colorScheme.primary,
        )
    }

    val errorOn by viewModel.messageState
    val errorMessage by viewModel.stringResult
    val messageAlpha by animateFloatAsState(
        targetValue = if (errorOn) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
    )

    if (errorOn) { // 상태에 따라나 스낵바가 나타나도록 함
        AlertSnackBar(
            text = errorMessage,
            errorOn = errorOn,
            modifier = Modifier
                .alpha(messageAlpha)
                .padding(16.dp)
                .zIndex(1f)
        )
    }

    val orderRes by viewModel.orderListResponse.observeAsState()

    // 중복 호출을 막기 위함
    LaunchedEffect(Unit) {
        if (orderRes == null) {
            viewModel.getMyOrder()
        }
    }
    Column(
        modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {

        MyCartHead(
            onDismiss = onDismiss,
            Modifier.zIndex(1f)
        )

        OrderItemCollection(
            itemList = orderRes?.orderList,
            onDetailClick = onDetailClick,
        )

    }
}

@Composable
private fun MyCartHead(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                    .zIndex(5f)
            ) {
                Icon(
                    imageVector = mirroringBackIcon(),
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    contentDescription = stringResource(R.string.back_description)
                )

            }
            ColumnTitleText(text = "내 주문 아이템")
        }
    }
}
@Composable
private fun OrderItemCollection(
    itemList: List<Order>?,
    onDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Spacer(modifier = Modifier.height(16.dp))

    HorizontalItemDivider()

    if (itemList.isNullOrEmpty()) {
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
            ColumnTitleText(text = "구매내역 정보가 없습니다.")
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
            itemsIndexed(itemList) { index, item ->
                OrderItem(
                    item = item,
                    onClick = { onDetailClick(item.itemId) })
            }

            item {
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
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
            )
            .zIndex(5f)
    ) {
        Icon(
            imageVector = mirroringBackIcon(),
            tint = MaterialTheme.colorScheme.surfaceTint,
            contentDescription = stringResource(R.string.back_description)
        )
    }
}
