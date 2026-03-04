package com.fairyub.composetutorial.ui

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.fairyub.composetutorial.R
import com.fairyub.composetutorial.data.ConversationWithLastMessage
import com.fairyub.composetutorial.ui.component.createNotification
import com.fairyub.composetutorial.ui.component.createNotificationChannel
import com.fairyub.composetutorial.ui.theme.ComposeTutorialTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopConversationsScreenBar(
    onSettingButtonClicked: () -> Unit = {},
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text("Conversations") },
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
fun ConversationCard(conversation: ConversationWithLastMessage, onConversationClicked: (conversationId: Int) -> Unit, conversationId: Int) {
    // Add padding around our message
    Row(modifier = Modifier.padding(all = 8.dp).clickable { onConversationClicked(conversationId) }) {
        Image(
            painter = rememberAsyncImagePainter(conversation.avatarRes),
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
        Column() {
            Text(
                text = conversation.title ?: "",
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
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
                    .clickable { isExpanded = !isExpanded }
            ) {
                Text(
                    text = conversation.lastMessageText ?: "image",
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
        }
    }
}

@Composable
fun ConversationsScreen(onSettingButtonClicked: () -> Unit, onConversationClicked: (conversationId: Int) -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        createNotificationChannel(context)
        createNotification(context, "Test", "Automatic Notification")
    }

    Scaffold(
        topBar = {
            TopConversationsScreenBar(
                onSettingButtonClicked = onSettingButtonClicked
            )
        }
    ) { paddingValues ->
        ConversationContent(
            modifier = Modifier.padding(paddingValues),
            onConversationClicked = onConversationClicked as (conversationId: Int) -> Unit
        )
    }
}

@Composable
fun ConversationContent(
    modifier: Modifier = Modifier,
    onConversationClicked: (conversationId: Int) -> Unit
) {
    val viewModel = hiltViewModel<ConversationViewModel>()
    val conversations by viewModel.conversations.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.conversationsFromDB()
    }

    LazyColumn(
        modifier = Modifier.padding(top = 100.dp),
    ) {

        items(conversations) { conversation ->
            if(conversation != null) ConversationCard(conversation, onConversationClicked, conversation.conversationId)

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
fun PreviewConversationCard() {
    ComposeTutorialTheme {
        Surface {
            ConversationCard(
                conversation = ConversationWithLastMessage(
                    conversationId = 1,
                    title = "Test",
                    avatarRes = R.drawable.cat,
                    createdAt = System.currentTimeMillis(),
                    lastMessageText = "Test",
                    lastMessageTime = System.currentTimeMillis()
                ),
                onConversationClicked = {},
                conversationId = 1
            )
        }
    }
}

@Preview
@Composable
fun PreviewConversationContent() {
    ComposeTutorialTheme {
        ConversationContent(
            onConversationClicked = {}
        )
    }
}

@Preview
@Composable
fun PreviewConversationsScreen() {
    ComposeTutorialTheme {
        ConversationsScreen(
            onSettingButtonClicked = {},
            onConversationClicked = {}
        )
    }
}

@Preview
@Composable
fun PrivewTopConversationsScreenBar() {
    ComposeTutorialTheme {
        TopConversationsScreenBar()
    }
}