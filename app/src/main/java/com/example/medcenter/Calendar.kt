package com.example.medcenter

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import java.util.Calendar
import java.util.Locale

class Calendar : AppCompatActivity() {
    private lateinit var calendarView1: CalendarView
    private lateinit var dateTextView: TextView
    private lateinit var buttonNext: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        Locale.setDefault(Locale("ru"))
        val config = resources.configuration
        config.setLocale(Locale("ru"))
        resources.updateConfiguration(config, resources.displayMetrics)

        calendarView1 = findViewById(R.id.calendarView1)
        dateTextView = findViewById(R.id.dateTextView)
        buttonNext = findViewById(R.id.buttonNext)

        val calendar = Calendar.getInstance()
        calendarView1.date = calendar.timeInMillis

        updateDateTextView(calendar)

        calendarView1.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            if (selectedDate.before(Calendar.getInstance())) {
                Toast.makeText(this, "Выбор размещён для прошедших дат недоступен", Toast.LENGTH_SHORT).show()
                calendarView1.date = System.currentTimeMillis()
            } else {
                updateDateTextView(selectedDate)
            }
        }

        buttonNext.setOnClickListener {
            val selectedDate = Calendar.getInstance()
            selectedDate.timeInMillis = calendarView1.date
            val intent = Intent(this, DateTime::class.java).apply {
                putExtra("SELECTED_DATE", selectedDate.timeInMillis)
                putExtra("DOCTOR_NAME", intent.getStringExtra("DOCTOR_NAME"))
                putExtra("DOCTOR_PHOTO", intent.getStringExtra("DOCTOR_PHOTO"))
                putExtra("DOCTOR_FEE", intent.getStringExtra("DOCTOR_FEE"))
            }
            startActivity(intent)
        }

    }

    private fun updateDateTextView(calendar: Calendar) {
        val formattedDate = "Выбранная дата: ${calendar.get(Calendar.DAY_OF_MONTH)}.${calendar.get(Calendar.MONTH) + 1}.${calendar.get(Calendar.YEAR)}"
        dateTextView.text = formattedDate
    }
}