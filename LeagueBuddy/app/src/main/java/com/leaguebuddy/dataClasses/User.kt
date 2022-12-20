package com.leaguebuddy.dataClasses

data class User(
    val uid: String,
    val userName: String,
    val leagueId: String,
    val discordId: String,
    val friends: ArrayList<Friend>,
    val friendRequests: Map<String, String>
)