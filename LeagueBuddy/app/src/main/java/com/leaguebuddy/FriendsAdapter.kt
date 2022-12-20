package com.leaguebuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.leaguebuddy.dataClasses.Friend

class FriendsAdapter(private val friendsList: ArrayList<Friend>): RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {
    private val friends: ArrayList<Friend> = friendsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friends[position]
        holder.friendName.text = friend.userName

        val statusColor = if (friend.online) R.color.greenAccent else R.color.redAccent
        holder.friendStatus.backgroundTintList = ContextCompat.getColorStateList(holder.friendStatus.context, statusColor)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val friendName: TextView = itemView.findViewById(R.id.tvFriendName)
        val friendStatus: ImageView = itemView.findViewById(R.id.ivStatus)
    }
}