package com.leaguebuddy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leaguebuddy.dataClasses.User
import com.leaguebuddy.databinding.ActivityRegistrationBinding
import com.leaguebuddy.fragments.session.LoginFragment

class SessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        replaceFragment(LoginFragment())
    }

    fun replaceFragment(fragment: Fragment) {
        with(supportFragmentManager.beginTransaction()) {
            addToBackStack(null)
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }

    fun insertUserData(user: User) {
        db.collection("users").add(user)
    }

    fun prevFragment() {
        supportFragmentManager.popBackStack()
    }

    fun showHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}