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
import android.widget.TextView
import android.widget.Switch
import androidx.core.view.get
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailinput = findViewById<EditText>(R.id.EmailInput)
        val passwordinput = findViewById<EditText>(R.id.PasswordInput)
        val switch_activity_button = findViewById<Button>(R.id.SubmitButton)

        val registerLink = findViewById<TextView>(R.id.registerLink)
        val spannableString = SpannableString("Don't have an account? Register")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Start RegisterActivity
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickableSpan, 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        registerLink.text = spannableString
        registerLink.movementMethod = LinkMovementMethod.getInstance()

        switch_activity_button.setOnClickListener() {
            if (emailinput.text.length >= 6 &&
                passwordinput.text.length >= 6)
            {
                val intent = Intent(this, SecondActivity::class.java)
                // place to sendinformation to fire base and login user
                startActivity(intent)
            }
            else {
                emailinput.error = "Email must be at least 6 characters long"
                passwordinput.error = "Password must be at least 6 characters long"
            }

        }

    }
}