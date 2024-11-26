package com.example.medcenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private val db = Firebase.firestore
class Confirmation : AppCompatActivity() {
    private lateinit var doctorNameTextView: TextView
    private lateinit var doctorImageView: ImageView
    private lateinit var appointmentDateTextView: TextView
    private lateinit var appointmentTimeTextView: TextView
    private lateinit var appointmentCostTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        doctorNameTextView = findViewById(R.id.doctorNameTextView)
        doctorImageView = findViewById(R.id.doctorImageView)
        appointmentDateTextView = findViewById(R.id.appointmentDateTextView)
        appointmentTimeTextView = findViewById(R.id.appointmentTimeTextView)
        appointmentCostTextView = findViewById(R.id.appointmentCostTextView)

        val doctorName = intent.getStringExtra("DOCTOR_NAME")
        val photoUrl = intent.getStringExtra("DOCTOR_PHOTO")
        val appointmentDate = intent.getStringExtra("APPOINTMENT_DATE")
        val appointmentTime = intent.getStringExtra("APPOINTMENT_TIME")
        val appointmentCost = intent.getStringExtra("DOCTOR_FEE")

        doctorNameTextView.text = doctorName
        appointmentDateTextView.text = appointmentDate
        appointmentTimeTextView.text = appointmentTime
        appointmentCostTextView.text = appointmentCost


        Glide.with(this)
            .load(photoUrl)
            .error(R.drawable.photo)
            .into(doctorImageView)

        findViewById<ImageView>(R.id.button_back).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.confirmButton).setOnClickListener {
            confirmAppointment(doctorName, appointmentDate, appointmentTime, appointmentCost)
            val intent = Intent(this, MainPriem::class.java).apply {
                putExtra("DOCTOR_NAME", doctorName)
                putExtra("APPOINTMENT_DATE", appointmentDate)
                putExtra("DOCTOR_PHOTO", photoUrl)
                putExtra("APPOINTMENT_TIME", appointmentTime)
                putExtra("DOCTOR_FEE", appointmentCost)
            }
            startActivity(intent)
        }
    }
    private fun confirmAppointment(doctorName: String?, date: String?, time: String?, cost: String?)
    {
        val appointmentData = hashMapOf(
            "doctorName" to doctorName,
            "date" to date,
            "time" to time,
            "cost" to cost
        )
        db.collection("appointments")
            .add(appointmentData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Запись успешно подтверждена!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Ошибка при сохранении: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}