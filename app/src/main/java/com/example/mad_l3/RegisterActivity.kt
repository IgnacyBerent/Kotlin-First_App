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
import com.example.mad_l3.project_functions.SnackbarHelper.showErrorSnackBar
import com.google.firebase.firestore.FirebaseFirestore


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootView = findViewById<View>(android.R.id.content)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val nameInput = findViewById<EditText>(R.id.NameInput)
        val emailInput = findViewById<EditText>(R.id.EmailInput)
        val passwordInput = findViewById<EditText>(R.id.PasswordInput)
        val repeatPasswordInput = findViewById<EditText>(R.id.RepeatPasswordInput)
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
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
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

        registerButton.setOnClickListener {

            // place to sendinformation to fire base and login user
            val userName = nameInput.text.toString()
            val userLogin = emailInput.text.toString()
            val userPassword = passwordInput.text.toString()
            val repeatedPassword = repeatPasswordInput.text.toString()

            registerUser(userName, userLogin, userPassword, repeatedPassword)
        }

    }


    private fun validateRegisterDetails(
        name: String,
        email: String,
        password: String,
        repeatedPassword: String):
            Boolean {

        return when {
            // password and repeated password must be the same
            password != repeatedPassword -> {
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

    private fun registerUser(name: String, email: String, password: String, repeatedPassword: String) {
        if (validateRegisterDetails(name, email, password, repeatedPassword)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        // If the registration is successfully done
                        if (task.isSuccessful) {
                            showErrorSnackBar(
                                rootView,
                                "Registration successful.",
                                "green"
                            )
                            val currentUserId = auth.currentUser?.uid.toString()
                            val user = User(currentUserId, name,true, email)
                            FireStoreClass().registerUserFS(user)
                            val intent = Intent(this, ChooseNumbersActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            showErrorSnackBar(rootView, "Registration failed. Please try again.")
                        }
                    })
        }
    }
}