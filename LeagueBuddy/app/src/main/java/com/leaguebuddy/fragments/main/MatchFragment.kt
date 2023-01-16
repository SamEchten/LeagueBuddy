package com.leaguebuddy.fragments.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leaguebuddy.CryptoManager
import com.leaguebuddy.LoaderFragment
import com.leaguebuddy.MainActivity
import com.leaguebuddy.R
import com.leaguebuddy.api.DiscordApiHelper
import com.leaguebuddy.api.LeagueApiHelper
import com.leaguebuddy.dataClasses.LiveMatch
import com.leaguebuddy.fragments.main.matchFragments.MatchStatsFragment
import com.leaguebuddy.fragments.main.matchFragments.SpellTimerFragment
import com.leaguebuddy.sql.SqlDbHelper
import kotlinx.coroutines.*
import java.lang.Runnable

class MatchFragment : Fragment() {
    private lateinit var frameLayout: FrameLayout
    private lateinit var mainActivity: MainActivity
    private lateinit var matchStatsBtn: Button
    private lateinit var spellTimerBtn: Button
    private var leagueApiHelper: LeagueApiHelper = LeagueApiHelper()
    private var discordApiHelper : DiscordApiHelper = DiscordApiHelper()
    private var auth : FirebaseAuth = Firebase.auth
    private lateinit var linearLayoutHeader: LinearLayout
    lateinit var liveMatch: LiveMatch
    private lateinit var sqlDbHelper: SqlDbHelper
    private val cryptoManager = CryptoManager()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        sqlDbHelper = SqlDbHelper(mainActivity)

        linearLayoutHeader = view.findViewById(R.id.llMatchHeader)

        GlobalScope.launch {
            try {
                replaceFragment(LoaderFragment())

                val user = sqlDbHelper.getCurrentUserCredentials()
                // Load in livematch data of current user
                liveMatch = leagueApiHelper.getLiveMatch(user.summonerId)

                setLiveHeaderStats(linearLayoutHeader, view)
                addClickListeners(view)

                replaceFragment(MatchStatsFragment())

                // Send channel invite
                auth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        GlobalScope.launch{
                            var discordId = cryptoManager.decrypt(user.discordId)
                            val summonerId = user.summonerId
                            val publicKey = discordApiHelper.getPublicKey()

                            var ediscordId: String = cryptoManager.encryptUsingPublickey(discordId, publicKey)
                            ediscordId = ediscordId.replace("\n", "")
                            discordApiHelper.sendInviteLink(liveMatch, task.result.token.toString(), ediscordId, summonerId)
                        }
                    } else {
                      Log.e("AUTH","User is not logged in")
                    }
                }
            }catch (e: Exception){
                summonerNotInGame()
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

    private fun summonerNotInGame(){
        try {
            replaceFragment(NoMatchFragment())
        }catch (e: Exception){
            println(e)
        }
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

    companion object {
        var pusiPuu : String = "tecEajzxPe6Up_Y2B26x-rTdHpd07lTBD13vdfWud3TB8BnJux6UEXP7aw"
        var BROHAN : String = "1LcBJ3AZathZ8el4_XSd_2GW5sEoUIV7e8jdMsow62IOF-AROCDF0ujlkw"
        var RATIRL : String = "Vr3IUGGjYJEOOkaqSHiyl-SRsKG055BglnSVfPUjstXA_8s"
        var Aeolxs : String = "q_t7xuxUZLz3-QzhYaR3eJqzQ3ugP6vTxm3rEmpqFXEs_ps7"
        var kesha : String = "YFwLEUnlto9IPJUZH6uwMl8s_5hEFoy3QTWzsofET2KdUP6FT4thp6Rxpw"
    }

}