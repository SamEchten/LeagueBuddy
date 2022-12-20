package com.leaguebuddy.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.leaguebuddy.MainActivity
import com.leaguebuddy.R
import com.leaguebuddy.api.LeagueApiHelper
import com.leaguebuddy.dataClasses.LiveMatch
import com.leaguebuddy.fragments.main.matchFragments.MatchStatsFragment
import com.leaguebuddy.fragments.main.matchFragments.SpellTimerFragment
import kotlinx.coroutines.*
import java.lang.Runnable

class MatchFragment : Fragment() {
    private lateinit var frameLayout: FrameLayout
    private lateinit var mainActivity: MainActivity
    private lateinit var matchStatsBtn: Button
    private lateinit var spellTimerBtn: Button
    private lateinit var leagueApiHelper: LeagueApiHelper
    private lateinit var linearLayoutHeader: LinearLayout
    lateinit var liveMatch: LiveMatch

    private var pusiPuu : String = "tecEajzxPe6Up_Y2B26x-rTdHpd07lTBD13vdfWud3TB8BnJux6UEXP7aw"
    private var BROHAN : String = "1LcBJ3AZathZ8el4_XSd_2GW5sEoUIV7e8jdMsow62IOF-AROCDF0ujlkw"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        leagueApiHelper = LeagueApiHelper()
        linearLayoutHeader = view.findViewById(R.id.llMatchHeader)
        replaceFragment(NoMatchFragment())

        GlobalScope.launch {
            try {
                liveMatch = leagueApiHelper.getLiveMatch(BROHAN)
                setLiveHeaderStats(linearLayoutHeader, view)
                addClickListeners(view)
                replaceFragment(MatchStatsFragment())
                println("Setting live match data to match fragment")
            }catch (e: Exception){
                println(e)
            }

        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    private fun setLiveHeaderStats(layout: LinearLayout, view: View) {
        activity?.runOnUiThread(Runnable {
            val matchIdTv : TextView = view.findViewById(R.id.tvMatchId)
            val liveGame : TextView = view.findViewById(R.id.tvLiveGame)

            liveGame.text = "Live"
            liveGame.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.redAccent)
            matchIdTv.text = liveMatch.gameMode

        })
    }

    private fun addClickListeners(view: View) {
    matchStatsBtn = view.findViewById(R.id.btnMatchStats)
    spellTimerBtn = view.findViewById(R.id.btnSpellTimer)

    matchStatsBtn.setOnClickListener {
        replaceFragment(MatchStatsFragment())
        matchStatsBtn.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.blueAccent)
        spellTimerBtn.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.greyAccent)
    }

    spellTimerBtn.setOnClickListener {
        replaceFragment(SpellTimerFragment())
        matchStatsBtn.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.greyAccent)
        spellTimerBtn.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.blueAccent)
    }
    }

    private fun replaceFragment(fragment: Fragment) {
        with(childFragmentManager.beginTransaction()) {
            replace(R.id.flMatchFragmentHolder, fragment)
            commit()
        }
    }

}