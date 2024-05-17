package com.capstone.ccal.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.ccal.ui.theme.OliveGreen

@Composable
fun RowTitleContent(
    title : String,
    content : String,
    modifier: Modifier = Modifier
) {
   Row(
       verticalAlignment = Alignment.CenterVertically,
       modifier = Modifier.padding(horizontal = 8.dp)
   ) {
       Text(
           text = title,
           style = MaterialTheme.typography.headlineSmall,
           color = OliveGreen,
           fontSize = 14.sp,
           maxLines = 1,
           overflow = TextOverflow.Ellipsis,
           modifier = Modifier
       )

       Text(
           text = content,
           style = MaterialTheme.typography.headlineSmall,
           fontSize = 16.sp,
           maxLines = 1,
           overflow = TextOverflow.Ellipsis,
           modifier = Modifier
               .padding(horizontal = 4.dp)
       )
   }
}