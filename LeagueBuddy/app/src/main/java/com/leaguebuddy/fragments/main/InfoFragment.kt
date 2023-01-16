package com.leaguebuddy.fragments.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leaguebuddy.CryptoManager
import com.leaguebuddy.MainActivity
import com.leaguebuddy.R
import com.leaguebuddy.SessionActivity
import com.leaguebuddy.sql.SqlDbHelper

class InfoFragment : Fragment() {
    private lateinit var tvLoggedInAs: TextView
    private lateinit var tvSummoner: TextView
    private lateinit var btnLogOut: Button

    private val cryptoManager = CryptoManager()
    private lateinit var sqlDbHelper: SqlDbHelper
    private lateinit var mainActivity: MainActivity
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        sqlDbHelper = SqlDbHelper(mainActivity)

        tvLoggedInAs = view.findViewById(R.id.loggedInAs)
        tvSummoner = view.findViewById(R.id.tvSummoner)

        val user = sqlDbHelper.getCurrentUserCredentials()
        tvLoggedInAs.text = "Currently logged in as: ${auth.currentUser?.email}"
        tvSummoner.text = "Logged in League account: ${cryptoManager.decrypt(user.summonerName)}"

        btnLogOut = view.findViewById(R.id.btnLogout)
        btnLogOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, SessionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

}