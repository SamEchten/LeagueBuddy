package com.leaguebuddy.api

import com.leaguebuddy.BuildConfig
import com.leaguebuddy.MainActivity
import com.leaguebuddy.api.dataclasses.Summoner
import com.leaguebuddy.exceptions_v2.CouldNotFetchDataException
import com.leaguebuddy.exceptions_v2.CouldNotFetchSummonerException
import com.leaguebuddy.exceptions_v2.IncorrectResponseCodeException
import com.leaguebuddy.exceptions_v2.SummonerNameInvalidException
import net.pwall.json.schema.JSONSchema
import okhttp3.HttpUrl
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
    private var apiKey : String = "RGAPI-96c64fd9-848f-4f99-98ef-101ddb7b6885"// Get the api key and decrypt it so we can receive the information

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