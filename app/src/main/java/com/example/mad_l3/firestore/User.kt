package com.example.mad_l3.firestore

data class User(
    val id: String="",
    val name: String="",
    val registeredUser: Boolean = false,
    val email: String="",
)
