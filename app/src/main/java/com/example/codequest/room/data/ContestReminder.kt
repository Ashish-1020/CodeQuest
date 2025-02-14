package com.example.codequest.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contest_reminders")
data class ContestReminder(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val contestName: String,
    val contestPlatform: String,
    val contestTime: String
)
