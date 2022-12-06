package com.leaguebuddy.dataClasses

data class User(
    val userName: String,
    val email: String,
    val password: String,
    val friends: ArrayList<Friend>,
    val friendRequest: Map<String, String>
)