package com.leaguebuddy.fragments.main.matchFragments

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import coil.load
import com.leaguebuddy.R
import com.leaguebuddy.dataClasses.LiveSummoner
import com.leaguebuddy.fragments.main.MatchFragment

class MatchStatsFragment : Fragment() {
    private lateinit var linearlayoutTeam: LinearLayout
    private lateinit var linearlayoutOpponent: LinearLayout


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearlayoutTeam = view.findViewById(R.id.llTeamMates)
        linearlayoutOpponent = view.findViewById(R.id.llOpponent)

        loadLiveMatchData()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_stats, container, false)
    }

    private fun loadLiveMatchData(){
        try{
            val liveMatch = (parentFragment as MatchFragment).liveMatch
            createLiveSummonerItem(liveMatch.participants)
        }catch (e: Exception){
            println(e)
        }
    }

    private fun createLiveSummonerItem(list: List<LiveSummoner>){
        for(i in list.indices){
            val liveSummoner = list[i]
            if(list[i].teamId == 100) {
                addItemsToLayout(liveSummoner, linearlayoutTeam)
            }else {
                addItemsToLayout(liveSummoner, linearlayoutOpponent)
            }
        }
    }

    // Needs to get an object with data to create items based on team or opponents
    private fun addItemsToLayout(liveSummoner: LiveSummoner, layout: LinearLayout){
        // loop through object and add the match items.
        val view : View = layoutInflater.inflate(R.layout.match_item, null)
        val summonerChampImage: ImageView = view.findViewById(R.id.ivSummonerChampion)
        val username: TextView = view.findViewById(R.id.tvLeagueId)
        val rank: ImageView = view.findViewById(R.id.ivRank)

        val imageP = "https://ddragon.leagueoflegends.com/cdn/12.23.1/img/champion/${liveSummoner.championImage}"
        if(liveSummoner.rank != null){
            rank.setImageResource(resources.getIdentifier("r_${liveSummoner.rank?.tier}","drawable", view.context.packageName))
        }

        summonerChampImage.load(imageP)
        username.text = liveSummoner.summonerName

        layout.addView(view);
    }

}