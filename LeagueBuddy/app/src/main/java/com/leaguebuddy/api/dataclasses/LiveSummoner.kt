package com.leaguebuddy.api.dataclasses

data class LiveSummoner(
    val summonerName : String,
    val summonerId : String,
    val spell1 : String,
    val spell2 : String,
    val championId : Int,
    val teamId  : Int,
    val rank : String
)
