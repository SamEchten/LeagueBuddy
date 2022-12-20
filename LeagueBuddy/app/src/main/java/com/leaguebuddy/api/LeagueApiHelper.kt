package com.leaguebuddy.api

import com.leaguebuddy.dataClasses.Summoner
import com.leaguebuddy.dataClasses.LiveMatch
import com.leaguebuddy.dataClasses.LiveSummoner
import com.leaguebuddy.dataClasses.LiveSummonerSpell
import com.leaguebuddy.exceptions.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import ru.gildor.coroutines.okhttp.await

class LeagueApiHelper {
    private var client : OkHttpClient = OkHttpClient()
    //Storing api key is here is temporary, for testing purposes only
    private var apiKey : String = "RGAPI-ea4a775d-6975-4aeb-83a7-d68f49b23417"// Get the api key and decrypt it so we can receive the information TODO

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
                val result = response.body?.string()
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
                throw IncorrectResponseCodeException("Response returned 400 - 500 status code",
                    response.code
                )
            }
        }else {
            throw SummonerNameInvalidException("Summoner name must be smaller than 16 characters")
        }

    }

    suspend fun getLiveMatch(summonerId: String) : LiveMatch {
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
             println("test")

        val response = client.newCall(request).await()

        if(response.isSuccessful) {
            val result = response.body?.string()
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

    suspend fun getSummonerSpellsById(spellId: Int, secondSpellId2 : Int) : List<LiveSummonerSpell> {
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

        val response = client.newCall(request).await()
        if(response.isSuccessful) {
            val result = response.body?.string()
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
            throw IncorrectResponseCodeException("Response returned 400 - 500 status code",
                response.code
            )
        }
    }

    private suspend fun getChampionNameById(championId: Int) : String {
        val url = HttpUrl.Builder()
            .scheme("https")
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
            val result = response.body?.string()
            if (result != null) {
                if(result.isNotEmpty()) {
                    return createChampionNameById(result, championId)
                }else {
                    throw CouldNotFetchSummonerException("Could not fetch Summoner")
                }
            }else {
                throw CouldNotFetchSummonerException("Could not fetch Summoner")
            }
        }else {
            throw IncorrectResponseCodeException("Response returned 400 - 500 status code",
                response.code
            )
        }
    }

    private suspend fun createLiveMatch(result : String) : LiveMatch {
        val jsonObject = JSONTokener(result).nextValue() as JSONObject
        val participantsArray = jsonObject.get("participants") as JSONArray

        return LiveMatch(
            jsonObject.get("gameId") as Long,
            jsonObject.get("gameMode") as String,
            createLiveSummoners(participantsArray)
        )
    }

    private suspend fun createLiveSummoners(summoners : JSONArray) : List<LiveSummoner>{
        val liveSummonerList : MutableList<LiveSummoner> = mutableListOf()
        for (i in 0 until summoners.length()) {
            val summoner =  summoners.getJSONObject(i)

            val spells = getSummonerSpellsById(summoner.get("spell1Id") as Int, summoner.get("spell2Id") as Int)
            val imagePath = getChampionNameById(summoner.get("championId") as Int)

            val liveSummoner = LiveSummoner(
                summoner.get("summonerName") as String,
                summoner.get("summonerId") as String,
                spells,
                imagePath,
                summoner.get("teamId") as Int,
                "Diamond"
            )
            liveSummonerList.add(liveSummoner)

        }
        return liveSummonerList
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
                spellList.add(
                    LiveSummonerSpell(
                    name,
                    key,
                    duration[0] as Int,
                    description
                )
                )
            }
            if(key == secondSpellId2){
                spellList.add(
                    LiveSummonerSpell(
                    name,
                    key,
                    duration[0] as Int,
                    description
                    )
                )
            }
        }
        return spellList
    }

    private suspend fun createChampionNameById(result: String, championId: Int) : String {
        val jsonObject = JSONTokener(result).nextValue() as JSONObject
        val championsObject = jsonObject.get("data") as JSONObject
        val championsArray = championsObject.toJSONArray(championsObject.names())

        for(i in 0 until championsArray.length()){
            val champion = championsArray.get(i) as JSONObject
            val key = champion.get("key").toString().toInt()
            val championName = champion.get("name").toString()
            if(key == championId){
                return championName
            }
        }
        return "Aatrox"
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
}