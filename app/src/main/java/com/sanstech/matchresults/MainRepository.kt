package com.sanstech.matchresults

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(private val matchService: MatchService) {
    suspend fun getMatchResults() = flow {
        emit(NetworkResult.Loading(true))
        val response = matchService.getMatches()
        emit(NetworkResult.Success(response.data))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }
}