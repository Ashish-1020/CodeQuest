package com.example.codequest.di

import com.example.codequest.room.dao.ContestReminderDao
import com.example.codequest.room.data.ContestReminder
import javax.inject.Inject

class RoomContestRepository @Inject constructor(private val contestReminderDao: ContestReminderDao) {
    suspend fun insertContest(contest: ContestReminder) {
        contestReminderDao.insertContest(contest)
    }

    suspend fun getAllContests(): List<ContestReminder> {
        return contestReminderDao.getAllContests()
    }

    suspend fun deleteContest(contestId: Int) {
        contestReminderDao.deleteContest(contestId)
    }
}