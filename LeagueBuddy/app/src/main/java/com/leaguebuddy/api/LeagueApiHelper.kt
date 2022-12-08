package com.leaguebuddy.api

import com.leaguebuddy.BuildConfig
import com.leaguebuddy.MainActivity
import com.leaguebuddy.api.dataclasses.Summoner
import net.pwall.json.schema.JSONSchema
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.Path

class LeagueApiHelper {
    private var client : OkHttpClient = OkHttpClient()
    private var apiKey : String = "RGAPI-fa63abaf-4226-4d72-b316-5d9d291edb5b"// Get the api key and decrypt it so we can receive the information

    fun getSummonerInfo(summonerName: String) : Summoner {
        if(summonerName.length < 16){
            val url = "https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/$summonerName?api_key=$apiKey"
            val request : Request = Request.Builder()
                .url(url)
                .build()
            try {
                val response = client.newCall(request).execute()
                if(response.isSuccessful) {
                    val result = response.body()?.string()
                    if (result != null) {
                        if(result.isNotEmpty()) {
                            return createSummoner(result)
                        }else {
                            throw Exception("Could not fetch Summoner")
                        }
                    }else {
                        throw Exception("Response is empty")
                    }
                }else {
                    throw Exception("Response returned 400-500 status code")
                }
            }catch(e : Exception){
                throw Exception("Could not fetch data from League API")
            }
        }else {
            throw Exception("Summoner name must be smaller than 16 characters")
        }

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