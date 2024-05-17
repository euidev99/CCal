package com.capstone.ccal

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.capstone.ccal.ui.detail.BookDetail
import com.capstone.ccal.ui.home.Feed
import com.capstone.ccal.ui.home.HomeSections
import com.capstone.ccal.ui.home.addHomeGraph
import com.capstone.ccal.ui.login.Login
import com.capstone.ccal.ui.login.Register
import com.capstone.ccal.ui.navigation.MainDestination
import com.capstone.ccal.ui.navigation.rememberMainNavController
import com.capstone.ccal.ui.theme.CCalTheme


@Composable
fun CalCalCalApp() {

    CCalTheme {
        val mainNavController = rememberMainNavController()
        NavHost(
            navController = mainNavController.navController,
//            startDestination = MainDestination.LOGIN
            startDestination = MainDestination.HOME_ROUTE
        ) {
            calMainNavGraph(
                onDetailSelected = mainNavController::navigateToBookDetail,
//                onNavigateToRoute = mainNavController::navigateToRoute,
                onNavigateToHome = mainNavController::navigateToHome,
                onNavigateToBottomBarRoute = mainNavController::navigateToBottomBarRoute,
                upPress = mainNavController::upPress
            )
        }
    }
}

private fun NavGraphBuilder.calMainNavGraph(
    onDetailSelected: (String, NavBackStackEntry) -> Unit,
//    onNavigateToRoute: (String, NavBackStackEntry) -> Unit,
    onNavigateToHome: (String, Boolean) -> Unit, // isBackStackAvailable 옵션
    onNavigateToBottomBarRoute: (String) -> Unit,
    upPress: () -> Unit,
) {
    navigation(
        route = MainDestination.HOME_ROUTE,
        startDestination = MainDestination.LOGIN //HomeSections.FEED.route
    ) {
        //로그인 화면
        composable(MainDestination.LOGIN) { from ->
            Login(onDetailClick = {
                id -> onDetailSelected(id, from) },
                onNavigateToHome
            )
        }

        addHomeGraph(onDetailSelected, onNavigateToBottomBarRoute)
    }

    //회원가입 화면
    composable(MainDestination.REGISTER) { from ->
        Register(onDetailClick = { id -> onDetailSelected(id, from) }, onNavigateToHome)
    }

    composable("${MainDestination.DETAIL_BOOK}/{${MainDestination.DETAIL_BOOK_KEY}}",
        arguments = listOf(navArgument(MainDestination.DETAIL_BOOK_KEY) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val bookId = arguments.getString(MainDestination.DETAIL_BOOK_KEY)
        //화면
        if (bookId != null) {
            BookDetail(bookId, upPress)
        }
    }
}