package com.fairyub.composetutorial.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "user_name") val name: String?,
    @ColumnInfo(name = "image") val imageUri: String?,
)

@Entity()
data class Conversation(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "avatar") val avatarRes: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
)

data class ConversationWithLastMessage(
    val conversationId: Int,
    val title: String?,
    val avatarRes: Int,
    val createdAt: Long,
    val lastMessageText: String?,
    val lastMessageTime: Long?
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Conversation::class,
            parentColumns = ["uid"],
            childColumns = ["conversation_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Message(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "conversation_id") val conversationId: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "text") val text: String?,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis(),
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Message::class,
            parentColumns = ["uid"],
            childColumns = ["message_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Attachment(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "message_id") val messageId: Int,
    @ColumnInfo(name = "uri") val uri: String?,
    @ColumnInfo(name = "image_res") val imageRes: Int?
)