package com.leaguebuddy.api.dataclasses
import android.graphics.Bitmap

data class LiveSummoner(
    val summonerName: String,
    val summonerId: String,
    val spells: List<LiveSummonerSpell>,
    val championImage: String,
    val teamId: Int,
    val rank: String
)
