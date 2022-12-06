package com.leaguebuddy.fragments.session

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.leaguebuddy.SessionActivity

abstract class FormFragment: Fragment() {
    protected lateinit var sessionActivity: SessionActivity

    protected fun storeForm(form: Map<String, String>) {
        val sharedPreferences: SharedPreferences = sessionActivity.getSharedPreferences("registrationForm", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        form.forEach{entry ->
            editor.putString(entry.key, entry.value)
        }
        editor.commit()
    }

    //protected abstract fun validateForm()
}