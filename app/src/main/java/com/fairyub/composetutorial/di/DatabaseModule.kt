package com.fairyub.composetutorial.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fairyub.composetutorial.data.AppDatabase
import com.fairyub.composetutorial.data.AttachmentDao
import com.fairyub.composetutorial.data.ConversationDao
import com.fairyub.composetutorial.data.DataSource
import com.fairyub.composetutorial.data.MessageDao
import com.fairyub.composetutorial.data.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        lateinit var db: AppDatabase

        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database.db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(dbSql: SupportSQLiteDatabase) {
                super.onCreate(dbSql)
                CoroutineScope(Dispatchers.IO).launch {
                    db.userDao().insertAll(*DataSource.users.toTypedArray())
                    db.conversationDao().insertAll(*DataSource.conversations.toTypedArray())
                    db.messageDao().insertAll(*DataSource.messages.toTypedArray())
                    db.attachmentDao().insertAll(*DataSource.attachments.toTypedArray())
                }
            }
        }).build()
        return db
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao =
        db.userDao()

    @Provides
    fun provideConversationDao(db: AppDatabase): ConversationDao =
        db.conversationDao()

    @Provides
    fun provideMessageDao(db: AppDatabase): MessageDao =
        db.messageDao()

    @Provides
    fun provideAttachmentDao(db: AppDatabase): AttachmentDao =
        db.attachmentDao()
}