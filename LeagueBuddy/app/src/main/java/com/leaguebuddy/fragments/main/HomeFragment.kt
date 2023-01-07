package com.leaguebuddy.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leaguebuddy.R
import com.leaguebuddy.adapters.NewsRecyclerAdapter
import com.leaguebuddy.api.GameNewsApiHelper
import com.leaguebuddy.dataClasses.NewsArticle
import com.leaguebuddy.dataClasses.NewsItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var rvNews: RecyclerView
    private val newsItems: ArrayList<NewsArticle> = ArrayList()
    private lateinit var newsAdapter: NewsRecyclerAdapter
    private var gameNewsApiHelper : GameNewsApiHelper = GameNewsApiHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsAdapter = NewsRecyclerAdapter(newsItems)
        val newsHeader = view.findViewById<TextView>(R.id.tvNewsHeader)
        val topic = view.findViewById<TextView>(R.id.tvTopic)

        //setRecentNews()

        newsHeader.setOnClickListener {
            setRecentNews()
            topic.text = "Latest news"
        }

        topic.setOnClickListener {
            val selectMenu = PopupMenu(context, topic)
            selectMenu.inflate(R.menu.news_nav)
            selectMenu.setOnMenuItemClickListener {
                setNewsByTopic(it.title.toString())
                topic.text = it.title.toString()
                true
            }
            selectMenu.show()
        }
        rvNews = view.findViewById(R.id.rvNews)
        rvNews.adapter = newsAdapter
        rvNews.layoutManager = LinearLayoutManager(activity)

    }

    private fun addNewsItem(newsArticle: NewsArticle) {
        activity?.runOnUiThread(Runnable {
            newsItems.add(newsArticle)
            newsAdapter.notifyItemInserted(newsAdapter.itemCount)
        })
    }

    private fun setRecentNews(){
        clear()
        newsAdapter.notifyDataSetChanged()
        GlobalScope.launch {
            try {
                val recentNews = gameNewsApiHelper.getRecentGameNews()
                for (i in recentNews){
                    addNewsItem(i)
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    private fun setNewsByTopic(topic: String){
        clear()
        GlobalScope.launch {
            try {
                val topicNews = gameNewsApiHelper.getGameNewsByTopic(topic)
                for (i in topicNews){
                    addNewsItem(i)
                }
            }catch (e: Exception) {
                println(e)
            }

        }
    }

    private fun clear () {
        val size = newsItems.size;
        newsItems.clear();
        newsAdapter.notifyItemRangeRemoved(0, size);
    }

}