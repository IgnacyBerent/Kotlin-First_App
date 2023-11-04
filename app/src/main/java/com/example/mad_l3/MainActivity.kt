package com.example.mad_l3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Switch
import androidx.core.view.get
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameinput = findViewById<EditText>(R.id.NameInput)
        val emailinput = findViewById<EditText>(R.id.EmailInput)
        val passwordinput = findViewById<EditText>(R.id.PasswordInput)
        val switch_activity_button = findViewById<Button>(R.id.SubmitButton)
        switch_activity_button.isEnabled = false
        val switcheryesno = findViewById<Switch>(R.id.switchyesno)

        switcheryesno.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // cheks if all inputs are at least 6 characters long
                if (nameinput.text.length >= 6 &&
                    emailinput.text.length >= 6 &&
                    passwordinput.text.length >= 6) {
                switcheryesno.text = "Yes"
                switch_activity_button.isEnabled = true
                } else {
                    switcheryesno.text = "Yes"
                }

            } else {
                switcheryesno.text = "No"
                switch_activity_button.isEnabled = false
            }
        }

        switch_activity_button.setOnClickListener() {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("NAME", nameinput.text.toString())
            intent.putExtra("EMAIL", emailinput.text.toString())
            intent.putExtra("PASSWORD", passwordinput.text.toString())
            startActivity(intent)

        }

    }
}