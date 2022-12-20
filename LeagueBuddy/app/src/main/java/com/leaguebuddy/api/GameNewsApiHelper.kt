package com.leaguebuddy.api

import com.leaguebuddy.dataClasses.NewsArticle
import com.leaguebuddy.exceptions.CouldNotFetchGameNewsDataException
import com.leaguebuddy.exceptions.IncorrectResponseCodeExceptionGamesNews
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import ru.gildor.coroutines.okhttp.await

class GameNewsApiHelper {
    private var client : OkHttpClient = OkHttpClient()
    //Storing api key is here is temporary, for testing purposes only
    private var apiKey : String = "2f1a547abdmsh179a9be92a0054dp1ce29ajsnc669ce37838b"// Get the api key and decrypt it so we can receive the information

    /**
     * Function returns a list of all the news articles from a specific game
     * @param game
     */
    suspend fun getGameNewsByTopic(game: String) : List<NewsArticle> {
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
        val response = client.newCall(request).await()
        val result = response.body?.string()

        if(response.isSuccessful) {
            if(result != null) {
                if(result.isNotEmpty()) {
                    return createNewsArticles(result)
                }else {
                    throw CouldNotFetchGameNewsDataException("Could not get news by topic")
                }
            }else {
                throw CouldNotFetchGameNewsDataException("Could not get news by topic")
            }
        }else {
            throw IncorrectResponseCodeExceptionGamesNews("Could not get news by topic", response.code)
        }
    }


    /**
     * Function returns a list of all the recent game news articles
     */
    suspend fun getRecentGameNews() : List<NewsArticle>{
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
        val response = client.newCall(request).await()
        val result = response.body?.string()
        if(response.isSuccessful) {
            if(result != null) {
                if(result.isNotEmpty()) {
                    return createNewsArticles(result)
                }else {
                    throw CouldNotFetchGameNewsDataException("Could not get recent news")
                }
            }else {
                throw CouldNotFetchGameNewsDataException("Could not get recent news")
            }
        }else {
            throw IncorrectResponseCodeExceptionGamesNews("Could not get recent news", response.code)
        }
    }

    private fun createNewsArticles(result: String): List<NewsArticle> {
        val jsonObj = JSONArray(result)
        val articles : MutableList<NewsArticle> = mutableListOf()
        for(i in 0 until jsonObj.length()){
            val article = jsonObj.getJSONObject(i)
            val newsArticle = NewsArticle(
                article.get("title").toString(),
                article.get("date").toString(),
                article.get("description").toString(),
                article.get("image").toString(),
                article.get("link").toString()
            )
            articles.add(newsArticle)
        }
        return articles
    }
}