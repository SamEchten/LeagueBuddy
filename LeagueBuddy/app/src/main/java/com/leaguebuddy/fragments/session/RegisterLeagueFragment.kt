package com.leaguebuddy.fragments.session

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.leaguebuddy.MainActivity
import com.leaguebuddy.R
import com.leaguebuddy.SessionActivity

class RegisterLeagueFragment : FormFragment() {
    private lateinit var etLeagueName: EditText
    private lateinit var btnBack: Button
    private lateinit var btnFinish: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionActivity = activity as SessionActivity

        etLeagueName = view.findViewById(R.id.etLeagueName)
        btnBack = view.findViewById(R.id.btnBack)
        btnFinish = view.findViewById(R.id.btnFinish)

        btnBack.setOnClickListener { sessionActivity.prevFragment() }
        btnFinish.setOnClickListener { finishRegistration() }
    }

    private fun finishRegistration() {
        if(validLeagueId()) {
            storeForm(mapOf(
                "leagueId" to etLeagueName.text.toString()
            ))
            sessionActivity.registerUser()
            sessionActivity.showHomeScreen()
        } else {
            //LeagueId is not valid
            println("LeagueID is not valid")
        }
    }

    private fun validLeagueId(): Boolean {
        if(etLeagueName.text.toString().length <= 16) {
            return true
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_league, container, false)
    }
}