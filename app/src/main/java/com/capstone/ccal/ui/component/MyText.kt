package com.capstone.ccal.ui.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.capstone.ccal.ui.theme.customFont
import kotlinx.coroutines.delay

/**
 * 텍스트 스타일 지정을 할 수도 있지만,
 * 그냥 컴포넌트를 사용
 */
//제목에 쓰는거
@Composable
fun TitleText(
    text: String,
    fontSize: TextUnit = 24.sp,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = fontSize,
        color = color,
        fontFamily = customFont,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

//이름 : 제목 : 이런 컬럼에 쓸 것
@Composable
fun ColumnTitleText(
    text: String,
    fontSize: TextUnit = 18.sp,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = fontSize,
        color = color,
        fontFamily = customFont,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

//장문의 뭐 그런거 쓸 때
@Composable
fun ContentText(
    text: String,
    fontSize: TextUnit = 18.sp,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = fontSize,
        fontFamily = customFont,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        color = color,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

//// 깜빡이는 텍스트
//@Composable
//fun BlinkingText(
//    text: String,
//    fontSize: TextUnit = 18.sp,
//    style: TextStyle = MaterialTheme.typography.titleLarge,
//    color: Color = MaterialTheme.colorScheme.primary,
//    intervalMillis: Long = 500,
//) {
//    var isVisible by remember { mutableStateOf(true) }
//
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(intervalMillis)
//            isVisible = !isVisible
//        }
//    }
//
//    if (isVisible) {
//        Text(
//            text = text,
//            fontSize = fontSize,
//            style = style,
//            color = color,
//            textAlign = TextAlign.Center,
//        )
//    } else {
//        Text(
//            text = text,
//            fontSize = fontSize,
//            style = style,
//            color = Color.Transparent,
//            textAlign = TextAlign.Center,
//        )
//    }
//}


@Composable
fun BlinkingText(
    text: String,
    fontSize: TextUnit = 18.sp,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    color: Color = MaterialTheme.colorScheme.primary,
    intervalMillis: Long = 1000,
    minAlpha: Float = 0f,
    maxAlpha: Float = 1f) {
    var alpha by remember { mutableFloatStateOf(1f) }
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val alphaAnimation by infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = maxAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = intervalMillis.toInt()),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    alpha = alphaAnimation

    Text(
        text = text,
        color = color.copy(alpha = alpha), // 텍스트의 투명도를 변경
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
        fontFamily = customFont,
        fontStyle = FontStyle.Normal,
        textAlign = TextAlign.Center
    )
}
