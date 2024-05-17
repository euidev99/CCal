package com.capstone.ccal.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


//잘 모르지만,, remember navController 생성해서 계속 메인 네비게이션이 유지되도록 하는 듯 함
@Composable
fun rememberMainNavController(
    navController: NavHostController = rememberNavController()
): MainNavController = remember(navController) {
    MainNavController(navController)
}

//Google 샘플에서 훔쳐옴 NavController 확장 정의해서 편하게 쓰는 패턴
@Stable
class MainNavController(
    val navController: NavHostController,
) {
    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    //뭐 다른 화면 많겠지만 스킵

    //디테일 화면 이동할때 편하게 쓰려고..
    fun navigateToBookDetail(bookId: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            Log.d("seki", "navigateToBookDetail : $bookId")
            navController.navigate("${MainDestination.DETAIL_BOOK}/$bookId")
        }
    }

//    fun navigateToRoute(route: String, from: NavBackStackEntry) {
//        if (from.lifecycleIsResumed()) {
//            navController.navigate(route)
//        }
//    }

    fun navigateToHome(route: String, isBackStackAvail: Boolean) {
        if (route != currentRoute) {
            if ( !isBackStackAvail ) {
                navController.popBackStack()
            }
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }
}

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)
