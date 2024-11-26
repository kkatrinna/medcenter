package com.example.medcenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Specialization : AppCompatActivity() {
    private lateinit var specializationLayout: LinearLayout
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specialization)

        specializationLayout = findViewById(R.id.specializationLayout)
        addInitialSpecializations()
        loadSpecializations()
    }

    private fun loadSpecializations() {
        val uniqueSpecializations = mutableSetOf<String>()

        db.collection("specializations")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val specializationName = document.getString("name") ?: continue

                    if (uniqueSpecializations.add(specializationName)) {
                        addButton(specializationName)
                    }
                }
                specializationLayout.requestLayout()
            }
            .addOnFailureListener { exception ->
                Log.e("Specialization", "Error getting documents: ", exception)
            }
    }

    private fun addButton(specializationName: String) {
        val button = Button(this).apply {
            text = specializationName
            layoutParams = GridLayout.LayoutParams().apply {
                setMargins(10, 10, 10, 10)
                width = 800
                height = 140
            }

            setBackgroundColor(ContextCompat.getColor(this@Specialization, R.color.white))
            setOnClickListener {
                val intent = Intent(this@Specialization, Doctors::class.java).apply {
                    putExtra("specializationName", specializationName)
                }
                startActivity(intent)
            }
        }

        specializationLayout.addView(button)
    }


    private fun addInitialSpecializations() {
        db.collection("specializations")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    val adultspecializations = listOf(
                        "Терапевт", "Педиатр", "Невролог", "Хирург",
                        "Стоматолог", "Травматолог", "Цефалголог",
                        "Эндокринолог", "Офтальмолог", "Психиатр", "Психолог"
                    )
                    for (specialization in adultspecializations) {
                        addSpecialization(specialization)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Specialization", "Error checking specializations", exception)
            }
    }

    private fun addSpecialization(specializationName: String) {
        val specializationData = hashMapOf(
            "name" to specializationName
        )

        db.collection("specializations")
            .add(specializationData)
            .addOnSuccessListener { documentReference ->
                Log.d("Specialization", "Specialization added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Specialization", "Error adding specialization", e)
            }
    }
}