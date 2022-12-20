package com.leaguebuddy.exceptions

class InvalidCipherTextException(): Exception("Unable to decrypt ciphertext. Given ciphertext is not the right format.") {
}