package com.leaguebuddy.fragments.session

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.leaguebuddy.R
import com.leaguebuddy.SessionActivity
import com.leaguebuddy.exceptions.InvalidEmailException
import com.leaguebuddy.exceptions.InvalidPasswordException

class LoginFragment : Fragment() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button
    private lateinit var sessionActivity: SessionActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionActivity = activity as SessionActivity

        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnRegister = view.findViewById(R.id.btnRegister)
        btnLogin = view.findViewById(R.id.btnLogin)

        btnRegister.setOnClickListener { sessionActivity.replaceFragment(RegisterFragment()) }
        btnLogin.setOnClickListener { login() }
    }

    private fun login() {
        sessionActivity.login(
            etEmail.text.toString(),
            etPassword.text.toString()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

}