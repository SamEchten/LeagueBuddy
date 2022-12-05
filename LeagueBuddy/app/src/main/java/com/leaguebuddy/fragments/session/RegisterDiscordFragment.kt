package com.leaguebuddy.fragments.session

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.leaguebuddy.R
import com.leaguebuddy.SessionActivity

class RegisterDiscordFragment : Fragment() {
    private lateinit var sessionActivity: SessionActivity
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
        btnNext.setOnClickListener { sessionActivity.replaceFragment(RegisterLeagueFragment()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_discord, container, false)
    }
}