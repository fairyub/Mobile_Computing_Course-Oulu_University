package com.fairyub.composetutorial.di

import android.content.Context
import androidx.room.Room
import com.fairyub.composetutorial.data.AppDatabase
import com.fairyub.composetutorial.data.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "users.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserDao(userDatabase: AppDatabase): UserDao =
        userDatabase.userDao()
}