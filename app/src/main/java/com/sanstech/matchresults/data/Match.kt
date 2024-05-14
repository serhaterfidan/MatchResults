package com.sanstech.matchresults.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ApiResponse(
    val success: Boolean,
    val data: List<Match>
)

@Parcelize
data class Match(
    val i: Long,
    val sgi: Long,
    val d: Long,
    val st: String,
    val bri: Long,
    val ht: Team,
    val at: Team,
    val sc: Score,
    val to: Tournament,
    val v: String
) : Parcelable

@Parcelize
data class Team(
    val i: Long,
    val n: String,
    val p: Long,
    val sn: String,
    val rc: Int // Eklenen alan
) : Parcelable

@Parcelize
data class Score(
    val st: Int,
    val abbr: String,
    val ht: MatchResult,
    val at: MatchResult
) : Parcelable

@Parcelize
data class MatchResult(
    val r: Int,
    val c: Int,
    val ht: Int
) : Parcelable

@Parcelize
data class Tournament(
    val i: Long,
    val n: String,
    val sn: String,
    val p: Long,
    val flag: String
) : Parcelable



