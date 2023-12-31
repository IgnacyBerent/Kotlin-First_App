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
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.example.mad_l3.project_functions.SnackbarHelper.showErrorSnackBar
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootView = findViewById(android.R.id.content)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        val emailInput = findViewById<EditText>(R.id.EmailInput)
        val passwordInput = findViewById<EditText>(R.id.PasswordInput)
        val loginButton = findViewById<Button>(R.id.SubmitButton)

        val registerLink = findViewById<TextView>(R.id.registerLink)
        val spannableString = SpannableString("Don't have an account? Register")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Start RegisterActivity
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
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

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            loginUser(email, password)
        }

    }

    private fun validateLoginDetails(email: String, password: String): Boolean {
        return when {
            // email must not be empty
            email.isEmpty() -> {
                showErrorSnackBar(rootView,"Please enter your email address.")
                false
            }
            // password must not be empty
            password.isEmpty() -> {
                showErrorSnackBar(rootView,"Please enter your password.")
                false
            }

            else -> {
                true
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        if (validateLoginDetails(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showErrorSnackBar(rootView, "Login successful.", "green")
                        val intent = Intent(this, ChooseNumbersActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showErrorSnackBar(rootView, "Login failed.")
                    }
                }
        }
    }
}