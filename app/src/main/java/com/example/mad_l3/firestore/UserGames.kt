package com.example.mad_l3.firestore

data class UserGames (
    var userid: String = "",
    var gamesId: List<String>?=null
)