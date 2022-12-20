package com.leaguebuddy.api.dataclasses

data class LiveSummonerSpell(
    val name : String,
    val id : Int,
    val duration : Int,
    val description : String
)
