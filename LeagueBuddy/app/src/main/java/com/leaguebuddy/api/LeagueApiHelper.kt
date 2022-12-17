package com.leaguebuddy.api

import com.leaguebuddy.api.dataclasses.Summoner
import com.leaguebuddy.api.dataclasses.LiveMatch
import com.leaguebuddy.api.dataclasses.LiveSummoner
import com.leaguebuddy.api.dataclasses.LiveSummonerSpell
import com.leaguebuddy.exceptions_v2.*
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

class LeagueApiHelper {
    private var client : OkHttpClient = OkHttpClient()
    private var apiKey : String = "RGAPI-443bc79c-150e-42c7-b502-1ad90baa983e"// Get the api key and decrypt it so we can receive the information

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

    fun getSummonerSpellsById(spellId: Int, secondSpellId2 : Int) : List<LiveSummonerSpell> {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("ddragon.leagueoflegends.com")
            .addPathSegment("cdn")
            .addPathSegment("12.23.1")
            .addPathSegment("data")
            .addPathSegment("en_US")
            .addPathSegment("summoner.json")
            .build()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if(response.isSuccessful) {
            val result = response.body()?.string()
            if (result != null) {
                if(result.isNotEmpty()) {
                    return createLiveSummonerSpell(result, spellId, secondSpellId2)
                }else {
                    throw CouldNotFetchSummonerSpellException("Could not fetch Summoner spell")
                }
            }else {
                throw CouldNotFetchSummonerSpellException("Could not fetch Summoner spell")
            }
        }else {
            throw IncorrectResponseCodeException("Response returned 400 - 500 status code",  response.code())
        }
    }

    fun getChampionInfo(summonerName: String) : Summoner {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("ddragon.leagueoflegends.com")
            .addPathSegment("cdn")
            .addPathSegment("12.23.1")
            .addPathSegment("data")
            .addPathSegment("en_US")
            .addPathSegment("champion.json")
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
    }

    private fun createLiveMatch(result : String) : LiveMatch {
        val jsonObject = JSONTokener(result).nextValue() as JSONObject
        val participantsArray = jsonObject.get("participants") as JSONArray

        return LiveMatch(
            jsonObject.get("gameId") as Long,
            jsonObject.get("gameMode") as String,
            createLiveSummoners(participantsArray)
        )
    }

    private fun createLiveSummoners(summoners : JSONArray) : List<LiveSummoner>{
        val liveSummonerList : MutableList<LiveSummoner> = mutableListOf()
        var spells : List<LiveSummonerSpell> = listOf()
        for (i in 0 until summoners.length()){
            val summoner =  summoners.getJSONObject(i)
            val spellApi = GlobalScope.async {
                val spellList = async {
                    spells = getSummonerSpellsById(summoner.get("spell1Id") as Int, summoner.get("spell2Id") as Int)
                }
            }

            GlobalScope.launch { Dispatchers.Main
                spellApi.await()

                val liveSummoner = LiveSummoner(
                    summoner.get("summonerName") as String,
                    summoner.get("summonerId") as String,
                    spells,
                    summoner.get("championId") as Int,
                    summoner.get("teamId") as Int,
                    "Diamond"
                )
                liveSummonerList.add(liveSummoner)
            }
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
            jsonObject.getString("profileIconId")as Int,
            jsonObject.getString("summonerLevel")as Int
        )
    }

    private fun createLiveSummonerSpell(result: String, spellId: Int, secondSpellId2: Int) : List<LiveSummonerSpell> {
        val jsonObject = JSONTokener(result).nextValue() as JSONObject
        val data = jsonObject.get("data") as JSONObject
        val test = data.toJSONArray(data.names())

        val spellList : MutableList<LiveSummonerSpell> = mutableListOf()

        for (i in 0 until test.length()){
            val spell = test.get(i) as JSONObject
            val key = spell.get("key").toString().toInt()
            val name = spell.get("name").toString()
            val duration  = spell.get("cooldown") as JSONArray
            val description = spell.get("description") as String

            if(key == spellId){
                spellList.add(LiveSummonerSpell(
                    name,
                    key,
                    duration[0] as Int,
                    description
                ))
            }
            if(key == secondSpellId2){
                spellList.add(LiveSummonerSpell(
                    name,
                    key,
                    duration[0] as Int,
                    description
                ))
            }
        }
        return spellList
    }
}