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

class RegisterLeagueFragment : Fragment() {
    private lateinit var sessionActivity: SessionActivity
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_league, container, false)
    }
}