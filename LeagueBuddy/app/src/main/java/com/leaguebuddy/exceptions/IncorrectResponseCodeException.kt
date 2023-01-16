package com.leaguebuddy.exceptions

class IncorrectResponseCodeException(message: String, statusCode: Int) : Exception(message) {
    val statusCode = statusCode
}
