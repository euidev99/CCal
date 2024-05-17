package com.capstone.ccal.ui.component

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

/**
 * 배경의 Gradient 혹은 커스텀이 필요할 때,
 * 여러번 정의 안하기 위해 Modifier 의 Extension 으로 정의
 */

//@Composable
fun Modifier.verticalGradientBackground(
    colors: List<Color>,
    start: Float = 0F,
    end : Float = 100F
) = background(
    brush = Brush.verticalGradient(
        colors = colors
    )
)

fun Modifier.horizontalGradientBackground(
    colors: List<Color>,
    start: Float = 0F,
    end : Float = 100F
) = background(
    brush = Brush.horizontalGradient(
        colors = colors
    )
)

/*
사용 예시
LazyRow(
    content = {
        items(10) { index ->
            val itemWidth = 100.dp
            val margin = 8.dp
            val startOffset = (itemWidth + margin) * index

            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .padding(start = if (index == 0) 0.dp else margin)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Red, Color.Blue),
                            start = Offset(startOffset.toPx(), 0f),
                            end = Offset((startOffset + itemWidth).toPx(), 0f)
                        )
                    )
            ) {
                // 각 아이템의 내용
            }
        }
    }
)


 */

