package com.megh.fixit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        // 1. Setup Profile Data
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvInitials = findViewById<TextView>(R.id.tvInitials)

        if (user != null) {
            tvName.text = user.displayName ?: "FixIt User"
            tvEmail.text = user.email

            // Get first letter of name for avatar
            val name = user.displayName ?: "User"
            if (name.isNotEmpty()) {
                tvInitials.text = name.first().toString().uppercase()
            }
        }

        // 2. Back Button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // 3. About Button
        findViewById<TextView>(R.id.btnAbout).setOnClickListener {
            Toast.makeText(this, "FixIt v1.0\nDeveloped by Megh", Toast.LENGTH_LONG).show()
        }

        // 4. Logout Logic
        findViewById<TextView>(R.id.btnLogout).setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}