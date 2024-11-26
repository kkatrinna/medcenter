package com.example.medcenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.medcenter.MainZap.AnalysisAdapter as AnalysisAdapter1

private val db = Firebase.firestore
class MainZap : AppCompatActivity() {
    data class Analysis(
        val date: String? = null,
        val type: String? = null
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_zap)
        findViewById<ImageView>(R.id.button_back).setOnClickListener {
            finish()
        }
        fetchAnalyses()
    }
    private fun fetchAnalyses() {
        db.collection("analyses")
            .get()
            .addOnSuccessListener { documents ->
                val analysisList = mutableListOf<Analysis>()
                for (document in documents) {
                    val analysis = document.toObject(Analysis::class.java)
                    analysisList.add(analysis)
                }
                displayAnalyses(analysisList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Ошибка при получении данных: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayAnalyses(analyses: List<Analysis>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = AnalysisAdapter(analyses)
        recyclerView.adapter = adapter
    }

    class AnalysisAdapter(private val analyses: List<Analysis>) :
        RecyclerView.Adapter<AnalysisAdapter.AnalysisViewHolder>() {

        class AnalysisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textDate: TextView = itemView.findViewById(R.id.textDate)
            val textType: TextView = itemView.findViewById(R.id.textType)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalysisViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.main_analiz, parent, false)
            return AnalysisViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: AnalysisViewHolder, position: Int) {
            val analysis = analyses[position]
            holder.textDate.text = analysis.date ?: "Нет данных"
            holder.textType.text = analysis.type ?: "Нет данных"
        }

        override fun getItemCount() = analyses.size
    }
}