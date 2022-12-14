package com.leaguebuddy.api

import com.leaguebuddy.api.dataclasses.Summoner
import com.leaguebuddy.api.dataclasses.LiveMatch
import com.leaguebuddy.api.dataclasses.LiveSummoner
import com.leaguebuddy.exceptions_v2.*
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

class LeagueApiHelper {
    private var client : OkHttpClient = OkHttpClient()
    private var apiKey : String = "RGAPI-9eebee6a-46c6-482e-ac9e-3232f811bfe3"// Get the api key and decrypt it so we can receive the information

    fun getSummonerInfo(summonerName: String) : Summoner {
        if(summonerName.length < 16){
            val url = HttpUrl.Builder()
                .scheme("https")
                .host("euw1.api.riotgames.com")
                .addPathSegment("lol")
                .addPathSegment("summoner")
                .addPathSegment("v4")
                .addPathSegment("summoners")
                .addPathSegment("by-name")
                .addPathSegment(summonerName)
                .addQueryParameter("api_key", apiKey)
                .build()
            val request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()
            if(response.isSuccessful) {
                val result = response.body()?.string()
                if (result != null) {
                    if(result.isNotEmpty()) {
                        return createSummoner(result)
                    }else {
                        throw CouldNotFetchSummonerException("Could not fetch Summoner")
                    }
                }else {
                    throw CouldNotFetchSummonerException("Could not fetch Summoner")
                }
            }else {
                throw IncorrectResponseCodeException("Response returned 400 - 500 status code",  response.code())
            }
        }else {
            throw SummonerNameInvalidException("Summoner name must be smaller than 16 characters")
        }

    }

    fun getLiveMatch(summonerId: String) : LiveMatch {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("euw1.api.riotgames.com")
            .addPathSegment("lol")
            .addPathSegment("spectator")
            .addPathSegment("v4")
            .addPathSegment("active-games")
            .addPathSegment("by-summoner")
            .addPathSegment(summonerId)
            .addQueryParameter("api_key", apiKey)
            .build()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if(response.isSuccessful) {
            val result = response.body()?.string()
            if (result != null) {
                if(result.isNotEmpty()) {
                    return createLiveMatch(result)
                }else {
                    throw EmptyResponseException("No live data found but summonerId is correct")
                }
            }else {
                throw EmptyResponseException("No live data found but summonerId is correct")
            }
        }else {
            throw SummonerNotInGameException("Summoner is not in a game currently")
        }
    }

    private fun createLiveMatch(result : String) : LiveMatch {
        val jsonObject = JSONTokener(result).nextValue() as JSONObject
        val participantsArray = jsonObject.get("participants") as JSONArray

        return LiveMatch(
            jsonObject.get("gameId") as Long,
            jsonObject.get("gameMode").toString(),
            createLiveSummoners(participantsArray)
        )
    }

    private fun createLiveSummoners(summoners : JSONArray) : List<LiveSummoner>{
        val liveSummonerList : MutableList<LiveSummoner> = mutableListOf<LiveSummoner>()
        for (i in 0 until summoners.length()){
            val summoner =  summoners.getJSONObject(i)
            val liveSummoner = LiveSummoner(
                summoner.get("summonerName").toString(),
                summoner.get("summonerId").toString(),
                summoner.get("spell1Id").toString(),
                summoner.get("spell2Id").toString(),
                summoner.get("championId") as Int,
                summoner.get("teamId") as Int,
                "Diamond"
            )
            liveSummonerList.add(liveSummoner)
        }
        return liveSummonerList
    }

    private fun createSummoner(result: String) : Summoner {
        val jsonObject = JSONTokener(result).nextValue() as JSONObject

        return Summoner(
            jsonObject.getString("id"),
            jsonObject.getString("accountId"),
            jsonObject.getString("puuid"),
            jsonObject.getString("name"),
            jsonObject.getString("profileIconId").toInt(),
            jsonObject.getString("summonerLevel").toInt()
        )

    }
}