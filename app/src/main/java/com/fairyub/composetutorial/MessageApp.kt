package com.fairyub.composetutorial

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fairyub.composetutorial.ui.ConversationScreen
import com.fairyub.composetutorial.ui.SettingScreen

enum class MessageScreen() {
    Conversation,
    Setting
}

@Composable
fun MessageApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MessageScreen.Conversation.name
    ) {
        composable(route = MessageScreen.Conversation.name) {
            ConversationScreen(
                onSettingButtonClicked = {
                    navController.navigate(MessageScreen.Setting.name) {
                        popUpTo(MessageScreen.Conversation.name) {
                            inclusive = false
                        }
                    }
                }
            )
        }
        composable(route = MessageScreen.Setting.name) {
            SettingScreen(
                onBackButtonClicked = {
                    navController.navigate(MessageScreen.Conversation.name) {
                        popUpTo(MessageScreen.Conversation.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}