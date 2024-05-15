package com.sanstech.matchresults.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sanstech.matchresults.data.Match

object SharedPreferencesHelper {

    private const val PREF_NAME = "my_pref"
    private const val KEY_MATCH_LIST = "favorite_matches"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun addMatch(context: Context, match: Match) {
        val matchList = getMatchList(context).toMutableList()
        matchList.add(match)
        saveMatchList(context, matchList)
    }

    fun removeMatch(context: Context, matchId: Long) {
        val matchList = getMatchList(context).toMutableList()
        val iterator = matchList.iterator()
        while (iterator.hasNext()) {
            val match = iterator.next()
            if (match.i == matchId) {
                iterator.remove()
                break
            }
        }
        saveMatchList(context, matchList)
    }

    private fun saveMatchList(context: Context, matchList: List<Match>) {
        val editor = getSharedPreferences(context).edit()
        val gson = Gson()
        val json = gson.toJson(matchList)
        editor.putString(KEY_MATCH_LIST, json)
        editor.apply()
    }

    fun getMatchList(context: Context): List<Match> {
        val gson = Gson()
        val json = getSharedPreferences(context).getString(KEY_MATCH_LIST, "")
        val type = object : TypeToken<List<Match>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
