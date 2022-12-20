package com.leaguebuddy.api

import com.leaguebuddy.api.dataclasses.NewsArticle
import com.leaguebuddy.api.dataclasses.Summoner
import com.leaguebuddy.exceptions_v2.CouldNotFetchDataException
import com.leaguebuddy.exceptions_v2.IncorrectResponseCodeException
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class GameNewsApiHelper {
    private var client : OkHttpClient = OkHttpClient()
    private var apiKey : String = "2f1a547abdmsh179a9be92a0054dp1ce29ajsnc669ce37838b"// Get the api key and decrypt it so we can receive the information

    /**
     * Function returns a list of all the news articles from a specific game
     * @param game
     */
    fun getNewestGameNews(game: String) : List<NewsArticle> {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("videogames-news2.p.rapidapi.com")
            .addPathSegment("videogames_news")
            .addPathSegment("search_news")
            .addQueryParameter("query", game)
            .build()

        val request : Request = Request.Builder()
            .url(url)
            .header("X-RapidAPI-Key",  apiKey)
            .header("X-RapidAPI-Host",  "videogames-news2.p.rapidapi.com")
            .build()
        try {
            val response = client.newCall(request).execute()
            val result = response.body?.string()

            if(response.isSuccessful) {
                if(result != null) {
                    if(result.isNotEmpty()) {
                        return createNewsArticles(result)
                    }else {
                        throw Exception("Response is empty")
                    }
                }else {
                    throw Exception("Response is empty")
                }
            }else {
                throw Exception("Response returned 400-500 status code")
            }
        } catch(e: Exception) {
            throw Exception(e)
        }
    }


    /**
     * Function returns a list of all the recent game news articles
     */
    fun getRecentGameNews() : List<NewsArticle>{
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("videogames-news2.p.rapidapi.com")
            .addPathSegment("videogames_news")
            .addPathSegment("recent")
            .build()

        val request : Request = Request.Builder()
            .url(url)
            .header("X-RapidAPI-Key",  apiKey)
            .header("X-RapidAPI-Host",  "videogames-news2.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        val result = response.body?.string()

        if(response.isSuccessful) {
            if(result != null) {
                if(result.isNotEmpty()) {
                    return createNewsArticles(result)
                }else {
                    throw CouldNotFetchDataException("Could not get recent news")
                }
            }else {
                throw CouldNotFetchDataException("Could not get recent news")
            }
        }else {
            throw IncorrectResponseCodeException("Could not get recent news", response.code)
        }
    }

    private fun createNewsArticles(result: String): List<NewsArticle> {
        val jsonObj = JSONArray(result)
        val articles : MutableList<NewsArticle> = mutableListOf<NewsArticle>()
        for(i in 0 until jsonObj.length()){
            val article = jsonObj.getJSONObject(i)
            val newsArticle = NewsArticle(
                article.get("title").toString(),
                article.get("date").toString(),
                article.get("description").toString(),
                article.get("image").toString(),
                article.get("link").toString()
            )
            articles + newsArticle
        }
        return articles
    }
}