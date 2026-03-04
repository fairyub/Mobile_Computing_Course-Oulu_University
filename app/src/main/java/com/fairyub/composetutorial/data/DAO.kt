package com.fairyub.composetutorial.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversation")
    fun getAll(): List<Conversation>

    @Query("""
        SELECT 
            c.uid AS conversationId,
            c.title AS title,
            c.avatar AS avatarRes,
            c.created_at AS createdAt,
            m.text AS lastMessageText,
            m.timestamp AS lastMessageTime
        FROM conversation c
        LEFT JOIN message m
        ON m.uid = (
            SELECT uid FROM message
            WHERE conversation_id = c.uid
            ORDER BY timestamp DESC, uid DESC
            LIMIT 1
        )
        ORDER BY COALESCE(lastMessageTime, c.created_at) DESC
    """)
    fun getAllWithLastMessage(): List<ConversationWithLastMessage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg conversations: Conversation)
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM message WHERE conversation_id = :conversationId ORDER BY timestamp ASC")
    fun getAll(conversationId: Int): List<Message>

    @Insert
    fun insertAll(vararg messages: Message)

    @Insert
    fun insert(message: Message) : Long

    @Delete
    fun delete(message: Message)
}

@Dao
interface AttachmentDao {
    @Query("SELECT * FROM attachment WHERE message_id = :messageId")
    fun getAll(messageId: Int): List<Attachment>

    @Insert
    fun insertAll(vararg attachments: Attachment)

    @Insert
    fun insert(attachment: Attachment)

    @Delete
    fun delete(attachment: Attachment)
}