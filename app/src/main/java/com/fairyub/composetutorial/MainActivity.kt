package com.fairyub.composetutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fairyub.composetutorial.ui.theme.ComposeTutorialTheme
import com.fairyub.composetutorial.data.DataSource
import com.fairyub.composetutorial.ui.ConversationScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTutorialTheme {
                MessageApp()
            }
        }
    }
}



