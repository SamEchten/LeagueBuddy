package com.leaguebuddy.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.leaguebuddy.MainActivity
import com.leaguebuddy.R
import com.leaguebuddy.api.LeagueApiHelper
import com.leaguebuddy.api.dataclasses.LiveMatch
import com.leaguebuddy.fragments.main.matchFragments.MatchStatsFragment
import com.leaguebuddy.fragments.main.matchFragments.SpellTimerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MatchFragment : Fragment() {
    private lateinit var frameLayout: FrameLayout
    private lateinit var mainActivity: MainActivity
    private lateinit var matchStatsBtn: Button
    private lateinit var spellTimerBtn: Button
    private lateinit var leagueApiHelper: LeagueApiHelper
    private lateinit var linearLayoutHeader: LinearLayout
    lateinit var testa : String
    lateinit var liveMatch: LiveMatch

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        replaceFragment(NoMatchFragment())
        linearLayoutHeader = view.findViewById(R.id.llMatchHeader)

        testa = "asda"

        leagueApiHelper = LeagueApiHelper()
        GlobalScope.launch { Dispatchers.Main
            try {
                liveMatch = leagueApiHelper.getLiveMatch("q_t7xuxUZLz3-QzhYaR3eJqzQ3ugP6vTxm3rEmpqFXEs_ps7")
                setLiveHeaderStats(linearLayoutHeader, view)
                addClickListeners(view);
                replaceFragment(MatchStatsFragment())
            }catch (e: Exception){
                println(e)
                replaceFragment(NoMatchFragment())
                activity?.runOnUiThread(Runnable {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                })
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
            liveGame.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.blueAccent)
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