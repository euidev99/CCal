package com.capstone.ccal.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.capstone.ccal.R
import com.capstone.ccal.ui.theme.PastelGreenLight
import com.capstone.ccal.ui.theme.customFont

@Composable
fun RoundTextButton(
    mainText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var buttonColor by remember { mutableStateOf(Color.Gray) }

    var isButtonPressed by remember { mutableStateOf(false) }

    Button(
        onClick = {
            onClick()
                  }
        ,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium, // 둥근 모서리를 가진 버튼
//        border = BorderStroke(1.dp, Color.Gray), // 테두리 설정
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isButtonPressed) Color.Gray else MaterialTheme.colorScheme.onSurface, // 버튼의 배경색을 변수에 연결합니다.
            contentColor = MaterialTheme.colorScheme.primary // 텍스트 색상을 지정합니다.
        ), // 배경색 지정

    ) {
        Text(
            text = mainText,
            fontFamily = customFont,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary // 텍스트 색상을 primary로 지정
        )
    }
}