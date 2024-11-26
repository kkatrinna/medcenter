package com.example.medcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private val db = Firebase.firestore
private lateinit var dateEditText: EditText
private lateinit var phoneEditText: EditText
private lateinit var emailEditText: EditText

class Cabinet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cabinet)

        val buttonPriem: Button = findViewById(R.id.button_priem)
        buttonPriem.setOnClickListener {
            startActivity(Intent(this, MainPriem::class.java))
        }

        val buttonAnaliz: Button = findViewById(R.id.button_analiz)
        buttonAnaliz.setOnClickListener {
            startActivity(Intent(this, MainZap::class.java))
        }
        dateEditText = findViewById(R.id.editTextDate)
        phoneEditText = findViewById(R.id.editTextPhone)
        emailEditText = findViewById(R.id.editTextTextEmailAddress)

        fetchUserData()
    }
    private fun fetchUserData() {
        val userDocument = db.collection("users").document("yourUserId")
        userDocument.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    dateEditText.setText(document.getString("date"))
                    phoneEditText.setText(document.getString("phone"))
                    emailEditText.setText(document.getString("email"))
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Ошибка загрузки данных: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun saveUserData() {
        val userData = hashMapOf(
            "date" to dateEditText.text.toString(),
            "phone" to phoneEditText.text.toString(),
            "email" to emailEditText.text.toString()
        )

        val userDocument = db.collection("users").document("yourUserId")
        userDocument.set(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "Данные успешно сохранены!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Ошибка сохранения данных: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onPause() {
        super.onPause()
        saveUserData()
    }
}