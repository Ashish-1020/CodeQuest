package com.example.codequest.domain

import com.example.codequest.data.ContestResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ContestApiService {
    @GET("contest/?order_by=-start&limit=300&offset=0")
    suspend fun getContests(
        @Query("username") username: String,
        @Query("api_key") apiKey: String
    ): ContestResponse


}
