package com.leaguebuddy.fragments.session

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.leaguebuddy.SessionActivity

abstract class FormFragment: Fragment() {
    protected lateinit var sessionActivity: SessionActivity
    protected val TAG = "REGISTRATION"

    protected fun storeForm(form: Map<String, String>) {
        val sharedPreferences: SharedPreferences = sessionActivity.getSharedPreferences("registrationForm", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        form.forEach{entry ->
            editor.putString(entry.key, entry.value)
        }
        editor.commit()
    }

    protected fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    protected fun filterInput(input: String): String {
        val pattern = "[^A-Za-z0-9]".toRegex()
        return pattern.replace(input, "")
    }
}