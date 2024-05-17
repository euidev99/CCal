package com.capstone.ccal.ui.component

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

@Composable
fun RisingView() {
    var animateState by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val offset by animateDpAsState(
        targetValue = if (animateState) (0).dp else 100.dp,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
    )

    // 애니메이션 정의
//    val translateY by infiniteTransition.animateFloat(
//        initialValue = -1f, // 초기 위치를 아래로 숨김
//        targetValue = 0f, // 위로 올라오는 애니메이션
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 1000),
//            repeatMode = RepeatMode.Reverse
//        ), label = ""
//    )

    // 애니메이션 시작
    LaunchedEffect(key1 = animateState) {
        animateState = false
        delay(100) // 애니메이션 시작 전에 잠시 대기
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // 뷰의 높이
            .padding(top = offset) // 여백 추가
//            .offset(y = offset) // Y축 방향으로 이동
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    animateState = !animateState
                }
                .zIndex(1f)
            ,
            color = Color.LightGray,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            )
        ) {
            // 뷰 내용
        }
    }
}


//var showMessage by remember { mutableStateOf(false) }

@Composable
fun AlertSnackBar(
    text: String = "",
    modifier: Modifier = Modifier,
    errorOn: Boolean
) {
    // errorOn을 상태로 관리합니다.
    var currentErrorOn by remember { mutableStateOf(errorOn) }


    // errorOn이 true인 동안에는 스낵바를 표시합니다.
    if (currentErrorOn) {
        LaunchedEffect(Unit) {
            // 3초 후에 errorOn을 false로 설정하여 스낵바를 종료합니다.
            delay(3000)
            currentErrorOn = false

        }
        Snackbar(
            modifier = modifier,
            content = {
                Text(text)
            }
        )
    }
}

