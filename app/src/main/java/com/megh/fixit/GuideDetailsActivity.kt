package com.megh.fixit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class GuideDetailsActivity : AppCompatActivity() {

    private lateinit var adapter: StepAdapter
    private val stepList = mutableListOf<Step>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_details)

        // 1. Get Data from Intent (Passed from previous screen)
        val guideId = intent.getStringExtra("GUIDE_ID") ?: ""
        val title = intent.getStringExtra("GUIDE_TITLE") ?: ""
        val desc = intent.getStringExtra("GUIDE_DESC") ?: ""
        val difficulty = intent.getStringExtra("GUIDE_DIFF") ?: ""
        val time = intent.getStringExtra("GUIDE_TIME") ?: ""

        // 2. Setup Static UI (Header)
        findViewById<TextView>(R.id.tvGuideTitle).text = title
        findViewById<TextView>(R.id.tvDescription).text = desc
        findViewById<TextView>(R.id.chipDifficulty).text = difficulty
        findViewById<TextView>(R.id.tvTime).text = time

        // 3. Setup RecyclerView
        val rvSteps = findViewById<RecyclerView>(R.id.rvSteps)
        rvSteps.layoutManager = LinearLayoutManager(this)
        adapter = StepAdapter(stepList)
        rvSteps.adapter = adapter

        // 4. Fetch Steps from Firestore
        if (guideId.isNotEmpty()) {
            fetchSteps(guideId)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchSteps(guideId: String) {
        val db = FirebaseFirestore.getInstance()

        // Query: guides -> {guideId} -> steps
        db.collection("guides")
            .document(guideId)
            .collection("steps")
            .orderBy("order") // Sort by order field (1, 2, 3...)
            .get()
            .addOnSuccessListener { result ->
                stepList.clear()
                for (doc in result) {
                    val step = doc.toObject(Step::class.java).copy(id = doc.id)
                    stepList.add(step)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("FixIt", "Error loading steps", e)
            }
    }
}