package com.leaguebuddy.api

import android.util.Log
import com.leaguebuddy.dataClasses.*
import com.leaguebuddy.exceptions.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import ru.gildor.coroutines.okhttp.await
import java.util.*
import kotlin.math.roundToInt

class LeagueApiHelper {
    private var client : OkHttpClient = OkHttpClient()
    var apikey : String = Keys.lolApiKey()

    /**
     * Gets the summoner information needed to call other function.
     * @param summonerName
     * @return Summoner
     * @sample Summoner
     */
    suspend fun getSummonerInfo(summonerName: String) : Summoner {
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
                .addQueryParameter("api_key", Keys.lolApiKey())
                .build()
            val request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).await()
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

    /**
     * Returns true if a summoner is in a live game at the moment of calling this function
     * @param summonerId
     */
    suspend fun summonerInGame(summonerId : String) : Boolean {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("euw1.api.riotgames.com")
            .addPathSegment("lol")
            .addPathSegment("spectator")
            .addPathSegment("v4")
            .addPathSegment("active-games")
            .addPathSegment("by-summoner")
            .addPathSegment(summonerId)
            .addQueryParameter("api_key", Keys.lolApiKey())
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).await()

        if(response.isSuccessful) {
            return true
        }else {
            throw SummonerNotInGameException("Summoner is not in a game currently")
        }
    }

    /**
     * Checks if a summoner name exists and returns true if this is the case.
     * @param summonerName
     */
    suspend fun summonerNameExists(summonerName : String) : Boolean{
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("euw1.api.riotgames.com")
            .addPathSegment("lol")
            .addPathSegment("summoner")
            .addPathSegment("v4")
            .addPathSegment("summoners")
            .addPathSegment("by-name")
            .addPathSegment(summonerName)
            .addQueryParameter("api_key", Keys.lolApiKey())
            .build()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if(response.isSuccessful) {
            return true
        }else {
            Log.e("TAG",  "Summoner name does not exist, check response message for more info")
            throw SummonerNameNotFoundException("Summoner name invalid")
        }
    }

    /**
     * Returns a LiveMatch dataclass. This dataclass contains all the live match information.
     * @param summonerId
     */
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
            .addQueryParameter("api_key", Keys.lolApiKey())
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

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

    /**
     * Gets the rank from a summoner and returns a Rank dataclass object with all information about the summoner his rank.
     * @param summonerId
     * @return Rank
     */
    private suspend fun getRankBySummonerId(summonerId: String) : Rank? {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("euw1.api.riotgames.com")
            .addPathSegment("lol")
            .addPathSegment("league")
            .addPathSegment("v4")
            .addPathSegment("entries")
            .addPathSegment("by-summoner")
            .addPathSegment(summonerId)
            .addQueryParameter("api_key", Keys.lolApiKey())
            .build()
        val request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).await()
        if(response.isSuccessful) {
            val result = response.body?.string()
            if (result != null) {
                if(result.isNotEmpty()) {
                    return createRankBySummonerId(result)
                }else {
                    throw CouldNotFetchSummonerRankException("Could not fetch summoner rank")
                }
            }else {
                throw CouldNotFetchSummonerRankException("Could not fetch summoner rank")
            }
        }else {
            throw IncorrectResponseCodeException("Response returned 400 - 500 status code",
                response.code
            )
        }
    }

    /**
     * Function is part of
     * @see createLiveSummoners
     */
    private suspend fun getSummonerSpellsById(spellId: Int, secondSpellId2 : Int) : List<LiveSummonerSpell> {
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

    /**
     * Gets the champion name of a single champion by champion Id.
     * @param  championId
     * @see createLiveSummoners
     * @return String
     */
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

        val response = client.newCall(request).await()
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

    /**
     * Creates the whole liveMatch and returns it.
     * @param result this is the response of the call
     * @see  getLiveMatch
     */
    private suspend fun createLiveMatch(result : String) : LiveMatch {
        val jsonObject = JSONTokener(result).nextValue() as JSONObject
        val participantsArray = jsonObject.get("participants") as JSONArray

        return LiveMatch(
            jsonObject.get("gameId") as Long,
            jsonObject.get("gameMode") as String,
            createLiveSummoners(participantsArray)
        )
    }

    /**
     * Combines all the information from the two API's and returns a list of liveSummoners
     * @param summoners
     * @see createLiveMatch
     *
     */
    private suspend fun createLiveSummoners(summoners : JSONArray) : List<LiveSummoner>{
        val liveSummonerList : MutableList<LiveSummoner> = mutableListOf()
        for (i in 0 until summoners.length()) {
            val summoner =  summoners.getJSONObject(i)

            val spells = getSummonerSpellsById(summoner.get("spell1Id") as Int, summoner.get("spell2Id") as Int)
            val imagePath = getChampionNameById(summoner.get("championId") as Int)
            val rank = getRankBySummonerId(summoner.get("summonerId").toString())

            val liveSummoner = LiveSummoner(
                summoner.get("summonerName") as String,
                summoner.get("summonerId") as String,
                spells,
                imagePath,
                summoner.get("teamId") as Int,
                rank
            )
            liveSummonerList.add(liveSummoner)
        }
        return liveSummonerList
    }

    /**
     * Gets the result and creates the live summoners spells object with all attributes.
     * @param result
     * @param spellId
     * @param secondSpellId2
     * @see  createLiveSummoners
     * @sample LiveSummonerSpell
     */
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

    private fun createChampionNameById(result: String, championId: Int) : String {
        val jsonObject = JSONTokener(result).nextValue() as JSONObject
        val championsObject = jsonObject.get("data") as JSONObject
        val championsArray = championsObject.toJSONArray(championsObject.names())

        for(i in 0 until championsArray.length()){
            val champion = championsArray.get(i) as JSONObject
            val key = champion.get("key").toString().toInt()
            val championName = champion.get("name").toString()
            val imageObj = champion.get("image") as JSONObject
            val imagePath = imageObj.get("full").toString()
            if(key == championId){
                return imagePath
            }
        }
        return "Aatrox.png"
    }

    private fun createRankBySummonerId(result: String): Rank?{
        val jsonObject = JSONTokener(result).nextValue() as JSONArray
        if(jsonObject.length() != 0){
            for(i in 0 until jsonObject.length()){
                val rankObject = jsonObject.get(i) as JSONObject
                val rankType = rankObject.get("queueType")
                if(rankType == "RANKED_SOLO_5x5"){
                    val wins = rankObject.get("wins").toString().toInt()
                    val losses = rankObject.get("losses").toString().toInt()

                    val winrate = (wins * 100) / (wins + losses)

                    return Rank(
                        rankObject.get("tier").toString().lowercase(Locale.getDefault()),
                        rankObject.get("rank").toString(),
                        rankObject.get("leaguePoints").toString(),
                        "${winrate.toDouble().roundToInt()}%",
                        rankObject.get("hotStreak").toString().toBoolean()
                    )
                }
            }
        }else {
            return null
        }
        return null
    }

    private fun createSummoner(result: String) : Summoner {
        val jsonObject = JSONTokener(result).nextValue() as JSONObject

        return Summoner(
            jsonObject.getString("id"),
            jsonObject.getString("accountId"),
            jsonObject.getString("puuid"),
            jsonObject.getString("name"),
            jsonObject.getString("profileIconId"),
            jsonObject.getString("summonerLevel")
        )
    }


    object Keys {
        init {
            System.loadLibrary("native-lib")
        }

        external fun lolApiKey() : String
    }
}