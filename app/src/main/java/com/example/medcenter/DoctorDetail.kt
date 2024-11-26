package com.example.medcenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private val db = Firebase.firestore

class DoctorDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_detail)
        val doctorName = intent.getStringExtra("DOCTOR_NAME")
        val photoUrl = intent.getStringExtra("DOCTOR_PHOTO")
        val appointmentFee = intent.getDoubleExtra("DOCTOR_FEE", 0.0)

        val nameTextView: TextView = findViewById(R.id.doctorDetailName)
        val feeTextView: TextView = findViewById(R.id.doctorDetailFee)
        val doctorImageView: ImageView = findViewById(R.id.doctorDetailImage)
        findViewById<ImageView>(R.id.button_back).setOnClickListener {
            finish()
        }
        nameTextView.text = doctorName
        feeTextView.text = "$appointmentFee"
        Glide.with(this)
            .load(photoUrl)
            .error(R.drawable.photo)
            .into(doctorImageView)


        findViewById<Button>(R.id.button_schedule).setOnClickListener {
            saveAppointment(doctorName, appointmentFee)
            val intent = Intent(this, Calendar::class.java).apply {
                putExtra("DOCTOR_NAME", doctorName)
                putExtra("DOCTOR_PHOTO", photoUrl)
                putExtra("DOCTOR_FEE", appointmentFee)
            }
            startActivity(intent)
        }
        addSampleLerning()
        addSampleWork()
        addSampleDiagnosis()
        loadWork()
        loadLerning()
        loadDiagnosis()

    }

    private fun saveAppointment(doctorName: String?, appointmentFee: Double) {
        val appointmentData = hashMapOf(
            "doctorName" to doctorName,
            "appointmentFee" to appointmentFee,
            "date" to System.currentTimeMillis()
        )

        db.collection("appointments")
            .add(appointmentData)
            .addOnSuccessListener { documentReference ->
                Log.d("DoctorDetail", "Appointment added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("DoctorDetail", "Error adding appointment", e)
            }
    }
    private fun addDiagnosis(name: String) {
        val doctorData = hashMapOf(
            "name" to name
        )
        db.collection("diagnosis")
            .add(doctorData)
            .addOnSuccessListener { documentReference ->
                Log.d("Diagnosis", "Diagnos added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Diagnosis", "Error adding diagnos", e)
            }
    }

    private fun addSampleDiagnosis() {
        addDiagnosis("Бронхит")
        addDiagnosis("Гастрит")
        addDiagnosis("Астма")
        addDiagnosis("Перелом")
        addDiagnosis("Мигрень")
        addDiagnosis("Пневмония")
        addDiagnosis("Отравление")
        addDiagnosis("Менингит")
        addDiagnosis("Стоматит")
        addDiagnosis("Остеохондроз")
        addDiagnosis("Анемия")
        addDiagnosis("ПРЛ")
    }

    private fun loadDiagnosis() {
        val specializationList = mutableListOf<String>()
        db.collection("diagnosis")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val diagnosis = document.getString("name")
                    if (diagnosis != null) {
                        specializationList.add(diagnosis)
                    }
                }
                setupSpecializationSpinner(specializationList)
            }
            .addOnFailureListener { exception ->
                Log.e("Specializations", "Error getting documents: ", exception)
            }
    }
    private fun setupSpecializationSpinner(specializations: List<String>) {
        val spinner: Spinner = findViewById(R.id.specializationSpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, specializations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

    }

    private fun addLerning(name: String) {
        val doctorData = hashMapOf(
            "name" to name
        )

        db.collection("lerning")
            .add(doctorData)
            .addOnSuccessListener { documentReference ->
                Log.d("Lerning", "Lerning added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Lerning", "Error adding lerning", e)
            }
    }

    private fun addSampleLerning() {
        addLerning("2018 - Государственный медицинский университет")
        addLerning("2010 - Государственный медицинский университет")
        addLerning("2001 - Государственный медицинский университет")
        addLerning("2020 - Государственный медицинский университет")
    }

    private fun loadLerning() {
        val lerningList = mutableListOf<String>()

        db.collection("lerning")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val lerning = document.getString("name")
                    if (lerning != null) {
                        lerningList.add(lerning)
                    }
                }
                setupLerningSpinner(lerningList)
            }
            .addOnFailureListener { exception ->
                Log.e("Specializations", "Error getting documents: ", exception)
            }
    }
    private fun setupLerningSpinner(lerning: List<String>) {
        val spinner: Spinner = findViewById(R.id.lerningSpinner)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lerning)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun addWork(name: String) {
        val doctorData = hashMapOf(
            "name" to name
        )

        db.collection("work")
            .add(doctorData)
            .addOnSuccessListener { documentReference ->
                Log.d("Work", "Work added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Work", "Error adding work", e)
            }
    }

    private fun addSampleWork() {
        addWork("Медицина")
        addWork("МедЭксперт")
    }

    private fun loadWork() {
        val workList = mutableListOf<String>()

        db.collection("work")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val work = document.getString("name")
                    if (work != null) {
                        workList.add(work)
                    }
                }
                setupWorkSpinner(workList)
            }
            .addOnFailureListener { exception ->
                Log.e("Work", "Error getting documents: ", exception)
            }
    }
    private fun setupWorkSpinner(work: List<String>) {
        val spinner: Spinner = findViewById(R.id.workSpinner)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, work)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}