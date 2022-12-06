package com.leaguebuddy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leaguebuddy.dataClasses.Friend
import com.leaguebuddy.dataClasses.User
import com.leaguebuddy.databinding.ActivityRegistrationBinding
import com.leaguebuddy.fragments.session.LoginFragment
import kotlin.reflect.full.memberProperties

class SessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private val db = Firebase.firestore
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        sp = getSharedPreferences("registrationForm", Context.MODE_PRIVATE)
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

    fun registerUser() {
        val user = createUser()
        if(!validUser(user)) {
            throw Exception()
        }
        insertUser(user)
    }

    private fun validUser(user: User): Boolean {
        var valid = true
        for(propery in User::class.memberProperties) {
            try {
                val value = propery.get(user) as String
                if(value.split('#')[0] == "default") {
                    valid = false
                }
            } catch(_: Exception) {}
        }
        return valid
    }

    private fun createUser(): User {
        return User(
            sp.getString("userName", "default#UserName").toString(),
            sp.getString("email", "default#Email").toString(),
            sp.getString("password", "default#Password").toString(),
            sp.getString("leagueId", "default#LeagueId").toString(),
            sp.getString("discordId", "default#DiscordId").toString(),
            ArrayList(),
            emptyMap()
        )
    }

    private fun insertUser(user: User) {
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