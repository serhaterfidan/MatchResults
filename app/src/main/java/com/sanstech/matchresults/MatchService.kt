package com.sanstech.matchresults

import retrofit2.http.GET
// MatchService.kt
interface MatchService {
    @GET("statistics/sport/SOCCER/matches")
    suspend fun getMatches(): ApiResponse
}