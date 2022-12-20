package com.leaguebuddy.exceptions

class InvalidUserNameException() : Exception("Given username is not valid. Username cannot contain the character # and cannot be longer then 16 characters.") {}
