package com.leaguebuddy.fragments.main

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.leaguebuddy.MainActivity
import com.leaguebuddy.R
import org.w3c.dom.Text

class MatchFragment : Fragment() {
    private lateinit var frameLayout: FrameLayout
    private lateinit var mainActivity: MainActivity
    private lateinit var matchStatsBtn: Button
    private lateinit var spellTimerBtn: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        replaceFragment(MatchStatsFragment())

        addClickListeners(view);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    private fun addClickListeners(view: View){
        matchStatsBtn = view.findViewById(R.id.btnMatchStats)
        spellTimerBtn = view.findViewById(R.id.btnSpellTimer)

        matchStatsBtn.setOnClickListener{
            replaceFragment(MatchStatsFragment())
            matchStatsBtn.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.blueAccent)
            spellTimerBtn.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.greyAccent)
        }

        spellTimerBtn.setOnClickListener{
            replaceFragment(SpellTimerFragment())
            matchStatsBtn.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.greyAccent)
            spellTimerBtn.backgroundTintList = ContextCompat.getColorStateList(mainActivity, R.color.blueAccent)
        }
    }

    private fun isSummonerInGame(){
        // If the summoner is NOT in a game display NotInGame fragment else load in game status
    }
    private fun replaceFragment(fragment: Fragment) {
        with(childFragmentManager.beginTransaction()) {
            replace(R.id.flMatchFragmentHolder, fragment)
            commit()
        }
    }


}