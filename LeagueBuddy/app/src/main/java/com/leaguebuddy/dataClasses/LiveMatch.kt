package com.leaguebuddy.dataClasses

import com.leaguebuddy.exceptions.SummonerNameNotFoundException

data class LiveMatch(
    val gameId : Long,
    val gameMode : String,
    val participants : List<LiveSummoner>

){
    fun getTeamCodeBySummonerId(summonerId : String) : Int {
        for(i in participants){
            if(i.summonerId == summonerId){
                return i.teamId
            }
        }
        throw SummonerNameNotFoundException("SummonerId not found in participants list of livematch object")
    }
}
