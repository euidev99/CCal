package com.capstone.ccal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun DefaultItem(itemWidthDp: Dp = 160.dp, itemLabel: String, itemColor: Color = Color.Red) {
    // Defines the appearance of each item in the lazy row
    Box(
        modifier = Modifier
            .size(itemWidthDp) // Set the size of the item
            .background(itemColor)
    ) {
        // Display the item label centered within the box
        Text(
            text = itemLabel,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun <T> LazyRowWithSnap(
    itemWidthDp: Dp = 160.dp, // Default width for each item
    paddingDp: Dp = 8.dp, // Default distance between items
    items: List<T>, // List of items to display
    itemsColor: Color = Color.Red, // Default color for each item
    itemContent: @Composable (T, () -> Unit) -> Unit,
    onItemClick: () -> Unit,
) {
    // Key Point 1: Remembering Scroll State and Coroutine Scope
    val listState = rememberLazyListState() // Remember the scroll state for lazy row
    val coroutineScope = rememberCoroutineScope() // Coroutine scope for launching animations

    // Calculate the width of each item in pixels, including padding
    val itemWidthPx = with(LocalDensity.current) { (itemWidthDp + paddingDp).toPx() }

    // LazyRow displaying items with specified state and modifier
    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(paddingDp)
    ) {
        items(items) { item ->
            itemContent(item, onItemClick)
//            DefaultItem(itemLabel = item, itemWidthDp = itemWidthDp, itemColor = itemsColor)
        }
    }

    // Key Point 2: LaunchedEffect for Scroll Completion and Triggering Snapping
    LaunchedEffect(Unit) {
        // Observes if scrolling is in progress and calculates the target index for snapping
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (!isScrolling) {
                    // Calculate the last item index
                    val lastItemIndex = items.size - 1
                    // Get the layout info of the LazyList
                    val layoutInfo = listState.layoutInfo
                    // Determine if the last item is visible
                    val isLastItemVisible =
                        layoutInfo.visibleItemsInfo.any { it.index == lastItemIndex }
                    if (!isLastItemVisible) {
                        // Calculate the target index for snapping when scrolling stops
                        val targetIndex = calculateTargetIndex(
                            listState.firstVisibleItemIndex,
                            listState.firstVisibleItemScrollOffset,
                            itemWidthPx,
                            items.size
                        )
                        // Animate scrolling to the target index to achieve snapping effect
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = targetIndex)
                        }
                    }
                }
            }
    }
}

// Key Point 3: Calculating the Target Index for Snapping
fun calculateTargetIndex(
    firstVisibleItemIndex: Int,
    firstVisibleItemScrollOffset: Int,
    itemWidthPx: Float,
    itemCount: Int // Pass the total number of items in the list
): Int {
    // Calculate the total scroll offset in pixels
    val totalScrollOffset = firstVisibleItemIndex * itemWidthPx + firstVisibleItemScrollOffset
    // Calculate the index based on the scroll offset
    var targetIndex = (totalScrollOffset / itemWidthPx).toInt()

    // Determine the fraction of the item that is visible
    val visibleItemFraction = totalScrollOffset % itemWidthPx
    // If more than half of the item is shown, snap to the next item
    if (visibleItemFraction > itemWidthPx / 2) {
        targetIndex++
    }

    // Special case: when the user has scrolled to the end, snap to the last item
    if (targetIndex >= itemCount - 1) {
        targetIndex = itemCount - 1
    }

    return targetIndex
}

@Preview
@Composable
fun LazyRowWithSnapPreview() {
    // Preview for LazyRowWithSnap composable function
//    LazyRowWithSnap(items = (0..10).map { "Item $it" })
}
