package com.leaguebuddy.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.findFragment
import com.leaguebuddy.R

class SpellTimerFragment : Fragment() {
    private lateinit var linearLayout: LinearLayout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayout = view.findViewById(R.id.llSpellTimer)
        addItemsToLayout(linearLayout)
        addItemsToLayout(linearLayout)
        addItemsToLayout(linearLayout)
        addItemsToLayout(linearLayout)
        addItemsToLayout(linearLayout)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spell_timer, container, false)
    }

    private fun addItemsToLayout(layout: LinearLayout){
        // loop through object and add the match items.
        val view : View = layoutInflater.inflate(R.layout.spell_timer_item, null)
        val firstSpell = view.findViewById<ImageView>(R.id.ivFirstSpell)
        val secondSpell = view.findViewById<ImageView>(R.id.ivSecondSpell)

        firstSpell.setOnClickListener{
            println("Clicked the first spell")
        }
        secondSpell.setOnClickListener{
            println("Clicked the second spell")
        }
        layout.addView(view);
    }


}