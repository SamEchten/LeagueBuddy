package com.leaguebuddy.fragments.main.matchFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.leaguebuddy.R
import com.leaguebuddy.api.LeagueApiHelper
import com.leaguebuddy.dataClasses.LiveSummoner
import com.leaguebuddy.fragments.main.MatchFragment

class SpellTimerFragment : Fragment() {
    private lateinit var linearLayout: LinearLayout
    private lateinit var leagueApiHelper: LeagueApiHelper;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leagueApiHelper = LeagueApiHelper()
        linearLayout = view.findViewById(R.id.llSpellTimer)

        loadLiveMatchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spell_timer, container, false)
    }

    private fun loadLiveMatchData(){
        try {
            val liveMatch = (parentFragment as MatchFragment).liveMatch
            createLiveSummonerItem(liveMatch.participants)
        }catch (e: Exception){
            println(e)
        }
    }
    private fun createLiveSummonerItem(list: List<LiveSummoner>){
        for(i in list.indices){
            val liveSummoner = list[i]
            if(liveSummoner.teamId == 100) {
                addItemsToLayout(liveSummoner, linearLayout)
            }
        }
    }
    private fun addItemsToLayout(liveSummoner: LiveSummoner, layout: LinearLayout){
        // loop through object and add the match items.
        val view : View = layoutInflater.inflate(R.layout.spell_timer_item, null)

        val summonerName = view.findViewById<TextView>(R.id.tvLeagueId)

        val firstSpell = view.findViewById<ImageView>(R.id.ivFirstSpell)
        val secondSpell = view.findViewById<ImageView>(R.id.ivSecondSpell)

        firstSpell.setImageResource(resources.getIdentifier("s_${liveSummoner.spells[0].id}","drawable", view.context.packageName))
        secondSpell.setImageResource(resources.getIdentifier("s_${liveSummoner.spells[1].id}","drawable", view.context.packageName))

        summonerName.text = liveSummoner.summonerName

        firstSpell.setOnClickListener{
            println("Clicked the first spell")
        }
        secondSpell.setOnClickListener{
            println("Clicked the second spell")
        }
        layout.addView(view);
    }

}