package com.fairyub.composetutorial.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairyub.composetutorial.R
import com.fairyub.composetutorial.ui.theme.ComposeTutorialTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSettingScreenBar(
    onBackButtonClicked: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
    )
}

@Composable
fun SettingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.padding(
            vertical = 100.dp,
            horizontal = 8.dp
        )
    ) {
        Text(
            text = "User:",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.primary,
        ) {
            Text(
                text = "Lexi",
                modifier = Modifier.padding(all = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun SettingScreen(onBackButtonClicked: () -> Unit) {
    Scaffold(
        topBar = {
            TopSettingScreenBar(
                onBackButtonClicked = onBackButtonClicked
            )
        }
    ) { paddingValues ->
        SettingContent(
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Preview
@Composable
fun PreviewSettingContent() {
    ComposeTutorialTheme {
        SettingContent()
    }
}

@Preview
@Composable
fun PreviewSettingScreen() {
    ComposeTutorialTheme {
        SettingScreen(
            onBackButtonClicked = {}
        )
    }
}
