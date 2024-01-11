package com.example.mad_l3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.example.mad_l3.firestore.FireStoreClass
import com.example.mad_l3.custom_elements.GameAdapter

class StatisticsActivity : AppCompatActivity() {

    private lateinit var fireStoreClass: FireStoreClass
    private lateinit var auth: FirebaseAuth
    val uid = auth.currentUser?.uid.toString()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val newRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        fireStoreClass = FireStoreClass()

        val currentUserId = auth.currentUser?.uid
        fireStoreClass.getGamesForUser(currentUserId!!) { games ->
            val gameAdapter = GameAdapter(uid, games)
            newRecyclerView.adapter = gameAdapter
        }
    }
}