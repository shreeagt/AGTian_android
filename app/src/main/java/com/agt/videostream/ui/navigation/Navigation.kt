package com.agt.videostream.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.agt.videostream.ui.screen.dashboard.DashBoardScreen
import com.agt.videostream.ui.screen.login.LoginScreen
import com.agt.videostream.ui.screen.login.LoginViewModel
import com.agt.videostream.ui.screen.video.VideoScreen
import dagger.hilt.android.AndroidEntryPoint


@Composable
fun Navigation() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {


        composable(Screen.LoginScreen.route) {
            LoginScreen(navController)
        }

        composable(Screen.VideoScreen.route,
            arguments = listOf(
                navArgument("videoId") {
                    type = NavType.IntType
                },
                navArgument("isShortList"){
                    type = NavType.BoolType
                }

            )
        ) {
            val id = it.arguments!!.getInt("videoId")
            val isShortList = it.arguments!!.getBoolean("isShortList")
            VideoScreen(navController = navController, videoId = id, isShortListed = isShortList)
        }
        composable(Screen.DashBoardScreen.route) {
            DashBoardScreen(navController = navController)
        }


    }

}