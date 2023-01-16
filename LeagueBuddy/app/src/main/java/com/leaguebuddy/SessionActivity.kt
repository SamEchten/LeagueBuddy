package com.leaguebuddy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leaguebuddy.api.LeagueApiHelper
import com.leaguebuddy.dataClasses.Summoner
import com.leaguebuddy.dataClasses.User
import com.leaguebuddy.databinding.ActivityRegistrationBinding
import com.leaguebuddy.exceptions.*
import com.leaguebuddy.fragments.session.LoginFragment
import com.leaguebuddy.sql.SqlDbHelper
import kotlinx.coroutines.*

class SessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var sp: SharedPreferences
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val cryptoManager: CryptoManager = CryptoManager()
    private val TAG = "REGISTRATION"
    private val sqlDbHelper: SqlDbHelper = SqlDbHelper(this)
    private val leagueApiHelper: LeagueApiHelper = LeagueApiHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        sp = getSharedPreferences("registrationForm", Context.MODE_PRIVATE)
        setContentView(binding.root)
        supportActionBar?.hide()

        val sqlDbHelper = SqlDbHelper(this)
        sqlDbHelper.writableDatabase

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
            val user = auth.currentUser ?: return false
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

    fun login(email: String, password: String) {
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

    fun registerUser() {
        val userCredentials = getEncryptedCredentials()
        val email = userCredentials["email"]
        val password = userCredentials["password"]

        if(email == null || password == null) {
            throw Exception("Could not find email or password")
        }

        try {
            validateEmail(email)
            firebaseCreateUser(email, password)
        } catch(e: Exception) {
            handleError(e)
        }
    }

    private fun validateEmail(email: String) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw InvalidEmailException()
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

            GlobalScope.launch {
                try {
                    val summonerName = sp.getString("leagueId", "default#leagueId").toString()
                    val discordId = sp.getString("discordId", "default#discordId").toString()

                    val summonerNameDecrypted = cryptoManager.decrypt(summonerName)
                    val summoner: Summoner = leagueApiHelper.getSummonerInfo(summonerNameDecrypted)

                    sqlDbHelper.addUser(summonerName, summoner.id, discordId, 0)
                    Log.d(TAG, "userInsert:success")
                } catch(e: Exception) {
                    e.message?.let { Log.e("Error", it) }
                }
            }


        } catch(e: Exception) {
            handleError(e)
        }
    }

    private fun createUser(uid: String): User {
        try {
            val userName: String = filterInput(sp.getString("userName", "default#UserName").toString())

            return User(
                uid,
                userName,
                sp.getString("leagueId", "default#LeagueId").toString(),
                sp.getString("discordId", "default#DiscordId").toString(),
                ArrayList(),
                emptyMap()
            )
        } catch(e: Exception) {
            throw UserCreationException()
        }
    }

    private fun filterInput(input: String): String {
        val pattern = "[^A-Za-z0-9]".toRegex()
        return pattern.replace(input, "")
    }

    fun prevFragment() {
        supportFragmentManager.popBackStack()
    }

    fun showHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }
}