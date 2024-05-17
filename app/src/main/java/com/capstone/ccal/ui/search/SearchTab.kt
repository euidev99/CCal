package com.capstone.ccal.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun SearchTab(
    onDetailClick: (String) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    //get Data From Repository


    //set UI With DATA
    Column(
        modifier = Modifier
            .fillMaxSize()
    )
       {
           Text(
           text = "asD",
           modifier = Modifier.width(16.dp))
       }

}

