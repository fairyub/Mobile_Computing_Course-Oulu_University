package com.fairyub.composetutorial.ui

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.rememberAsyncImagePainter
import com.fairyub.composetutorial.data.DataSource
import com.fairyub.composetutorial.data.Message
import com.fairyub.composetutorial.data.User
import com.fairyub.composetutorial.ui.component.createNotification
import com.fairyub.composetutorial.ui.component.createNotificationChannel
import com.fairyub.composetutorial.ui.theme.ComposeTutorialTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopConversationScreenBar(
    onSettingButtonClicked: () -> Unit = {},
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text("") },
        actions = {
            IconButton(onClick = onSettingButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Localized description"
                )
            }
        },
    )
}

@Composable
fun MessageCard(msg: Message, user: User?) {
    // Add padding around our message
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = rememberAsyncImagePainter(user?.imageUri),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        // Add a horizontal space between the image and the column
        Spacer(modifier = Modifier.width(8.dp))

        // We keep track if the message is expanded or not in this variable
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = user?.name ?: msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                )
            }
        }
    }
}

@Composable
fun ConversationScreen(onSettingButtonClicked: () -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        createNotificationChannel(context)
        createNotification(context, "Test", "Automatic Notification")
    }

    Scaffold(
        topBar = {
            TopConversationScreenBar(
                onSettingButtonClicked = onSettingButtonClicked
            )
        }
    ) { paddingValues ->
        ConversationContent(
            messages = DataSource.conversationSample,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun ConversationContent(
    messages: List<Message>,
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<UserViewModel>()
    val users by viewModel.users.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.userFromDB()
    }

    val user = users.firstOrNull()

    LazyColumn(
        modifier = Modifier.padding(top = 100.dp),
    ) {

        items(messages) { message ->
            MessageCard(message, user = user)
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    ComposeTutorialTheme {
        Surface {
            MessageCard(
                msg = Message("Lexi", "Take a look at Jetpack Compose, it's great!"),
                user = null
            )
        }
    }
}

@Preview
@Composable
fun PreviewConversationContent() {
    ComposeTutorialTheme {
        ConversationContent(DataSource.conversationSample)
    }
}

@Preview
@Composable
fun PreviewConversationScreen() {
    ComposeTutorialTheme {
        ConversationScreen(
            onSettingButtonClicked = {}
        )
    }
}

@Preview
@Composable
fun PrivewTopConversationScreenBar() {
    ComposeTutorialTheme {
        TopConversationScreenBar()
    }
}