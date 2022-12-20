package com.leaguebuddy.api.dataclasses

data class LiveMatch(
    val gameId : Long,
    val gameMode : String,
    val participants : List<LiveSummoner>

)
