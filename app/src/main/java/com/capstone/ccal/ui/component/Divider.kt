package com.capstone.ccal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.capstone.ccal.ui.theme.ForestGreen
import com.capstone.ccal.ui.theme.Gold
import com.capstone.ccal.ui.theme.GoldSand
import com.capstone.ccal.ui.theme.OliveGreen
import com.capstone.ccal.ui.theme.OliveSatin
import com.capstone.ccal.ui.theme.SandGreen
import java.util.Random


/**
 * Dot 디자인 패턴
 */
@Composable
fun DotHorizontalDivider(
    index : Int = 0,
    height :Dp = 75.dp,
    modifier : Modifier = Modifier,
) {

    Spacer(modifier = Modifier.padding(8.dp))

    Column(
        modifier
            .fillMaxWidth()
            .height(height)
            .background(
                if (index % 3 == 0) Gold
                else if (index % 3 == 1) SandGreen
                else if (index % 3 == 2) Color.Red
                else GoldSand
            )
            .drawBehind {
                repeat(30) {
                    val x = Random()
                        .nextInt(size.width.toInt())
                        .toFloat()
                    val y = Random()
                        .nextInt(size.height.toInt())
                        .toFloat()
                    val radius = Random().nextFloat() * 30f
                    drawCircle(Color.Black, radius, Offset(x, y))
                }
            },
        horizontalAlignment = if (index % 2==0) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        //nothing

    }

//    Spacer(modifier = Modifier.padding(8.dp))
}



/**
 * 전체 항목 크게 나누기
 */
@Composable
fun MyHorizontalDivider(
    color: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
    thickness: Int = 10, // in pixels
    modifier: Modifier = Modifier
) {
    Spacer(modifier = Modifier.padding(8.dp))

    Box(
        modifier
            .fillMaxWidth()
            .height(thickness.dp)
            .padding(
//                horizontal = 12.dp,
//                vertical = 4.dp
            )
            .background(color)
    )
}

/**
 * 아이템 사이에 줄 긋기
 */
@Composable
fun HorizontalItemDivider(
    color: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
    thickness: Int = 2, // in pixels
    modifier: Modifier = Modifier
) {
//    Spacer(modifier = Modifier.padding(8.dp))
    
    Box(
        modifier
            .fillMaxWidth()
            .height(thickness.dp)
            .padding(
                horizontal = 8.dp,
            )
            .background(color)
    )
}