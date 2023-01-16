package com.leaguebuddy.api

import android.util.Log
import com.google.gson.Gson
import com.leaguebuddy.CryptoManager
import com.leaguebuddy.dataClasses.LiveMatch
import com.leaguebuddy.dataClasses.discord.DiscordGame
import com.leaguebuddy.dataClasses.discord.DiscordPostBody
import com.leaguebuddy.dataClasses.discord.DiscordUser
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import ru.gildor.coroutines.okhttp.await

class DiscordApiHelper {
    private var client : OkHttpClient = OkHttpClient()
    private var gson : Gson = Gson()
    private val BASE_URL = "localhost:4848"

    suspend fun getPublicKey(): String {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(BASE_URL)
            .addPathSegment("api")
            .addPathSegment("publickey")
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).await()
        if(response.isSuccessful){
            return response.body?.string() ?: ""
        }else  {
            Log.d("Discord bot", "Could not retreive public key")
            throw Exception("Could not retreive public key")
        }
    }

    suspend fun sendInviteLink(liveMatch: LiveMatch, authKey: String, discordId: String, summonerId: String){
        val body = DiscordPostBody(
            DiscordUser(
                discordId,
                authKey
            ),
            DiscordGame(
                liveMatch.gameId,
                liveMatch.getTeamCodeBySummonerId(summonerId)
            )
        )

        val url = HttpUrl.Builder()
            .scheme("https")
            .host(BASE_URL)
            .addPathSegment("api")
            .addPathSegment("discord")
            .addPathSegment("createChannel")
            .build()

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val request = Request.Builder()
            .url(url)
            .post(gson.toJson(body).toRequestBody(mediaType))
            .build()

        val response = client.newCall(request).await()
        if(response.isSuccessful){
            Log.d("Discord bot", "Succesfully send discord link to the user")
        }else  {
            Log.d("Discord bot", "Could not send link to user")
        }
    }
}