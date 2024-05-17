package com.capstone.ccal.ui.home

import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.core.os.ConfigurationCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.capstone.ccal.R
import com.capstone.ccal.ui.component.MyHorizontalDivider
import com.capstone.ccal.ui.mypage.MyPageTab
import com.capstone.ccal.ui.search.SearchTab
import java.util.Locale

enum class HomeSections(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String
) {
    FEED(R.string.main_feed, Icons.Outlined.Home, "home/feed"),
    SEARCH(R.string.main_search, Icons.Outlined.Search, "home/search"),
    MYPAGE(R.string.my_page, Icons.Outlined.Face, "home/mypage")
}

fun NavGraphBuilder.addHomeGraph(
    onItemSelected: (String, NavBackStackEntry) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    composable(HomeSections.FEED.route) { from ->
        Feed(onDetailClick = { id -> onItemSelected(id, from) }, onNavigateToRoute, modifier)
    }
    composable(HomeSections.SEARCH.route) { from ->
        SearchTab(onDetailClick = { id -> onItemSelected(id, from) }, onNavigateToRoute, modifier)
    }
    composable(HomeSections.MYPAGE.route) { from ->
        MyPageTab(onDetailClick = { id -> onItemSelected(id, from) }, onNavigateToRoute, modifier )
    }

}

