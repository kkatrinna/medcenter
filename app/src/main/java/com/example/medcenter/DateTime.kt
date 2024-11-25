package com.example.medcenter


import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val db = Firebase.firestore

class DateTime : AppCompatActivity() {
    private lateinit var weekTextView: TextView
    private lateinit var appointmentsLayout: LinearLayout
    private var doctorName: String = "" // Variable to hold doctor's name
    private var appointmentCost: Long = 0 // Variable to hold appointment cost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_time)

        weekTextView = findViewById(R.id.weekTextView)
        appointmentsLayout = findViewById(R.id.appointmentsLayout)

        // Get the selected date and doctor's details from the intent
        val selectedDateMilli = intent.getLongExtra("SELECTED_DATE", 0L)
        doctorName = intent.getStringExtra("SELECTED_NAME") ?: ""
        appointmentCost = intent.getLongExtra("SELECTED_FEE", 0L)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDateMilli

        displayWeek(calendar)
        addAppointmentCardsForWeek(calendar.time)
    }

    // Display the week starting from the selected date
    private fun displayWeek(calendar: Calendar) {
        val weekDays = StringBuilder()
        val dayFormatter = SimpleDateFormat("EE dd ", Locale("ru"))
        for (i in 0 until 7) {
            weekDays.append(dayFormatter.format(calendar.time)).append(" ")
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        weekTextView.text = weekDays.toString()
    }

    // Save available appointment times to Firestore
    private fun saveAvailableTimes(date: String) {
        val availableTimes = (9..19).map { hour ->
            String.format("%02d:00", hour) // Generate time slots like 9:00, 10:00, etc.
        }

        val appointmentData = hashMapOf(
            "date" to date,
            "availableTimes" to availableTimes
        )

        db.collection("appointments").add(appointmentData)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Available times added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding available times", e)
            }
    }

    // Load available appointment times from Firestore
    private fun loadAvailableTimes(date: String, cardView: View) {
        db.collection("appointments")
            .whereEqualTo("date", date)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val availableTimes = document.get("availableTimes") as List<String>
                    populateTimeButtons(availableTimes, cardView)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting available times: ", exception)
            }
    }

    // Populate buttons for each available time slot
    private fun populateTimeButtons(availableTimes: List<String>, cardView: View) {
        val buttonContainer = cardView.findViewById<GridLayout>(R.id.buttonContainer)
        buttonContainer.removeAllViews()
        availableTimes.forEach { time ->
            val button = Button(this).apply {
                text = time
                setBackgroundColor(ContextCompat.getColor(this@DateTime, R.color.pink))
                setTextColor(ContextCompat.getColor(this@DateTime, R.color.white))
                layoutParams = GridLayout.LayoutParams().apply {
                    setMargins(8, 8, 8, 8)
                    width = 150
                    height = 100
                }
            }

            button.setOnClickListener {
                Toast.makeText(this, "Выбрано время: $time", Toast.LENGTH_SHORT).show()
                val confirmationIntent = Intent(this, Confirmation::class.java).apply {
                    putExtra("DOCTOR_NAME", intent.getStringExtra("DOCTOR_NAME"))
                    putExtra("DOCTOR_FEE", intent.getStringExtra("DOCTOR_FEE"))
                    putExtra("APPOINTMENT_DATE", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
                    putExtra("APPOINTMENT_TIME", time)
                }
                startActivity(confirmationIntent)
            }
            buttonContainer.addView(button)
        }
    }
    private fun addAppointmentCard(date: Date) {
        val cardView = layoutInflater.inflate(R.layout.week_card, appointmentsLayout, false)
        val dateTextView: TextView = cardView.findViewById(R.id.weekDayText)
        dateTextView.text = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("ru")).format(date)
        loadAvailableTimes(SimpleDateFormat("yyyy-MM-dd").format(date), cardView)
        saveAvailableTimes(SimpleDateFormat("yyyy-MM-dd").format(date))
        appointmentsLayout.addView(cardView)
    }

    // Add appointment cards for the entire week
    private fun addAppointmentCardsForWeek(startDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        for (i in 0 until 7) {
            addAppointmentCard(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
    }
}