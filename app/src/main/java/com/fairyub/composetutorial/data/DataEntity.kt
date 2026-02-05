package com.fairyub.composetutorial.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "user_name") val name: String?,
    @ColumnInfo(name = "image") val imageUri: String?
)