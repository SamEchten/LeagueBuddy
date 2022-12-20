package com.leaguebuddy.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.leaguebuddy.MainActivity
import com.leaguebuddy.R
import com.leaguebuddy.api.GameNewsApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var gameNewsApiHelper: GameNewsApiHelper
    private lateinit var topic : TextView
    private lateinit var mainActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameNewsApiHelper = GameNewsApiHelper()

        GlobalScope.launch { Dispatchers.Main
            try {
                val data = gameNewsApiHelper.getRecentGameNews()
                activity?.runOnUiThread(Runnable {

                })
            } catch (e: Exception){
                errorHandler(e)
            }
        }
    }

    private fun errorHandler(e: Exception){
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


}