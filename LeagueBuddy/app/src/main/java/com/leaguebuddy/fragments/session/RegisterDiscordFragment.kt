package com.leaguebuddy.fragments.session

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.common.primitives.UnsignedBytes.toInt
import com.leaguebuddy.R
import com.leaguebuddy.SessionActivity

class RegisterDiscordFragment : FormFragment() {
    private lateinit var etDiscordId: EditText
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionActivity = activity as SessionActivity

        etDiscordId = view.findViewById(R.id.etdiscordId)
        btnBack = view.findViewById(R.id.btnBack)
        btnNext = view.findViewById(R.id.btnNext)

        btnBack.setOnClickListener { sessionActivity.prevFragment() }
        btnNext.setOnClickListener { continueRegistration() }
    }

    private fun continueRegistration() {
        if(validDiscordId()) {
            storeForm(mapOf(
                "discordId" to etDiscordId.text.toString()
            ))
            sessionActivity.replaceFragment(RegisterLeagueFragment())
        } else {
            //DiscordId is not valid
            println("DiscordId is not valid")
        }
    }

    private fun validDiscordId(): Boolean {
        val discordId = etDiscordId.text.toString()
        if(!discordId.contains('#')) {
            return false
        }

        val id = discordId.split("#")[1]
        if(id.length != 4) {
            return false
        }

        var valid = true;
        for(number in id) {
            if(!number.isDigit()) {
                valid = false
            }
        }

        return valid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_discord, container, false)
    }
}