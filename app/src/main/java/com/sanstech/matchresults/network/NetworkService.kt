package com.sanstech.matchresults.network

import com.sanstech.matchresults.data.ApiResponse
import retrofit2.http.GET
// NetworkService.kt
interface MatchService {
    @GET("statistics/sport/SOCCER/matches")
    suspend fun getMatches(): ApiResponse
}