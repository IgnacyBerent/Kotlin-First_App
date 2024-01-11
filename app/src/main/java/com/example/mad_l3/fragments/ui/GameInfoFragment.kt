package com.example.mad_l3.fragments.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.mad_l3.R
import com.example.mad_l3.firestore.FireStoreClass
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class GameInfoFragment : Fragment() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.game_info_fragment, container, false)
        val gameDate = arguments?.getString("gameDate")
        val gameSelNumbers = arguments?.getString("gameSelNumbers")
        val gameDrawnNumbers = arguments?.getString("gameDrawnNumbers")
        val gameWin = arguments?.getString("gameWin")
        val backButton = rootView.findViewById<Button>(R.id.backButton)

        val gameDateTextView = rootView.findViewById<TextView>(R.id.gameDateTextView)
        val gameSelNumbersTextView = rootView.findViewById<TextView>(R.id.gameSelNumbersTextView)
        val gameDrawnNumbersTextView = rootView.findViewById<TextView>(R.id.gameDrawnNumbersTextView)
        val gameWinTextView = rootView.findViewById<TextView>(R.id.gameWinTextView)

        // Set the text for the TextViews
        gameDateTextView.text = "Game Date: $gameDate"
        gameSelNumbersTextView.text = "Selected Numbers: $gameSelNumbers"
        gameDrawnNumbersTextView.text = "Drawn Numbers: $gameDrawnNumbers"
        gameWinTextView.text = "Win: $gameWin"

        backButton.setOnClickListener {
            val activity: FragmentActivity? = activity
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        return rootView
    }
}