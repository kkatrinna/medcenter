package com.example.medcenter

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.util.Calendar
import java.util.Locale

class DateTime : AppCompatActivity() {
    private lateinit var weekTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_time)

        weekTextView = findViewById(R.id.weekTextView)

        // Получаем переданную дату
        val selectedDateMilli = intent.getLongExtra("SELECTED_DATE", 0L)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDateMilli

        displayWeek(calendar)
    }

    private fun displayWeek(calendar: Calendar) {
        val weekDays = StringBuilder()
        val dayFormatter = SimpleDateFormat("EE dd  ", Locale("ru"))

        for (i in 0 until 7) {
            weekDays.append(dayFormatter.format(calendar.time))
            if (i < 6) weekDays.append(" ") // Добавляем перенос строки между днями
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        weekTextView.text = weekDays.toString()
    }
}