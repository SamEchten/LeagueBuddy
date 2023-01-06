package com.leaguebuddy.fragments.main.matchFragments

import android.content.IntentSender.OnFinished
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import com.leaguebuddy.R
import com.leaguebuddy.api.LeagueApiHelper
import com.leaguebuddy.dataClasses.LiveSummoner
import com.leaguebuddy.dataClasses.LiveSummonerSpell
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

        val ivFirstSpell = view.findViewById<ImageView>(R.id.ivFirstSpell)
        val ivSecondSpell = view.findViewById<ImageView>(R.id.ivSecondSpell)

        val btnFirstSpell = view.findViewById<ToggleButton>(R.id.btnFirstSpellTimer)
        val btnSecondSpell = view.findViewById<ToggleButton>(R.id.btnSecondSpellTimer)

        ivFirstSpell.setImageResource(resources.getIdentifier("s_${liveSummoner.spells[0].id}","drawable", view.context.packageName))
        ivSecondSpell.setImageResource(resources.getIdentifier("s_${liveSummoner.spells[1].id}","drawable", view.context.packageName))

        summonerName.text = liveSummoner.summonerName

        addCheckListeners(liveSummoner.spells[0].duration, btnFirstSpell, ivFirstSpell);
        addCheckListeners(liveSummoner.spells[0].duration, btnSecondSpell, ivSecondSpell);

        layout.addView(view);
    }

    private fun addCheckListeners(duration : Int, btn : ToggleButton, imageView: ImageView ){
        val timer = object : CountDownTimer((duration * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                btn.text = (millisUntilFinished / 1000).toString()
            }
            override fun onFinish() {
                btn.text = ""
                imageView.alpha  = 1f
            }
        }
        btn.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                imageView.alpha  = 0.30f
                timer.start()
            }else {
                timer.cancel()
                btn.text = ""
                imageView.alpha  = 1f
            }
        }

    }


}