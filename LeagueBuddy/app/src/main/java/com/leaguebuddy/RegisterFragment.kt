package com.leaguebuddy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class RegisterFragment() : Fragment() {
    private lateinit var etUserName: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepeatPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var registrationActivity: SessionActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationActivity = activity as SessionActivity

        etUserName = view.findViewById(R.id.etUserName)
        etPassword = view.findViewById(R.id.etPassword)
        etRepeatPassword = view.findViewById(R.id.etRepeatPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnRegister = view.findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener { registrationActivity.replaceFragment(LoginFragment())  }
        btnRegister.setOnClickListener {  }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }
}