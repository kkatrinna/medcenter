package com.example.medcenter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medcenter.Cabinet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Authorization : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var authButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        userEmail = findViewById(R.id.user_email)
        userPassword = findViewById(R.id.password)
        authButton = findViewById(R.id.button_auth)

        authButton.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = userEmail.text.toString()
        val password = userPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val userData = hashMapOf(
                        "email" to email
                    )

                    db.collection("users").document(userId!!)
                        .set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Авторизация успешна!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, Cabinet::class.java))
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Ошибка: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }
    fun reg(view: View) {
        startActivity(Intent(applicationContext, Registration::class.java))
    }
}