package com.example.medcenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private val db = Firebase.firestore

class MainPriem : AppCompatActivity() {
    data class Appointment(
        val doctorName: String? = null,
        val date: String? = null,
        val cost: String? = null,
        val photo: String? = null
    )
    class AppointmentAdapter(private val appointments: List<Appointment>) :
        RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

        class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textDoctorName: TextView = itemView.findViewById(R.id.textDoctorName)
            val textDate: TextView = itemView.findViewById(R.id.textDate)
            val textCost: TextView = itemView.findViewById(R.id.textCost)
            val photoUrl: ImageView = itemView.findViewById(R.id.doctorImage)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.main_card, parent, false)
            return AppointmentViewHolder(itemView)
        }
        override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
            val appointment = appointments[position]
            holder.textDoctorName.text = appointment.doctorName ?: "N/A"
            holder.textDate.text = appointment.date ?: "N/A"
            holder.textCost.text = appointment.cost ?: "N/A"
            Glide.with(holder.photoUrl.context)
                .load(appointment.photo)
                .into(holder.photoUrl)
        }
        override fun getItemCount() = appointments.size
    }
    private fun fetchAppointments() {
        db.collection("appointments")
            .get()
            .addOnSuccessListener { documents ->
                val appointmentList = mutableListOf<Appointment>()
                for (document in documents) {
                    val appointment = document.toObject(Appointment::class.java)
                    appointmentList.add(appointment)
                }
                displayAppointments(appointmentList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun displayAppointments(appointments: List<Appointment>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = AppointmentAdapter(appointments)
        recyclerView.adapter = adapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        fetchAppointments()
    }
}