package com.capstone.ccal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
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
import com.capstone.ccal.model.AuthorDto
import com.capstone.ccal.ui.util.mirroringBackIcon


/**
 * 작가 디테일 팝업
 */
@Composable
fun AuthorDetailPopup(
    item: AuthorDto?, //작가 정보
//    itemCollection: List<BookItemDto?>?, //작가가 쓴 책
    onDismiss: () -> Unit, //닫기
    modifier: Modifier = Modifier
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onSurface)
                .wrapContentHeight()
                .padding(24.dp)
                .fillMaxWidth(0.9f)
        ) {

            DetailPopupHeader {
                onDismiss()
            }

            if (item != null) {
                AuthorInfo(
                    item = item
                )
            }
        }
    }
}

@Composable
private fun DetailPopupHeader(
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

        ColumnTitleText(text = stringResource(R.string.detail_author_name))

    }

    HorizontalItemDivider()

}

@Composable
private fun AuthorInfo(
    item: AuthorDto
) {
    Column {
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row() {
            BookMainImage(
                imageUrl = item.imageUrl,
                contentDescription = "작가 프로필",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                ColumnTitleText(text = stringResource(id = R.string.detail_author_name))

                Spacer(modifier = Modifier.height(4.dp))
                ColumnTitleText(text = item.authorName)
                Spacer(modifier = Modifier.height(4.dp))
                ColumnTitleText(text = item.description)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

    }
}
