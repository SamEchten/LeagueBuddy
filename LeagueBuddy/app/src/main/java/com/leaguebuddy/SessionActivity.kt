package com.leaguebuddy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Credentials
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leaguebuddy.dataClasses.User
import com.leaguebuddy.databinding.ActivityRegistrationBinding
import com.leaguebuddy.exceptions.FirebaseSignupException
import com.leaguebuddy.exceptions.LoginException
import com.leaguebuddy.exceptions.UserCreationException
import com.leaguebuddy.fragments.session.LoginFragment
import com.leaguebuddy.fragments.session.RegisterFragment
import com.leaguebuddy.fragments.session.RegisterLeagueFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var sp: SharedPreferences
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val cryptoManager: CryptoManager = CryptoManager()
    private val TAG = "REGISTRATION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        sp = getSharedPreferences("registrationForm", Context.MODE_PRIVATE)
        setContentView(binding.root)
        supportActionBar?.hide()
        load()
    }

    private fun load() {
        if(loggedIn()) {
            showHomeScreen()
        } else {
            replaceFragment(LoginFragment())
        }
    }

    private fun loggedIn(): Boolean {
        try {
            val credentials = getCredentials()
            val user = auth.currentUser
            if(user == null) {
                return false
            }

            login(credentials)
            return true
        } catch(e: LoginException) {
            handleError(e)
            return false
        }
    }

    fun replaceFragment(fragment: Fragment) {
        with(supportFragmentManager.beginTransaction()) {
            addToBackStack(null)
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }

    private fun login(credentials: AuthCredential) {
        auth.signInWithCredential(credentials)
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //Store user credentials
                val credentials = EmailAuthProvider.getCredential(email, password)
                storeLoginCredentials(credentials)

                Log.d(TAG, "userLogin:success")
                showHomeScreen()
            }
            .addOnFailureListener { exception ->
                handleError(exception)
            }
    }

    private fun storeLoginCredentials(credentials: AuthCredential) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val encryptedUid = cryptoManager.encrypt(credentials.toString())

        editor.putString("credentials", encryptedUid)
        editor.commit()
    }

    private fun getCredentials(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val encryptedUid = sharedPreferences.getString("credentials", "default#credentials")
        if(encryptedUid != null && encryptedUid != "default#credentials") {
            return cryptoManager.decrypt(encryptedUid)
        }
        throw LoginException("Could not retreive credentials from device.")
    }

    fun registerUser() {
        val userCredentials = getEncryptedCredentials()
        val email = userCredentials["email"]
        val password = userCredentials["password"]

        if(email == null || password == null) {
            throw Exception("Could not find email or password")
        }

        try {
            firebaseCreateUser(email, password)
        } catch(e: Exception) {
            handleError(e)
        }
    }

    private fun handleError(e: Exception) {
        showToast(e.message.toString())
        Log.e(TAG, e.message.toString())
    }

    private fun getEncryptedCredentials(): Map<String, String> {
        val email = cryptoManager.decrypt(sp.getString("email", "default#Email").toString())
        val password = cryptoManager.decrypt(sp.getString("password", "default#Password").toString())

        return mapOf(
            "email" to email,
            "password" to password
        )
    }

    private fun firebaseCreateUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    finishRegistration()
                }
            }
            .addOnFailureListener { e ->
                handleError(e)
            }
    }

    private fun finishRegistration() {
        val uid = auth.currentUser?.uid.toString()
        insertUser(uid)

        Log.d(TAG, "createUserWithEmail:success")
        showHomeScreen()
    }

    private fun insertUser(uid: String) {
        try {
            val user: User = createUser(uid)
            db.collection("users").add(user)
            Log.d(TAG, "userInsert:success")
        } catch(e: Exception) {
            handleError(e)
        }
    }

    private fun createUser(uid: String): User {
        try {
            return User(
                uid,
                sp.getString("userName", "default#UserName").toString(),
                sp.getString("leagueId", "default#LeagueId").toString(),
                sp.getString("discordId", "default#DiscordId").toString(),
                ArrayList(),
                emptyMap()
            )
        } catch(e: Exception) {
            throw UserCreationException()
        }
    }

    fun prevFragment() {
        supportFragmentManager.popBackStack()
    }

    private fun showHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }
}