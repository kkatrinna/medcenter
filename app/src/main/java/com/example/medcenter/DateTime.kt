package com.example.medcenter


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_time)

        weekTextView = findViewById(R.id.weekTextView)
        appointmentsLayout = findViewById(R.id.appointmentsLayout)

        val selectedDateMilli = intent.getLongExtra("SELECTED_DATE", 0L)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDateMilli

        displayWeek(calendar)
        addAppointmentCardsForWeek(calendar.time)
    }

    private fun displayWeek(calendar: Calendar) {
        val weekDays = StringBuilder()
        val dayFormatter = SimpleDateFormat("EE dd ", Locale("ru"))

        for (i in 0 until 7) {
            weekDays.append(dayFormatter.format(calendar.time)).append(" ")
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        weekTextView.text = weekDays.toString()
    }

    private fun saveAvailableTimes(date: String) {
        val availableTimes = (9..19).map { hour ->
            String.format("%02d:00", hour) // Генерация времени по типу 9:00, 10:00 и т.д.
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
    private fun populateTimeButtons(availableTimes: List<String>, cardView: View) {
        val buttonContainer = findViewById<GridLayout>(R.id.buttonContainer)
        buttonContainer.removeAllViews()
        availableTimes.forEach { time ->
            val button = Button(this)
            button.text = time
            button.setBackgroundColor(ContextCompat.getColor(this@DateTime, R.color.pink))
            button.setTextColor(ContextCompat.getColor(this@DateTime, R.color.white))
            button.layoutParams = GridLayout.LayoutParams().apply {
                setMargins(8, 8, 8, 8)
                width = 150 // Динамическая ширина
                height = 100
            }

            button.setOnClickListener {
                // Обработка нажатия кнопки для выбора времени
                Toast.makeText(this, "Выбрано время: $time", Toast.LENGTH_SHORT).show()
                // Здесь можно сохранить выбранное время в Firestore или выполнить другие действия
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

        appointmentsLayout.addView(cardView) // Добавляем карточку в layout
    }
    private fun addAppointmentCardsForWeek(startDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        for (i in 0 until 7) {
            addAppointmentCard(calendar.time) // Добавляем карточку для текущей даты
            calendar.add(Calendar.DAY_OF_YEAR, 1) // Переходим к следующему дню
        }
    }

}


