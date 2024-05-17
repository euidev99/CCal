package com.capstone.ccal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.capstone.ccal.R
import com.capstone.ccal.model.AuthorDto
import com.capstone.ccal.model.BookItemDto


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
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            item {
                if (item != null) {
                    DetailPopupHeader(
                        onDismiss
                    )
                    AuthorInfo(
                        item = item
                    )
                }
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
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.detail_close),
            )
        }

        Text(
            text = stringResource(R.string.detail_author_name),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(
                    horizontal = 12.dp,
                    vertical = 12.dp
                )
        )
    }

    HorizontalItemDivider()
}

@Composable
private fun AuthorInfo(
    item: AuthorDto
) {
   Column {
       Text(text = item.authorName)
       Text(text = item.description)
       Text(text = item.authorType)
   } 
}