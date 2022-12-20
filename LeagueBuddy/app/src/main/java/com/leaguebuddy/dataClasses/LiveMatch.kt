package com.leaguebuddy.dataClasses

data class LiveMatch(
    val gameId : Long,
    val gameMode : String,
    val participants : List<LiveSummoner>

)
