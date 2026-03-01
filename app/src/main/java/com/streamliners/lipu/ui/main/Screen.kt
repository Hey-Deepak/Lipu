package com.streamliners.lipu.ui.main

sealed class Screen(
    val route: String
) {
    data object Chat : Screen("Chat")
    data object Projects : Screen("Projects")
    data object ContentHistory : Screen("ContentHistory")
}