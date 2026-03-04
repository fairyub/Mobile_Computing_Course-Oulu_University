package com.fairyub.composetutorial

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fairyub.composetutorial.ui.component.GyroscopeSensorForegroundService
import com.fairyub.composetutorial.ui.theme.ComposeTutorialTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        val intent = Intent(this, GyroscopeSensorForegroundService::class.java)
        ContextCompat.startForegroundService(this, intent)

        setContent {
            ComposeTutorialTheme {
                MessageApp()
            }
        }
    }
}