@Composable
fun MainTopBar(
    title: String,
    color: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.secondary
) {
    Surface(
        modifier = Modifier,
        shape = RectangleShape,
        color = color,
        contentColor = contentColor
    ) {
        Column {
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                val bookIcon =
                    painterResource(id = R.drawable.magic_book_icon) // 이 부분에서 에셋에서 이미지 리소스를 가져옵니다.
                Image(
                    painter = bookIcon,
                    contentDescription = "Book Default Image",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                        .align(Alignment.CenterVertically)// 이미지의 크기 조정을 위해 Modifier 사용
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            MyHorizontalDivider()

            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun MainBottomBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
    color: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
) {
    val routes = remember { tabs.map {  { it.route} }}
    val currentSection = tabs.first { it.route == currentRoute }

    Surface(
        modifier = Modifier,
        shape = RectangleShape,
        color = color,
        contentColor = contentColor
    ) {
        val springSpec = SpringSpec<Float>(
            // Determined experimentally
            stiffness = 800f,
            dampingRatio = 0.8f
        )

        HomeBottomNavLayout(
            selectedIndex = currentSection.ordinal,
            itemCount = routes.size,
            animSpec = springSpec,
            indicator = { HomeBottomNavIndicator() },
            modifier = Modifier.navigationBarsPadding()
        ) {
            val configuration = LocalConfiguration.current
            val currentLocale: Locale =
                ConfigurationCompat.getLocales(configuration).get(0) ?: Locale.getDefault()

            tabs.forEach {  section ->
                val selected = section == currentSection
                val tint by animateColorAsState(
                    if (selected) {
                        Color.White
                    } else {
                        Color.Gray
                    }, label = ""
                )

                val text = stringResource(id = section.title).uppercase(currentLocale)
                HomeBottomNavigationItem(
                    icon = {
                           Icon(
                               imageVector = section.icon,
                               tint = tint,
                               contentDescription = text
                           )
                    },
                    text = {
                        Text(
                            text = text,
                            color = tint,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1
                        )
                    },
                    selected = selected,
                    onSelected = { navigateToRoute(section.route) },
                    animSpec = springSpec,
                    modifier = BottomNavigationItemPadding
                        .clip(BottomNavIndicatorShape)
                )

            }
        }
    }
}

@Composable
fun HomeBottomNavLayout(
    selectedIndex: Int,
    itemCount: Int,
    animSpec: AnimationSpec<Float>,
    indicator: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val selectionFractions = remember(itemCount) {
        List(itemCount) { i ->
            Animatable(if (i == selectedIndex) 1f else 0f)
        }
    }

    selectionFractions.forEachIndexed { index, selectionFraction ->
        val target = if (index == selectedIndex) 1f else 0f
        LaunchedEffect(target, animSpec) {
            selectionFraction.animateTo(target, animSpec)
        }
    }

    val indicatorIndex = remember { Animatable(0f) }
    val targetIndicatorIndex = selectedIndex.toFloat()
    LaunchedEffect(targetIndicatorIndex) {
        indicatorIndex.animateTo(targetIndicatorIndex, animSpec)
    }

    Layout(
        modifier = modifier.height(BottomNavHeight),
        content = {
            content()
            Box(Modifier.layoutId("indicator"), content = indicator)
        }
    ) { measurables, constraints ->
        check(itemCount == (measurables.size -1))

        val unselectedWidth = constraints.maxWidth / (itemCount + 1)
        val selectedWidth = 2* unselectedWidth
        val indicatorMeasurable = measurables.first { it.layoutId == "indicator" }

        val itemPlaceables = measurables
            .filterNot { it == indicatorMeasurable }
            .mapIndexed { index, measurable ->
                val width = lerp(unselectedWidth, selectedWidth, selectionFractions[index].value)
                    measurable.measure(
                        constraints.copy(
                            minWidth = width,
                            maxWidth = width
                    )
                )
            }
        val indicatorPlaceable = indicatorMeasurable.measure(
            constraints.copy(
                minWidth = selectedWidth,
                maxWidth = selectedWidth
            )
        )

        layout(
            width = constraints.maxWidth,
            height = itemPlaceables.maxByOrNull { it.height }?.height ?: 0
        ) {
            val indicatorLeft = indicatorIndex.value * unselectedWidth
            indicatorPlaceable.placeRelative(x = indicatorLeft.toInt(), y = 0)
            var x = 0
            itemPlaceables.forEach { placeable ->
                placeable.placeRelative(x = x, y = 0)
                x += placeable.width
            }
        }
    }
}

@Composable
fun HomeBottomNavigationItem(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    selected: Boolean,
    onSelected: () -> Unit,
    animSpec: AnimationSpec<Float>,
    modifier: Modifier = Modifier
) {
    // Animate the icon/text positions within the item based on selection
    val animationProgress by animateFloatAsState(if (selected) 1f else 0f, animSpec)
    HomeBottomNavItemLayout(
        icon = icon,
        text = text,
        animationProgress = animationProgress,
        modifier = modifier
            .selectable(selected = selected, onClick = onSelected)
            .wrapContentSize()
    )
}


@Composable
private fun HomeBottomNavItemLayout(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float,
    modifier: Modifier = Modifier
) {
    Layout(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .layoutId("icon")
                    .padding(horizontal = TextIconSpacing),
                content = icon
            )
            val scale = lerp(0.6f, 1f, animationProgress)
            Box(
                modifier = Modifier
                    .layoutId("text")
                    .padding(horizontal = TextIconSpacing)
                    .graphicsLayer {
                        alpha = animationProgress
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = BottomNavLabelTransformOrigin
                    },
                content = text
            )
        }
    ) { measurables, constraints ->
        val iconPlaceable = measurables.first { it.layoutId == "icon" }.measure(constraints)
        val textPlaceable = measurables.first { it.layoutId == "text" }.measure(constraints)

        placeTextAndIcon(
            textPlaceable,
            iconPlaceable,
            constraints.maxWidth,
            constraints.maxHeight,
            animationProgress
        )
    }
}

private fun MeasureScope.placeTextAndIcon(
    textPlaceable: Placeable,
    iconPlaceable: Placeable,
    width: Int,
    height: Int,
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float
): MeasureResult {
    val iconY = (height - iconPlaceable.height) / 2
    val textY = (height - textPlaceable.height) / 2

    val textWidth = textPlaceable.width * animationProgress
    val iconX = (width - textWidth - iconPlaceable.width) / 2
    val textX = iconX + iconPlaceable.width

    return layout(width, height) {
        iconPlaceable.placeRelative(iconX.toInt(), iconY)
        if (animationProgress != 0f) {
            textPlaceable.placeRelative(textX.toInt(), textY)
        }
    }
}

@Composable
private fun HomeBottomNavIndicator(
    strokeWidth: Dp = 2.dp,
    color: Color = Color.White,
    shape: Shape = BottomNavIndicatorShape
) {
    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .then(BottomNavigationItemPadding)
            .border(strokeWidth, color, shape)
    )
}

private val TextIconSpacing = 2.dp
private val BottomNavHeight = 56.dp
private val BottomNavLabelTransformOrigin = TransformOrigin(0f, 0.5f)
private val BottomNavIndicatorShape = RoundedCornerShape(percent = 50)
private val BottomNavigationItemPadding = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
