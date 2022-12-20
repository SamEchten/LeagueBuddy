package com.leaguebuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leaguebuddy.api.GameNewsApiHelper
import com.leaguebuddy.api.LeagueApiHelper
import com.leaguebuddy.api.dataclasses.Summoner
import com.leaguebuddy.databinding.ActivityMainBinding
import com.leaguebuddy.exceptions_v2.CouldNotFetchDataException
import com.leaguebuddy.exceptions_v2.CouldNotFetchSummonerException
import com.leaguebuddy.exceptions_v2.IncorrectResponseCodeException
import com.leaguebuddy.exceptions_v2.SummonerNameInvalidException
import com.leaguebuddy.fragments.main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var navBar: BottomNavigationView
    private lateinit var  binding : ActivityMainBinding
    private lateinit var leagueApi : LeagueApiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        replaceFragment(HomeFragment())

        println("DONE")

        navBar = findViewById(R.id.navBar)
        navBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.friends -> replaceFragment(FriendsFragment())
                R.id.match -> replaceFragment(MatchFragment())
                R.id.history -> replaceFragment(HistoryFragment())
                R.id.info -> replaceFragment(InfoFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        with(supportFragmentManager.beginTransaction()) {
            addToBackStack(null)
            replace(R.id.flMain, fragment)
            commit()
        }
    }
}