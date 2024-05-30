package com.capstone.ccal.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.capstone.ccal.R
import com.capstone.ccal.model.Order


/**
 * 내 주문 내역 확인
 */
@Composable
fun OrderItem(
    item: Order,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Spacer(modifier = Modifier.height(4.dp))

    Row(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .padding(12.dp)
            .zIndex(4f),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = stringResource(R.string.detail_close),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterVertically)
        )

        //글씨 들어갈 우측 영역
        Column(
            horizontalAlignment = Alignment.Start,
        ) {

            RowTitleContent(
                title = "상품명 : ",
                content = item.itemName
            )
            RowTitleContent(
                title = "주문일자 : ",
                content = item.orderDate
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
    HorizontalItemDivider()
}
