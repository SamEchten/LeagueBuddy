package com.leaguebuddy.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leaguebuddy.FriendsAdapter
import com.leaguebuddy.R
import com.leaguebuddy.dataClasses.Friend

class FriendsFragment : Fragment() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val friends = ArrayList<Friend>()
    lateinit var friendsAdapter: FriendsAdapter
    private lateinit var rvFriends: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("test")

        friends.add(Friend("124318u418", "Bram Jansen", true))
        friends.add(Friend("1241f2r1r", "Noah Scheffer", true))
        friends.add(Friend("are21r12r", "Terror Teake", false))

        rvFriends = view.findViewById(R.id.rvFriends)
        rvFriends.layoutManager = LinearLayoutManager(context)
        friendsAdapter = FriendsAdapter(friends)
        rvFriends.adapter = friendsAdapter

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }
}