package com.example.codequest.domain

import android.content.Context
import androidx.room.Room
import com.example.codequest.di.RoomContestRepository
import com.example.codequest.room.ContestDatabase
import com.example.codequest.room.dao.ContestReminderDao
import com.example.codequest.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideContestApi(retrofit: Retrofit): ContestApiService {
        return retrofit.create(ContestApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ContestDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ContestDatabase::class.java,
            "contest_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideContestDao(database: ContestDatabase): ContestReminderDao {
        return database.contestReminderDao()
    }

    @Provides
    @Singleton
    fun provideRoomContestRepository(dao: ContestReminderDao): RoomContestRepository {
        return RoomContestRepository(dao)
    }
}
