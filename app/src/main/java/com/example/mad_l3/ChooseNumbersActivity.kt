package com.example.mad_l3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.NumberPicker
import com.example.mad_l3.firestore.FireStoreClass
import com.example.mad_l3.project_functions.SnackbarHelper.showErrorSnackBar
import com.example.mad_l3.firestore.GameData
import com.google.firebase.auth.FirebaseAuth
import com.example.mad_l3.firestore.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import android.util.Log


class ChooseNumbersActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootView = findViewById(android.R.id.content)
        setContentView(R.layout.activity_second)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val currentUserUid = auth.currentUser?.uid
        val selectedNumbersText = findViewById<TextView>(R.id.NumbersToShow)
        val selectButton = findViewById<Button>(R.id.SelectButton)
        val numberPicker = findViewById<NumberPicker>(R.id.picker)
        val switchActivityButton = findViewById<Button>(R.id.SubmitButton)
        switchActivityButton.isEnabled = false
        val welcomeText = findViewById<TextView>(R.id.welcome)
        numberPicker.minValue = 0
        numberPicker.maxValue = 49

        db.collection("users").document(currentUserUid!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject<User>()
                    welcomeText.text = "Welcome ${user?.name}, \n please select your Lucky numbers!"
                    Log.i("ChooseNumbersActivity", "User data retrieved successfully")
                } else {
                    Log.e("ChooseNumbersActivity", "No such document")
                }
            }
            .addOnFailureListener {
                Log.e("ChooseNumbersActivity", "Error while getting user data")
            }

        val selectedNumbers = mutableListOf<Int>()
        var newGameId: String? = null

        // picking 6 numbers mechanism
        selectButton.setOnClickListener {
            val number = numberPicker.value
            if (!selectedNumbers.contains(number)) {
                selectedNumbers.add(number)
                selectedNumbersText.text = selectedNumbers.joinToString("   ")
            } else {
                showErrorSnackBar(rootView, "You have already picked this number!")
            }
            if (selectedNumbers.size >= 6) {
                selectButton.isEnabled = false
                switchActivityButton.isEnabled = true
                showErrorSnackBar(
                    rootView,
                    "You have picked 6 numbers. You can now start lottery!",
                    "green"
                )

                val newGame = GameData(
                    selNumb =  selectedNumbers,
                    drawNumb =  null,
                    win = 0.0
                )
                newGameId = newGame.id
                FireStoreClass().registerNewGame(newGame, currentUserUid)
            }
        }
        switchActivityButton.setOnClickListener{
            val intent = Intent(this, DrawNumbersActivity::class.java)
            intent.putExtra("newGameId", newGameId)
            startActivity(intent)
            finish()
        }
    }
}