package com.leaguebuddy.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leaguebuddy.R
import com.leaguebuddy.adapters.NewsRecyclerAdapter
import com.leaguebuddy.dataClasses.NewsItem


class HomeFragment : Fragment() {
    private lateinit var rvNews: RecyclerView
    private val newsItems: ArrayList<NewsItem> = ArrayList()
    private lateinit var newsAdapter: NewsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for(i in 1..10) {
            addNewsItem(NewsItem("League of Legends zuigt", "LOL zuigt echt heel erg hard, bla die bla die bla die bla ", "6-9-2069"))
        }
        newsAdapter = NewsRecyclerAdapter(newsItems)

        rvNews = view.findViewById(R.id.rvNews)
        rvNews.layoutManager = LinearLayoutManager(activity)
        rvNews.adapter = newsAdapter
    }

    private fun addNewsItem(newsItem: NewsItem) {
        newsItems.add(newsItem)
        newsAdapter.notifyItemInserted(newsAdapter.itemCount)
    }

}