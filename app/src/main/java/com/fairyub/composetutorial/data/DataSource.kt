package com.fairyub.composetutorial.data

import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.fairyub.composetutorial.R

object DataSource {
    val users = listOf(
        User(1, "Alice",  R.drawable.profile_picture.toString()), //Owner
        User(2, "Bob",  R.drawable.ball.toString()),
        User(3, "Charlie",  R.drawable.cat.toString()),
        User(4, "David",  R.drawable.cat1.toString()),
        User(5, "Eve",  R.drawable.cat2.toString()),
        User(6, "Frank",  R.drawable.dog.toString()),
        User(7, "Grace",  R.drawable.dog2.toString()),
    )

    val conversations = listOf(
        Conversation(1, "Family Group", R.drawable.family_group ,1623937203000L),
        Conversation(2, "Best Friend Group", R.drawable.friends_group, 1623937203000L),
        Conversation(3, "Bob", R.drawable.ball, 1623937203000L),
        Conversation(4, "Charlie",R.drawable.cat, 1623937203000L),
        Conversation(5, "David",R.drawable.cat1, 1623937203000L),
        Conversation(6, "Eve", R.drawable.cat2, 1623937203000L),
        Conversation(7, "Frank", R.drawable.dog, 1623937203000L),
        Conversation(8, "Grace", R.drawable.dog2, 1623937203000L),
    )

    val messages = listOf(
        Message(1, 1, 1, "Good morning everyone!", 1623937203000L),
        Message(2, 1, 2, "Hi Alice!", 1623937204000L),
        Message(3, 1, 3, "Have a nice day", 1623937203000L),
        Message(4, 1, 4, "Would anyone like to go on a picnic this Sunday?", 1623937205000L),
        Message(16, 1, 1, "Good morning everyone!", 1623937217000L),
        Message(17, 1, 2, "Hi Alice!", 1623937218000L),
        Message(18, 1, 3, "Have a nice day", 1623937219000L),
        Message(19, 1, 4, "Would anyone like to go on a picnic this Sunday?", 162393722000L),
        Message(20, 1, userId = 5, text = """List of Android versions:
            |Android KitKat (API 19)
            |Android Lollipop (API 21)
            |Android Marshmallow (API 23)
            |Android Nougat (API 24)
            |Android Oreo (API 26)
            |Android Pie (API 28)
            |Android 10 (API 29)
            |Android 11 (API 30)
            |Android 12 (API 31)""".trim(), 1623937223000L),
        Message(21, 1, userId = 5, text = """List of Android versions:
            |Android KitKat (API 19)
            |Android Lollipop (API 21)
            |Android Marshmallow (API 23)
            |Android Nougat (API 24)
            |Android Oreo (API 26)
            |Android Pie (API 28)
            |Android 10 (API 29)
            |Android 11 (API 30)
            |Android 12 (API 31)""".trim(), 1623937224000L),
        Message(22, 1, userId = 5, text = """List of Android versions:
            |Android KitKat (API 19)
            |Android Lollipop (API 21)
            |Android Marshmallow (API 23)
            |Android Nougat (API 24)
            |Android Oreo (API 26)
            |Android Pie (API 28)
            |Android 10 (API 29)
            |Android 11 (API 30)
            |Android 12 (API 31)""".trim(), 1623937225000L),
        Message(23, 1, userId = 5, text = """List of Android versions:
            |Android KitKat (API 19)
            |Android Lollipop (API 21)
            |Android Marshmallow (API 23)
            |Android Nougat (API 24)
            |Android Oreo (API 26)
            |Android Pie (API 28)
            |Android 10 (API 29)
            |Android 11 (API 30)
            |Android 12 (API 31)""".trim(), 1623937226000L),
        Message(5, 2, 5, "How are you doing?", 1623937206000L),
        Message(6, 2, 6, "I'm doing well, thanks!", 1623937207000L),
        Message(7, 2, 7, "Alice, what's up?", 1623937208000L),
        Message(8, 2, 1, """I'm doing well, thanks! 
            |I'm planning to play badminton on Friday night, would anyone like to join?""".trim(), 1623937209000L),
        Message(9, 3, 2, "Alice, what's up?", 1623937210000L),
        Message(10, 3, 1, "Test...Test...Test...", 1623937211000L),
        Message(11, 4, userId = 3, text = "Searching for alternatives to XML layouts...", 1623937212000L),
        Message(12, 5, userId = 4, text = "It's available from API 21+ :)", 1623937213000L),
        Message(13, 6, userId = 5, text = """List of Android versions:
            |Android KitKat (API 19)
            |Android Lollipop (API 21)
            |Android Marshmallow (API 23)
            |Android Nougat (API 24)
            |Android Oreo (API 26)
            |Android Pie (API 28)
            |Android 10 (API 29)
            |Android 11 (API 30)
            |Android 12 (API 31)""".trim(), 1623937214000L),
        Message(14, 7, userId = 6, text = "Searching for alternatives to XML layouts...", 1623937215000L),
        Message(15, 8, userId = 7, text = "It's available from API 21+ :)", 1623937216000L),
    )

    val attachments = listOf(
        Attachment(1, 1,null, R.drawable.cat),
        Attachment(2, 2,null, R.drawable.dog),
        Attachment(3, 3,null, R.drawable.cat1)
    )
}
