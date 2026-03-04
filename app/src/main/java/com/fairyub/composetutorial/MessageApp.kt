package com.fairyub.composetutorial

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fairyub.composetutorial.ui.ChatScreen
import com.fairyub.composetutorial.ui.ConversationsScreen
import com.fairyub.composetutorial.ui.SettingScreen

enum class MessageScreen() {
    Conversations,
    Setting,
    Chat,
}

@Composable
fun MessageApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MessageScreen.Conversations.name
    ) {
        composable(route = MessageScreen.Conversations.name) {
            ConversationsScreen(
                onSettingButtonClicked = {
                    navController.navigate(MessageScreen.Setting.name) {
                        popUpTo(MessageScreen.Conversations.name) {
                            inclusive = false
                        }
                    }
                },
                onConversationClicked = { conversationId ->
                    navController.navigate("${MessageScreen.Chat.name}/$conversationId") {
                        Log.d("conversationId", "conversationId = ${conversationId}")
                        popUpTo(MessageScreen.Conversations.name) {
                            inclusive = false
                        }
                    }
                }
            )
        }
        composable(route = MessageScreen.Chat.name + "/{conversationId}", arguments = listOf(
            navArgument("conversationId") {
                type = NavType.IntType
            })) {
            ChatScreen(
                onBackButtonClicked = {
                    navController.navigate(MessageScreen.Conversations.name) {
                        popUpTo(MessageScreen.Conversations.name) {
                            inclusive = true
                        }
                    }
                },
                conversationId = it.arguments?.getInt("conversationId")
            )
        }
        composable(route = MessageScreen.Setting.name) {
            SettingScreen(
                onBackButtonClicked = {
                    navController.navigate(MessageScreen.Conversations.name) {
                        popUpTo(MessageScreen.Conversations.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}