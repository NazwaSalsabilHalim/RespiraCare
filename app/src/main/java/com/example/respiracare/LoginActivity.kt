package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.respiracare.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Prefill email dari Register
        val prefillEmail = intent.getStringExtra("email")
        if (!prefillEmail.isNullOrEmpty()) {
            binding.email.setText(prefillEmail)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔥 LOGIN KE FIREBASE
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    startActivity(Intent(this, Home::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Login gagal. Cek email / password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }
}