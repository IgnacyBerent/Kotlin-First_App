package com.example.mad_l3.firestore
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

data class GameData(
    var id: String = FirebaseDatabase.getInstance().getReference("games").push().key ?: "",
    var selNumb: List<Int>?=null,
    var drawNumb: List<Int>?=null,
    var win: Double = 0.0,
    var date: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
)