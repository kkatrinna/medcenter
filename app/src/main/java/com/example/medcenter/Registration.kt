package com.example.medcenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var auth: FirebaseAuth
private lateinit var db: FirebaseFirestore
private lateinit var userEmail: EditText
private lateinit var userPassword: EditText
private lateinit var userConfirmPassword: EditText
private lateinit var registerButton: Button

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        userEmail = findViewById(R.id.user_email)
        userPassword = findViewById(R.id.user_pass)
        userConfirmPassword = findViewById(R.id.user_twopass)
        registerButton = findViewById(R.id.button_reg)

        registerButton.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val email = userEmail.text.toString()
        val password = userPassword.text.toString()
        val confirmPassword = userConfirmPassword.text.toString()

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    saveUserToFirestore(email)
                } else {
                    Toast.makeText(this, "Ошибка: ${task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
        private fun saveUserToFirestore(email: String) {
            val userData = hashMapOf(
                "email" to email
            )
            val userId = auth.currentUser?.uid

            if (userId != null) {
                db.collection("users").document(userId)
                    .set(userData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Данные пользователя успешно сохранены!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Ошибка сохранения: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    fun auth(view: View) {
        startActivity(Intent(applicationContext, Authorization::class.java))
    }
}