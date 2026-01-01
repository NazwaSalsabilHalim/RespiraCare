package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.respiracare.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Set icon untuk email dan password ---
        val sizeInDp = 24
        val sizeInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            sizeInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        val emailIcon = ContextCompat.getDrawable(this, R.drawable.mail)
        emailIcon?.setBounds(0, 0, sizeInPx, sizeInPx)
        binding.email.setCompoundDrawables(emailIcon, null, null, null)

        val passwordIcon = ContextCompat.getDrawable(this, R.drawable.padlock)
        passwordIcon?.setBounds(0, 0, sizeInPx, sizeInPx)
        binding.password.setCompoundDrawables(passwordIcon, null, null, null)

        // --- Isi email otomatis dari Register ---
        val prefillEmail = intent.getStringExtra("email")
        if (!prefillEmail.isNullOrEmpty()) {
            binding.email.setText(prefillEmail)
        }

        // --- Tombol Login ---
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

            // TODO: nanti bisa tambahkan FirebaseAuth login di sini
            // Untuk sekarang langsung ke Home
            startActivity(Intent(this, Home::class.java))
            finish()
        }
    }
}