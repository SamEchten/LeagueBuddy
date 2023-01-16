package com.leaguebuddy.api

import android.util.Log
import com.google.gson.Gson
import com.leaguebuddy.dataClasses.LiveMatch
import com.leaguebuddy.dataClasses.discord.DiscordGame
import com.leaguebuddy.dataClasses.discord.DiscordPostBody
import com.leaguebuddy.dataClasses.discord.DiscordUser
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.gildor.coroutines.okhttp.await

class DiscordApiHelper {
    private var client : OkHttpClient = OkHttpClient()
    private var gson : Gson = Gson()

    suspend fun sendInviteLink(liveMatch: LiveMatch, authKey: String, discordId: String){
        val body = DiscordPostBody(
            DiscordUser(
                discordId,
                authKey
            ),
            DiscordGame(
                liveMatch.gameId,
                liveMatch.getTeamCodeBySummonerId("q_t7xuxUZLz3-QzhYaR3eJqzQ3ugP6vTxm3rEmpqFXEs_ps7")
            )
        )

        val url = HttpUrl.Builder()
            .scheme("https")
            .host("4344-2a02-a467-14f7-1-d02d-20e2-d8bd-1200.eu.ngrok.io")
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