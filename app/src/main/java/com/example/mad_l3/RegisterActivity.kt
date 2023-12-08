package com.example.mad_l3

import android.content.Intent
import android.graphics.Color
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
import com.example.mad_l3.projectfunctions.SnackbarHelper.showErrorSnackBar


class RegisterActivity : AppCompatActivity() {

    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        rootView = findViewById<View>(android.R.id.content)
        setContentView(R.layout.activity_register)

        val nameinput = findViewById<EditText>(R.id.NameInput)
        val emailinput = findViewById<EditText>(R.id.EmailInput)
        val passwordinput = findViewById<EditText>(R.id.PasswordInput)
        val repeatpasswordinput = findViewById<EditText>(R.id.RepeatPasswordInput)
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

        registerButton.setOnClickListener() {

            // place to sendinformation to fire base and login user
            val userName = nameinput.text.toString()
            val userLogin = emailinput.text.toString()
            val userPassword = passwordinput.text.toString()
            val repeatedPassword = repeatpasswordinput.text.toString()

            registerUser(userName, userLogin, userPassword, repeatedPassword)
        }

    }


    private fun validateRegisterDetails(
        name: String,
        email: String,
        password: String,
        repeated_password: String):
            Boolean {

        return when {
            // password and repeated password must be the same
            password != repeated_password -> {
                showErrorSnackBar(rootView,"Passwords do not match")
                false
            }
            // name must not be empty
            name.isEmpty() -> {
                showErrorSnackBar(rootView,"Please enter a name")
                false
            }
            // name must be at least 2 characters long
            name.length < 2 -> {
                showErrorSnackBar(rootView, "Name must be at least 2 characters long")
                false
            }
            // email must not be empty
            email.isEmpty() -> {
                showErrorSnackBar(rootView, "Please enter an email address")
                false
            }
            // email must be in a valid format
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showErrorSnackBar(rootView, "Please enter a valid email address")
                false
            }
            // password must not be empty
            password.isEmpty() -> {
                showErrorSnackBar(rootView, "Please enter a password")
                false
            }
            // password must be at least 8 characters long
            password.length < 8 -> {
                showErrorSnackBar(rootView, "Password must be at least 8 characters long")
                false
            }
            // password must contain at least one digit
            !password.any { it.isDigit() } -> {
                showErrorSnackBar(rootView, "Password must contain at least one digit")
                false
            }
            // password must contain at least one letter
            !password.any { it.isLetter() } -> {
                showErrorSnackBar(rootView, "Password must contain at least one letter")
                false
            }
            // password must contain at least one special character
            !password.any { !it.isLetterOrDigit() } -> {
                showErrorSnackBar(rootView, "Password must contain at least one special character")
                false
            }
            // password must contain at least one uppercase letter
            !password.any { it.isUpperCase() } -> {
                showErrorSnackBar(rootView, "Password must contain at least one uppercase letter")
                false
            }
            // password must contain at least one lowercase letter
            !password.any { it.isLowerCase() } -> {
                showErrorSnackBar(rootView, "Password must contain at least one lowercase letter")
                false
            }
            // password must not contain whitespaces
            password.any { it.isWhitespace() } -> {
                showErrorSnackBar(rootView, "Password must not contain whitespaces")
                false
            }

            else -> {
                true

            }
        }
    }

    private fun registerUser(name: String, email: String, password: String, repeated_password: String) {
        if (validateRegisterDetails(name, email, password, repeated_password)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        // If the registration is successfully done
                        if (task.isSuccessful) {
                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!
                            val user = User(
                                "Testowe ID",
                                name,
                                true,
                                email,
                            )
                            FireStoreClass().registerUserFS(this, user)
                            val intent = Intent(this, SecondActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            showErrorSnackBar(rootView, "Registration failed. Please try again.")
                        }
                    })
        }
    }
}