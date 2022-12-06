package com.leaguebuddy.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.leaguebuddy.R

class MatchStatsFragment : Fragment() {
    private lateinit var linearlayoutTeam: LinearLayout
    private lateinit var linearlayoutOpponent: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearlayoutTeam = view.findViewById(R.id.llTeamMates)
        linearlayoutOpponent = view.findViewById(R.id.llOpponent)

        addItemsToLayout(linearlayoutTeam)
        addItemsToLayout(linearlayoutTeam)
        addItemsToLayout(linearlayoutTeam)
        addItemsToLayout(linearlayoutTeam)
        addItemsToLayout(linearlayoutTeam)

        addItemsToLayout(linearlayoutOpponent)
        addItemsToLayout(linearlayoutOpponent)
        addItemsToLayout(linearlayoutOpponent)
        addItemsToLayout(linearlayoutOpponent)
        addItemsToLayout(linearlayoutOpponent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_stats, container, false)
    }

    // Needs to get an object with data to create items based on team or opponents
    private fun addItemsToLayout(layout: LinearLayout){
        // loop through object and add the match items.
        val view : View = layoutInflater.inflate(R.layout.match_item, null)
        val username: TextView = view.findViewById(R.id.tvLeagueId)
        val rank: TextView = view.findViewById(R.id.tvLeagueRank)
        val winrate: TextView = view.findViewById(R.id.tvWinRate)

        layout.addView(view);
    }

}