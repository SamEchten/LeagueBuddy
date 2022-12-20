package com.leaguebuddy.api.dataclasses

data class Summoner(
    val id: String,
    val accountId: String,
    val puuid: String,
    val name: String,
    val profileIconId : Int,
    val summonerLevel: Int

)
