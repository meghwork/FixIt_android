package com.megh.fixit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // UI References
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)
        val btnBack = findViewById<View>(R.id.btnBack) // Back Arrow

        // Back Navigation
        btnBack?.setOnClickListener { finish() }
        tvLogin.setOnClickListener { finish() }

        // Register Logic
        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // 1. Basic Validation
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Create User in Firebase Auth
            btnRegister.isEnabled = false // Prevent double clicks
            btnRegister.text = "Creating Account..."

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("Register", "createUser:success")
                        val user = auth.currentUser

                        // 3. Update Profile Name
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()

                        user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                            // 4. Save extra data to Firestore (Optional but recommended)
                            val userData = hashMapOf(
                                "uid" to user.uid,
                                "name" to name,
                                "email" to email
                            )

                            db.collection("users").document(user.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()
                                    // Go to Home
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    // Even if Firestore fails, we let them in
                                    Toast.makeText(this, "Welcome! (Database Error: ${e.message})", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        }
                    } else {
                        // 5. SHOW THE EXACT ERROR
                        btnRegister.isEnabled = true
                        btnRegister.text = "Sign Up"
                        Log.w("Register", "createUser:failure", task.exception)
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}