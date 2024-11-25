package com.example.medcenter

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

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
        val doctorPhotoUrl = intent.getStringExtra("DOCTOR_PHOTO")
        val appointmentDate = intent.getStringExtra("APPOINTMENT_DATE")
        val appointmentTime = intent.getStringExtra("APPOINTMENT_TIME")
        val appointmentCost = intent.getStringExtra("DOCTOR_FEE")

        doctorNameTextView.text = doctorName
        appointmentDateTextView.text = appointmentDate
        appointmentTimeTextView.text = appointmentTime
        appointmentCostTextView.text = appointmentCost


        findViewById<Button>(R.id.confirmButton).setOnClickListener {
            confirmAppointment(doctorName, appointmentDate, appointmentTime, appointmentCost)
        }

        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
        }
    }

    private fun confirmAppointment(doctorName: String?, date: String?, time: String?, cost: String?) {
        // Логика для подтверждения назначения
        Toast.makeText(this, "Запись подтверждена: $doctorName на $date, время: $time, стоимость: $cost", Toast.LENGTH_SHORT).show()
        // Дополнительные действия: сохранение в Firestore и т.д.
    }
}