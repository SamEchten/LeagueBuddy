package com.leaguebuddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.leaguebuddy.R
import com.leaguebuddy.dataClasses.NewsArticle
import com.leaguebuddy.dataClasses.NewsItem

class NewsRecyclerAdapter(private val itemList: ArrayList<NewsArticle>): RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>() {
    private val newsItems: ArrayList<NewsArticle> = itemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curItem = newsItems[position]
        holder.itemTitle.text = curItem.title
        holder.itemText.text = curItem.description
        holder.itemDate.text = curItem.date
        holder.itemImage.load(curItem.image)
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val item: LinearLayout = itemView.findViewById(R.id.llNewsItem)
        val itemTitle: TextView = itemView.findViewById(R.id.tvNewsItemTitle)
        val itemText: TextView = itemView.findViewById(R.id.tvNewsItemText)
        val itemDate: TextView = itemView.findViewById(R.id.tvNewsItemDate)
        val itemImage: ImageView = itemView.findViewById(R.id.ivNewsImage)
    }
}