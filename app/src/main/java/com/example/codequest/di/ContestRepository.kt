package com.example.codequest.di

import com.example.codequest.data.ContestResponse
import com.example.codequest.domain.ContestApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContestRepository @Inject constructor(
    private val contestApiService: ContestApiService
) {
    suspend fun getContests(username: String, apiKey: String): ContestResponse {
        return contestApiService.getContests(username, apiKey)
    }


}
