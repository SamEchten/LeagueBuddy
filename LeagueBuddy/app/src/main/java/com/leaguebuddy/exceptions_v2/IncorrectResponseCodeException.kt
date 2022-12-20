package com.leaguebuddy.exceptions_v2

class IncorrectResponseCodeException(message: String, statusCode: Int) : Exception(message) {
    val statusCode = statusCode
}
