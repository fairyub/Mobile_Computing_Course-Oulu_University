package com.fairyub.composetutorial.ui

import android.net.Uri
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
import coil3.compose.AsyncImage
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import android.util.Log
import androidx.compose.runtime.remember
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.content.Context
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.material3.TextField
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import java.io.File
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.fairyub.composetutorial.data.User

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
    modifier: Modifier = Modifier,
) {
    var userName by remember { mutableStateOf<String>("Lexi") }
    var imageUri: Uri? by remember { mutableStateOf<Uri?>(null) }
    var savedImageUri: String? by remember { mutableStateOf<String?>(null) }

    val viewModel = hiltViewModel<UserViewModel>()
    val users by viewModel.users.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.userFromDB()
    }

    LaunchedEffect(users) {
        if (users.isNotEmpty()) {
            val currentUser = users.first()
            userName = currentUser?.name ?: ""
            savedImageUri = currentUser?.imageUri
        }
    }

    fun saveUser() {
        val newUser = User(
            uid = if (users.isNotEmpty() && users.first()?.uid != null) users.first()!!.uid else 1,
            name = userName,
            imageUri = savedImageUri
        )
        viewModel.addUserToDB(newUser)
    }

    val context = LocalContext.current
    val applicationContext = context.applicationContext
    // Open a specific media item using InputStream.
    val resolver = applicationContext.contentResolver
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
        imageUri?.let {
            resolver.openInputStream(it).use { stream ->
                val file = File(context.filesDir, "cat.jpeg")
                file.outputStream().use { output ->
                    stream?.copyTo(output)
                }
                savedImageUri = file.absolutePath
            }
            saveUser()
        }
    }

    val displayUri = savedImageUri?.let {
        File(it).toUri()
    } ?: imageUri



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
            painter = rememberAsyncImagePainter(displayUri),
            contentDescription = "My Image",
            modifier = Modifier
                .clickable {launcher.launch("image/*")}
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = userName,
            onValueChange = {
                userName = it
                saveUser()
            },
        )
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
