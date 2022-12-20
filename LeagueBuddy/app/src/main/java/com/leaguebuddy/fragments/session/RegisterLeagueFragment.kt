package com.leaguebuddy.fragments.session

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.leaguebuddy.CryptoManager
import com.leaguebuddy.MainActivity
import com.leaguebuddy.R
import com.leaguebuddy.SessionActivity
import com.leaguebuddy.exceptions.InvalidLeagueIdException

class RegisterLeagueFragment : FormFragment() {
    private lateinit var etLeagueName: EditText
    private lateinit var btnBack: Button
    private lateinit var btnFinish: Button
    private val cryptoManager: CryptoManager = CryptoManager()

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
        try {
            validateLeagueId()

            val ecryptedLeagueId = cryptoManager.encrypt(etLeagueName.text.toString())
            storeForm(mapOf(
                "leagueId" to ecryptedLeagueId
            ))
            Log.d(TAG, "LeagueId saved")

            sessionActivity.registerUser()
        } catch(e: Exception) {
            e.message?.let {
                showToast(it)
                Log.e(TAG, it) }
        }
    }

    private fun validateLeagueId() {
        if(etLeagueName.text.toString().length > 16) {
            throw InvalidLeagueIdException()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_league, container, false)
    }
}