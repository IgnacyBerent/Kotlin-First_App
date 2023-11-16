package com.example.mad_l3

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

        val loginLink = findViewById<TextView>(R.id.loginLink)
        val spannableString = SpannableString("Already have an account? Log in")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Start LoginActivity
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                startActivity(intent)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickableSpan, 25, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 25, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        loginLink.text = spannableString
        loginLink.movementMethod = LinkMovementMethod.getInstance()

        switch_activity_button.setOnClickListener() {
            val intent = Intent(this, SecondActivity::class.java)
            // place to sendinformation to fire base and login user
            startActivity(intent)

        }

    }
}