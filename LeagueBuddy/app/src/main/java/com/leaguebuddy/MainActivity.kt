package com.leaguebuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leaguebuddy.api.LeagueApiHelper
import com.leaguebuddy.databinding.ActivityMainBinding
import com.leaguebuddy.fragments.main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navBar: BottomNavigationView
    private lateinit var binding : ActivityMainBinding
    private lateinit var leagueApi : LeagueApiHelper
    private val cryptoManager: CryptoManager = CryptoManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        replaceFragment(HomeFragment())

        val text = "dit is een test bericht"
        val cipher = cryptoManager.encryptUsingPublickey(text, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApUM//UNNYPwyre1rEFMs\n" +
                "8+thBZhecmlAyGCtM8q9CnpQuBqtBDwATDTU63Jg53nAO0FuaBvVTVekrY6rs6fe\n" +
                "hK/YNG9ga5obqPQ2D+QkqHq3Iud6x6yDSF7uzAOXFuU3egYUJH2+vt+FDyF3lRWf\n" +
                "RysBxIHp3N2OtWigXXrpRnL3th93PYnoTvrOE6t524oreDbyELJy3A/aewKeRdpt\n" +
                "cebQhKf5CDqZjrQpmGABHcgERhtJCdrdm6O3p6aMlVoG94OiOzzs98M+o3g9ti64\n" +
                "lZR44ybtXtBgd5DmlcABSkKjjfONXFXRSMx0glQd+TYWxhVtzEJOcy9WNtUUqZuD\n" +
                "IwIDAQAB")
        print(cipher)

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