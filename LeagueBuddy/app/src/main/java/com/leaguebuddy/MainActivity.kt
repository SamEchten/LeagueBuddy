package com.leaguebuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leaguebuddy.api.GameNewsApiHelper
import com.leaguebuddy.api.LeagueApiHelper
import com.leaguebuddy.databinding.ActivityMainBinding
import com.leaguebuddy.fragments.main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navBar: BottomNavigationView
    private lateinit var  binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        replaceFragment(HomeFragment())

        val leagueApi = LeagueApiHelper()
        val gameNewsApiHelper = GameNewsApiHelper()

        GlobalScope.launch { Dispatchers.IO

            val summonerTask = async { leagueApi.getSummonerInfo("Aeolxs") }
            //val allNews  = async { gameNewsApiHelper.getNewestGameNews("GTasdasdasd") }

            val data = summonerTask.await()
            println(data.puuid)
        }

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