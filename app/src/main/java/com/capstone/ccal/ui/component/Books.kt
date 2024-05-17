package com.capstone.ccal.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.capstone.ccal.R
import com.capstone.ccal.model.BookItemDto


@Composable
fun BookItemVertical(
    item: BookItemDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                onClick()
            }
            .width(150.dp)
            .height(200.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp),
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp),
            )
//            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp), clip = false)
            .zIndex(4f)
    ) {

        //이미지 상에 그라데이션 추가하려고
        Box(
            modifier
                .background(
                    brush = Brush.verticalGradient(listOf(Color.White, Color.White, Color.White)),
                    alpha = 0.4f,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            val bookIcon =
                painterResource(id = R.drawable.magic_book_icon) // 이 부분에서 에셋에서 이미지 리소스를 가져옵니다.
            Image(
                painter = bookIcon,
                contentDescription = "Book Default Image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .align(Alignment.Center)// 이미지의 크기 조정을 위해 Modifier 사용
            )
        }

        HorizontalItemDivider()

        //왼쪽에 붙이고 싶어서 사용
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {

            RowTitleContent(
                title = "제목 : ",
                content = item.bookName
            )

            RowTitleContent(
                title = "작가 : ",
                content = item.authorList.get(0).authorName
            )
        }
    }
}

@Composable
fun PagingBookItem(
    item: BookItemDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                onClick()
            }
            .width(300.dp)
            .wrapContentHeight(Alignment.CenterVertically)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp),
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp),
            )
//            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp), clip = false)
            .zIndex(4f)
    ) {

        //이미지 상에 그라데이션 추가하려고
        Box(
            modifier
                .background(
                    brush = Brush.verticalGradient(listOf(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.tertiary,
                        Color.White)),
                    alpha = 0.4f,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            val bookIcon =
                painterResource(id = R.drawable.book_opened) // 이 부분에서 에셋에서 이미지 리소스를 가져옵니다.
            Image(
                painter = bookIcon,
                contentDescription = "Book Default Image",
                modifier = Modifier
                    .size(180.dp)
                    .padding(8.dp)
                    .align(Alignment.Center)// 이미지의 크기 조정을 위해 Modifier 사용
            )
        }

        HorizontalItemDivider()

        //왼쪽에 붙이고 싶어서 사용
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {

            RowTitleContent(
                title = "제목 : ",
                content = item.bookName
            )

            RowTitleContent(
                title = "작가 : ",
                content = item.authorList.get(0).authorName
            )
        }
    }
}


@Preview
@Composable
private fun PreviewBookItem(

) {
    BookItemVertical(item = BookItemDto(), onClick = { })
}
