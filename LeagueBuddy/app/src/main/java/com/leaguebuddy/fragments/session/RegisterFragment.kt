package com.leaguebuddy.fragments.session

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.leaguebuddy.CryptoManager
import com.leaguebuddy.R
import com.leaguebuddy.SessionActivity
import com.leaguebuddy.exceptions.InvalidEmailException
import com.leaguebuddy.exceptions.InvalidFormException
import com.leaguebuddy.exceptions.InvalidPasswordException
import com.leaguebuddy.exceptions.InvalidUserNameException

class RegisterFragment() : FormFragment() {
    private lateinit var etUserName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepeatPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    private val cryptoManager: CryptoManager = CryptoManager()

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
        try {
            validateForms()
            validateEmail()
            validateUserName()
            validatePasswords()

            val encryptedEmail = cryptoManager.encrypt(etEmail.text.toString())
            val encryptedPwd = cryptoManager.encrypt(etPassword.text.toString())

            //Store form data to the sharedPreferences
            storeForm(mapOf(
                "userName" to etUserName.text.toString(),
                "email" to encryptedEmail,
                "password" to encryptedPwd
            ))

            Log.d(TAG, "First usercredentials saved")

            //Switch fragment to next registration form
            sessionActivity.replaceFragment(RegisterDiscordFragment())
        } catch(e: Exception) {
            e.message?.let {
                showToast(it)
                Log.e(TAG, it) }
        }
    }

    private fun validateUserName() {
        if(etUserName.text.length > 16) {
            throw InvalidUserNameException()
        }

        if(etUserName.text.contains("#")
            || etUserName.text.contains("default#")) {
            throw InvalidUserNameException()
        }
        return
    }

    private fun validatePasswords() {
        if(etPassword.text.toString() != etRepeatPassword.text.toString()) {
            throw InvalidPasswordException("Passwords do not match.")
        }

        if(etPassword.text.toString().length < 6) {
            throw InvalidPasswordException("Password must be atleast 6 characters long.")
        }
    }

    private fun validateForms() {
        if(etUserName.text.isNotEmpty()
            && etEmail.text.isNotEmpty()
            && etPassword.text.isNotEmpty()
            && etRepeatPassword.text.isNotEmpty()
        ) {
            return
        }
        throw InvalidFormException()
    }

    private fun validateEmail() {
        val email = etEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw InvalidEmailException()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }
}