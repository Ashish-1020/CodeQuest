package com.example.codequest.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.codequest.room.dao.ContestReminderDao
import com.example.codequest.room.data.ContestReminder

@Database(entities = [ContestReminder::class], version = 2, exportSchema = false)
abstract class ContestDatabase : RoomDatabase() {
    abstract fun contestReminderDao(): ContestReminderDao
}

