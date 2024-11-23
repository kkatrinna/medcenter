package com.example.medcenter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Doctors : AppCompatActivity() {
    private lateinit var doctorsListLayout: LinearLayout
    private val db = Firebase.firestore
    private lateinit var selectedSpecialization: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors)
        selectedSpecialization = intent.getStringExtra("specializationName") ?: ""

        addSampleDoctors()
        loadDoctors()
    }

    private fun addDoctor(name: String, photoUrl: String, appointmentFee: Double, specialization: String) {
            val doctorData = hashMapOf(
                "name" to name,
                "photoUrl" to photoUrl,
                "appointmentFee" to appointmentFee,
                "specialization" to specialization // Добавляем специальность
            )

            db.collection("doctors")
                .add(doctorData)
                .addOnSuccessListener { documentReference ->
                    Log.d("Doctors", "Doctor added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("Doctors", "Error adding doctor", e)
                }
        }

    private fun addSampleDoctors() {
        // Добавьте данные о врачах
        addDoctor("Иван Иванов", R.drawable.photo.toString(), 1500.0, "Терапевт")
        addDoctor("Петр Петров", R.drawable.photo.toString(), 2000.0, "Хирург")
        addDoctor("Сидор Сидоров", R.drawable.photo.toString(), 1000.0, "Терапевт")
    }


    private fun loadDoctors() {
        val uniqueDoctors = mutableSetOf<String>()
        Log.d("Doctors", "Selected specialization: $selectedSpecialization")
        db.collection("doctors")
            .whereEqualTo("specialization", selectedSpecialization)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.d("Doctors", "No doctors found for specialization: $selectedSpecialization")
                }
                for (document in result) {
                    val name = document.getString("name") ?: continue
                    val photoUrl = document.getString("photoUrl") ?: continue
                    val appointmentFee = document.getDouble("appointmentFee") ?: continue

                    if (uniqueDoctors.add(name)) {
                        addDoctorCard(name, photoUrl, appointmentFee)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Doctors", "Error getting documents: ", exception)
            }
    }

    @SuppressLint("SetTextI18n")
    private fun addDoctorCard(name: String, photoUrl: String, appointmentFee: Double) {
        val cardView = layoutInflater.inflate(R.layout.doctor_card, doctorsListLayout, false)
        val doctorNameTextView: TextView = cardView.findViewById(R.id.doctorName)
        val doctorFeeTextView: TextView = cardView.findViewById(R.id.doctorFee)
        val doctorImageView: ImageView = cardView.findViewById(R.id.doctorImage)


        doctorNameTextView.text = name
        doctorFeeTextView.text = "Стоимость: $appointmentFee"
        Glide.with(this)
            .load(photoUrl)
            .error(R.drawable.photo)
            .into(doctorImageView)

        cardView.setOnClickListener {
            val intent = Intent(this, DoctorDetail::class.java).apply {
                putExtra("DOCTOR_NAME", name)
                putExtra("DOCTOR_PHOTO", photoUrl)
                putExtra("DOCTOR_FEE", appointmentFee)
            }
            startActivity(intent)
        }

        doctorsListLayout.addView(cardView)
    }

    fun exit(view: View) {
        setContentView(R.layout.activity_specialization)
        addSampleDoctors()
        loadDoctors()
    }
}
