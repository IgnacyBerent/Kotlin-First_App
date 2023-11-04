package com.example.mad_l3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.NumberPicker

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val intent = intent
        val user_name = intent.getStringExtra("NAME")
        val user_email = intent.getStringExtra("EMAIL")
        val user_password = intent.getStringExtra("PASSWORD")

        val selected_numbers_text = findViewById<TextView>(R.id.NumbersToShow)
        val select_button = findViewById<Button>(R.id.SelectButton)
        val numberPicker = findViewById<NumberPicker>(R.id.picker)
        val switch_activity_button = findViewById<Button>(R.id.SubmitButton)
        switch_activity_button.isEnabled = false

        numberPicker.minValue = 0  // Minimum value
        numberPicker.maxValue = 49 // Maximum value

        val welcome_text = findViewById<TextView>(R.id.welcome)
        welcome_text.text = "Hello $user_name. Let's pick your lucky numbers!"

        val selectedNumbers = mutableListOf<Int>()

        // picking 6 numbers mechanism
        select_button.setOnClickListener() {
            val number = numberPicker.value
            if (!selectedNumbers.contains(number)) {
                selectedNumbers.add(number)
                selected_numbers_text.text = selectedNumbers.joinToString("   ")
            }
            if (selectedNumbers.size >= 6) {
                select_button.isEnabled = false
                switch_activity_button.isEnabled = true
            }
        }
        switch_activity_button.setOnClickListener() {
            val intent2 = Intent(this, ThirdActivity::class.java)
            intent2.putExtra("NAME", user_name)
            intent2.putExtra("EMAIL", user_email)
            intent2.putExtra("PASSWORD", user_password)
            intent2.putExtra("NUMBERS", selectedNumbers.joinToString("   "))
            startActivity(intent2)
        }
    }
}