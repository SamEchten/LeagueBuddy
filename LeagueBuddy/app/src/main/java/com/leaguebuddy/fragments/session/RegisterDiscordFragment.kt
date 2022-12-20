package com.leaguebuddy.fragments.session

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.common.primitives.UnsignedBytes.toInt
import com.leaguebuddy.CryptoManager
import com.leaguebuddy.R
import com.leaguebuddy.SessionActivity
import com.leaguebuddy.exceptions.InvalidDiscordIdException

class RegisterDiscordFragment : FormFragment() {
    private lateinit var etDiscordId: EditText
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button
    private val cryptoManager: CryptoManager = CryptoManager()

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
        try {
            validateDiscordId()

            val encryptedDiscordId = cryptoManager.encrypt(etDiscordId.text.toString())
            storeForm(mapOf(
                "discordId" to encryptedDiscordId
            ))

            Log.d(TAG, "Discord id saved")

            sessionActivity.replaceFragment(RegisterLeagueFragment())
        } catch(e: Exception) {
            e.message?.let {
                showToast(it)
                Log.e(TAG, it) }
        }
    }

    private fun validateDiscordId() {
        val discordId = etDiscordId.text.toString()

        if(!discordId.contains('#')) {
            throw InvalidDiscordIdException()
        }

        val id = discordId.split("#")[1]
        if(id.length != 4) {
            throw InvalidDiscordIdException()
        }

        for(number in id) {
            if(!number.isDigit()) {
                throw InvalidDiscordIdException()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_discord, container, false)
    }
}