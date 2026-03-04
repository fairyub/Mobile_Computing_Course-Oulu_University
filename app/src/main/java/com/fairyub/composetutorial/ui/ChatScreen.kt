package com.fairyub.composetutorial.ui

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.fairyub.composetutorial.data.Attachment
import com.fairyub.composetutorial.data.Message
import com.fairyub.composetutorial.data.User
import com.fairyub.composetutorial.ui.component.LocationService
import com.fairyub.composetutorial.ui.component.ScreenTopBar
import com.fairyub.composetutorial.ui.theme.ComposeTutorialTheme
import java.io.File
import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun ChatScreen(onBackButtonClicked: () -> Unit, conversationId: Int?) {
    val conversationViewModel = hiltViewModel<ConversationViewModel>()
    val currentConversation by conversationViewModel.currentConversation.collectAsState()


    LaunchedEffect(Unit) {
        if(conversationId != null) {
            conversationViewModel.currentConversationFromDB(conversationId)
        }
    }

    Scaffold(
        topBar = {
            ScreenTopBar(
                onBackButtonClicked = onBackButtonClicked,
                title = currentConversation?.title ?: ""
            )
        }
    ) { paddingValues ->
        ChatContent(
            conversationId = conversationId,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun ChatContent(conversationId: Int?, modifier: Modifier = Modifier,) {
//    Log.d("conversationId", "conversationId = ${conversationId}")
    val messagesViewModel = hiltViewModel<MessageViewModel>()
    val messages by messagesViewModel.messages.collectAsState()

    val usersViewModel = hiltViewModel<UserViewModel>()
    val users by usersViewModel.users.collectAsState()

    val attachmentViewModel = hiltViewModel<AttachmentViewModel>()
    val attachments by attachmentViewModel.attachments.collectAsState()

    LaunchedEffect(conversationId) {
        if(conversationId != null) {
            messagesViewModel.messageBelongConversationFromDB(conversationId)
            usersViewModel.userFromDB()
        }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, attachments.size) {
        val lastIndex = messages.lastIndex
        if (lastIndex >= 0) {
            listState.animateScrollToItem(lastIndex)
        }
    }

    Scaffold(
        bottomBar = {
            if(conversationId != null) MessageInputField(modifier, conversationId)
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(messages) { message ->
                val user = users.find { it?.uid == message?.userId }
                if(message != null && user != null) MessageCard(modifier, message, user)
            }
        }
    }
}

@Composable
fun MessageCard(modifier: Modifier = Modifier, message: Message, user: User) {
    val attachmentViewModel = hiltViewModel<AttachmentViewModel>()
    val map by attachmentViewModel.attachments.collectAsState()
    LaunchedEffect(message.uid) {
        attachmentViewModel.attachmentBelongMessageFromDB(message.uid)
    }

    val attachments = map[message.uid].orEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        horizontalArrangement = if (message.userId == 1)
            Arrangement.End
        else
            Arrangement.Start
    ) {
        val avatar = if (message.userId == 1) -1 else user.imageUri?.toInt()
        if (avatar != -1) {
            Image(
                painter = rememberAsyncImagePainter(avatar),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        val surfaceColor by animateColorAsState(
            if (user.uid == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )

        Column(
            horizontalAlignment = if (message.userId == 1) Alignment.End else Alignment.Start
        ) {
            if (!message.text.isNullOrBlank()) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .animateContentSize()
                        .padding(1.dp),
                    color = surfaceColor
                ) {
                    Text(
                        text = message.text,
                        modifier = Modifier.padding(all = 4.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
            attachments.forEach { attachment ->
                val image_url = attachment.imageRes ?: attachment.uri
                Image(
                    painter = rememberAsyncImagePainter(image_url),
                    contentDescription = "Image attachment",
                    modifier = Modifier.size(150.dp)
                )
            }
        }
    }
}

@Composable
fun MessageInputField(modifier: Modifier = Modifier, conversationId: Int) {
    var messageInput by remember { mutableStateOf<String>("") }
    val messageViewModel = hiltViewModel<MessageViewModel>()
    val attachmentViewModel = hiltViewModel<AttachmentViewModel>()

    var savedImageUris by remember { mutableStateOf<List<String>>(emptyList()) }

    val context = LocalContext.current
    val applicationContext = context.applicationContext
    val resolver = applicationContext.contentResolver

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val paths = uris.mapNotNull { uri ->
            resolver.openInputStream(uri)?.use { stream ->
                val file = File(context.filesDir, "${System.currentTimeMillis()}.jpeg")
                file.outputStream().use { out -> stream.copyTo(out) }
                file.absolutePath
            }
        }
        savedImageUris = paths
    }

    fun saveMessage() {
        messageViewModel.sendMessage(
            conversationId = conversationId,
            userId = 1,
            text = messageInput,
            attachmentUris = savedImageUris
        )
        messageInput = ""
        savedImageUris = emptyList()
    }

    fun handleClickedSendButton() {
        saveMessage()
        messageInput = ""
    }

    val locationService = remember { LocationService(context) }

    var hasFineLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { hasPermission ->
        hasFineLocationPermission = hasPermission
    }

    fun handleSendingLocation() {
        locationService.getLastLocation { latitude, longitude ->
            val messageText = "Latitude: $latitude, Longitude: $longitude"
            messageViewModel.sendMessage(
                conversationId = conversationId,
                userId = 1,
                text = messageText,
                attachmentUris = emptyList()
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Row() {
            Button(
                onClick = {launcher.launch("image/*")}
            ) {
                Text(text = "Choose Image")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if(hasFineLocationPermission) {
                        handleSendingLocation()
                    } else {
                        locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if(hasFineLocationPermission) MaterialTheme.colorScheme.primary else Color.Gray
                )

            ) {
                Text(
                    text = "Send Location",
                )
            }
        }
        Row() {
            TextField(
                value = messageInput,
                onValueChange = {
                    messageInput = it
                },
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { handleClickedSendButton()},
            ) {
                Text(text = "Send")
            }
        }
    }
}

@Preview
@Composable
fun PreviewChatScreen() {
    ComposeTutorialTheme {
        ChatScreen(
            onBackButtonClicked = {},
            conversationId = 1
        )
    }
}