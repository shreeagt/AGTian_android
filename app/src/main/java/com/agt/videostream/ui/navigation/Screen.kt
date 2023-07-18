package com.agt.videostream.ui.navigation

sealed class Screen(val route: String) {

    object LoginScreen : Screen("LoginScreen")
    object DashBoardScreen : Screen("DashBoardScreen")
    object VideoScreen : Screen("VideoScreen/{videoId}/{isShortList}") {
        fun withArgs(id: Int,isShortList:Boolean): String {
            return "VideoScreen/$id/$isShortList"
        }
    }

}
