package com.example.mad_l3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val intent = intent
        // Mapa łącząca klucze intentu z identyfikatorami widoków
        val mappings = mapOf(
            "name" to R.id.name,
            "email" to R.id.email,
            "password" to R.id.password,
            "numbers" to R.id.numbers
        )

        // Iterowanie przez mapę
        for ((key, viewId) in mappings) {
            val value = intent.getStringExtra(key.uppercase())
            val textView = findViewById<TextView>(viewId)
            textView.text = "Your ${key}: $value"
        }
    }
}