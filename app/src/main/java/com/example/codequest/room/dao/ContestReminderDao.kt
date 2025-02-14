package com.example.codequest.room.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.codequest.room.data.ContestReminder

@Dao
interface ContestReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContest(contest: ContestReminder)

    @Query("SELECT * FROM contest_reminders ORDER BY  id")
    suspend fun getAllContests(): List<ContestReminder>

    @Query("DELETE FROM contest_reminders WHERE id = :contestId")
    suspend fun deleteContest(contestId: Int)
}
