package com.capstone.ccal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.capstone.ccal.R


/**
 * 작가 디테일 팝업
 */
@Composable
fun AppInfoPopup(
    onDismiss: () -> Unit, //닫기
    modifier: Modifier = Modifier
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
    ) {

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.onSurface)
                .wrapContentHeight()
                .padding(24.dp)
        ) {

            InfoPopupHeader {
                onDismiss()
            }

            AppInfo()
        }
    }
}

@Composable
private fun InfoPopupHeader(
    onDismiss: () -> Unit
) {
    HorizontalItemDivider()

    Row(
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
                imageVector = Icons.Default.Clear,
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = stringResource(R.string.back_description)
            )
        }

        ColumnTitleText(text = stringResource(R.string.app_info_popup_title))

    }

    HorizontalItemDivider()

}

@Composable
private fun AppInfo(
    modifier : Modifier = Modifier
) {
    Column {

        Spacer(modifier = Modifier.height(16.dp))
        
        ColumnTitleText(text = stringResource(id = R.string.app_info_description))

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalItemDivider()

        Spacer(modifier = Modifier.height(16.dp))

        ColumnTitleText(
            text = stringResource(id = R.string.app_info_maker),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        ColumnTitleText(text = "팀장 : 심의석 ( 안드로이드 마켓 앱 개발 ")
        Spacer(modifier = Modifier.height(8.dp))
        ColumnTitleText(text = "팀원 : 장영지 ( 책 기획 및 유니티 에셋 작업 ")
        Spacer(modifier = Modifier.height(8.dp))
        ColumnTitleText(text = "팀원 : 이예빈 ( 3D 모델링 및 유니티 스크립트 작업 ")
        Spacer(modifier = Modifier.height(8.dp))
        ColumnTitleText(text = "팀원 : 임나슬 ( 유니티 스크립트 & 씬 작업 및 모델링 작업")

        HorizontalItemDivider()

        Spacer(modifier = Modifier.height(16.dp))

        ColumnTitleText(
            text = stringResource(id = R.string.app_info_popup_copyrights),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ColumnTitleText(text = "폰트 사용 : 넷마블 폰트를 사용하였습니다. \n 이 페이지 에는 넷마블에서 제공한 넷마블체가 적용되어 있습니다.")

        Spacer(modifier = Modifier.height(16.dp))
    }
}
