package com.streamliners.timify.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.streamliners.base.ext.koinBaseViewModel
import com.streamliners.lipu.ui.main.MainActivity
import com.streamliners.lipu.feature.chat.ChatScreen

@Composable
fun MainActivity.NavHostGraph(
    navController: NavHostController
){
    NavHost(
        startDestination = Screen.Chat.route,
        navController = navController
    ){

        composable(Screen.Chat.route){
            ChatScreen(
                navController = navController,
                viewModel = koinBaseViewModel()
            )
        }

        composable(Screen.PieChart.route){

        }

        composable(Screen.SheetSync.route){

        }
    }
}