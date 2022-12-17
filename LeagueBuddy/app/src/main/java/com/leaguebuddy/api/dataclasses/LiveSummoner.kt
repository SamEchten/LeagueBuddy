package com.leaguebuddy.api.dataclasses

data class LiveSummoner(
    val summonerName: String,
    val summonerId: String,
    val spells: List<LiveSummonerSpell>,
    val championId: Int,
    val teamId: Int,
    val rank: String
)
