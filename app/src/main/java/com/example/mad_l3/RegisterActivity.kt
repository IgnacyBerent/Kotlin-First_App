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
import com.example.mad_l3.firestore.FireStoreClass
import com.example.mad_l3.firestore.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameinput = findViewById<EditText>(R.id.NameInput)
        val emailinput = findViewById<EditText>(R.id.EmailInput)
        val passwordinput = findViewById<EditText>(R.id.PasswordInput)
        val registerButton = findViewById<Button>(R.id.SubmitButton)
        registerButton.isEnabled = false
        val switcheryesno = findViewById<Switch>(R.id.switchyesno)

        switcheryesno.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switcheryesno.text = "Yes"
                registerButton.isEnabled = true
            } else {
                switcheryesno.text = "No"
                registerButton.isEnabled = false
            }
        }

        val loginLink = findViewById<TextView>(R.id.loginLink)
        val spannableString = SpannableString("Already have an account? Log in")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Goes to Login Activity
                goToLogin(widget)
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

        registerButton.setOnClickListener() {

            // place to sendinformation to fire base and login user
            val userName = nameinput.text.toString()
            val userLogin = emailinput.text.toString()
            val userPassword = passwordinput.text.toString()

            registerUser(userName, userLogin, userPassword)

            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)

        }

    }

    fun goToLogin(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showErrorSnackBar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    private fun validateRegisterDetails(name: String, email: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                showErrorSnackBar("Please enter a name")
                false
            }
            // name must be at least 2 characters long
            name.length < 2 -> {
                showErrorSnackBar("Name must be at least 2 characters long")
                false
            }
            email.isEmpty() -> {
                showErrorSnackBar("Please enter an email address")
                false
            }
            // email must be in a valid format
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showErrorSnackBar("Please enter a valid email address")
                false
            }
            password.isEmpty() -> {
                showErrorSnackBar("Please enter a password")
                false
            }
            // password must be at least 8 characters long
            password.length < 8 -> {
                showErrorSnackBar("Password must be at least 8 characters long")
                false
            }
            // password must contain at least one digit
            !password.any { it.isDigit() } -> {
                showErrorSnackBar("Password must contain at least one digit")
                false
            }
            // password must contain at least one letter
            !password.any { it.isLetter() } -> {
                showErrorSnackBar("Password must contain at least one letter")
                false
            }
            // password must contain at least one special character
            !password.any { !it.isLetterOrDigit() } -> {
                showErrorSnackBar("Password must contain at least one special character")
                false
            }
            // password must contain at least one uppercase letter
            !password.any { it.isUpperCase() } -> {
                showErrorSnackBar("Password must contain at least one uppercase letter")
                false
            }
            // password must contain at least one lowercase letter
            !password.any { it.isLowerCase() } -> {
                showErrorSnackBar("Password must contain at least one lowercase letter")
                false
            }
            // password must not contain whitespaces
            password.any { it.isWhitespace() } -> {
                showErrorSnackBar("Password must not contain whitespaces")
                false
            }
            // password must not contain non-ASCII characters
            !password.all { it.isLetterOrDigit() } -> {
                showErrorSnackBar("Password must not contain non-ASCII characters")
                false
            }

            else -> {
                true

            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        if (validateRegisterDetails(name, email, password)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        // If the registration is successfully done
                        if (task.isSuccessful) {
                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            showErrorSnackBar("You are registered successfully. Your user id is ${firebaseUser.uid}")
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!
                            val user = User(
                                "Testowe ID",
                                name,
                                true,
                                email,
                            )
                            FireStoreClass().registerUserFS(this, user)
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString())
                        }
                    })
        }
    }

    fun  userRegistrationSuccess(){
        Toast.makeText(this@RegisterActivity, resources.getString(),
            Toast.LENGTH_LONG).show()
    }



}