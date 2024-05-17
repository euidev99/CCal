package com.capstone.ccal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.capstone.ccal.model.BookCategoryDto
import com.capstone.ccal.ui.theme.Black40
import com.capstone.ccal.ui.theme.Black99
import com.capstone.ccal.ui.theme.Grey4a


@Composable
fun BookCategoryItem(
    categoryDto: BookCategoryDto,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .background(
                color = if (isSelected) Color.White else Color.LightGray,
                shape = RoundedCornerShape(8.dp),
            )
            .border(
                width = 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(8.dp),
            )
            .widthIn(min = 40.dp)
            .height(32.dp)
            .clickable {
                onToggle()
            }
//            .wrapContentHeight(Alignment.CenterVertically)
    ) {
        Text(
            text = categoryDto.category,
            fontSize = 14.sp,
            style = MaterialTheme.typography.labelMedium,
            color = (if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun BookCategoryCollection(
    collection: List<BookCategoryDto>,
    selectedIndex: Int,
    scrollProvider:() -> Int,
    onToggle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
    ) {
        //title Size
//        Spacer(modifier = Modifier.padding(top = 56.dp))

        val maxOffset = with(LocalDensity.current) { 48.dp.toPx() }
        val minOffset = with(LocalDensity.current) { 0.dp.toPx() }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
            modifier = Modifier
                .offset {
                    val scroll = scrollProvider()
                    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                    IntOffset(x = 0, y = (offset.toInt()))
                }
                .fillMaxWidth()
                .background(Black99)
                .padding(vertical = 2.dp)
        ) {
            itemsIndexed(collection) {index, item ->
                BookCategoryItem(
                    categoryDto = item,
                    isSelected = selectedIndex == index,
                    onToggle = { onToggle(index) }
                )
            }
        }
    }
}