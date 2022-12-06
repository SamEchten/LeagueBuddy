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
import com.leaguebuddy.R
import com.leaguebuddy.SessionActivity

class RegisterFragment() : FormFragment() {
    private lateinit var etUserName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepeatPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionActivity = activity as SessionActivity

        etUserName = view.findViewById(R.id.etUserName)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etRepeatPassword = view.findViewById(R.id.etRepeatPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnRegister = view.findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener { sessionActivity.replaceFragment(LoginFragment())  }
        btnRegister.setOnClickListener { continueRegistration() }
    }

    private fun continueRegistration() {
        if(allFieldsFilledIn()) {
            if(validUserName()) {
                if(passwordsMatch()) {
                    //Encrypt password before storing it
                    storeForm(mapOf(
                        "userName" to etUserName.text.toString(),
                        "email" to etEmail.text.toString(),
                        "password" to etPassword.text.toString()
                    ))
                    sessionActivity.replaceFragment(RegisterDiscordFragment())
                } else {
                    //Passwords do not match
                    println("Passwords do not match")
                }
            } else {
                //Username is not valid
                println("Username is not valid")
            }
        } else {
            //Not all fields are filled in
            println("Not all fields are filled in")
        }
    }

    private fun validUserName(): Boolean {
        if(etUserName.text.length < 16) {
            return true
        }
        return false
    }

    private fun passwordsMatch(): Boolean {
        if(etPassword.text.toString() == etRepeatPassword.text.toString()) {
            return true
        }
        return false
    }

    private fun allFieldsFilledIn(): Boolean {
        if(etUserName.text.isNotEmpty()
            && etEmail.text.isNotEmpty()
            && etPassword.text.isNotEmpty()
            && etRepeatPassword.text.isNotEmpty()
        ) {
            return true
        }
        return false
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }
}