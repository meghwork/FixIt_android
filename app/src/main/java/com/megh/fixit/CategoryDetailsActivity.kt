package com.megh.fixit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class CategoryDetailsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GuideAdapter
    private val guideList = mutableListOf<Guide>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_details)

        // 1. Get Data from Intent
        val categoryId = intent.getStringExtra("CAT_ID") ?: ""
        val categoryName = intent.getStringExtra("CAT_NAME") ?: "Category"
        val categorySubtitle = intent.getStringExtra("CAT_SUBTITLE") ?: ""

        // 2. Setup UI
        findViewById<TextView>(R.id.tvCategoryTitle).text = categoryName
        findViewById<TextView>(R.id.tvCategorySubtitle).text = categorySubtitle

        // 3. Setup RecyclerView
        recyclerView = findViewById(R.id.rvGuides)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with click listener
        adapter = GuideAdapter(guideList, this) { selectedGuide ->
            val intent = Intent(this, GuideDetailsActivity::class.java)

            // FIXED: Using correct fields from new Guide class
            intent.putExtra("GUIDE_ID", selectedGuide.id)
            intent.putExtra("GUIDE_TITLE", selectedGuide.name) // 'name', not 'title'
            intent.putExtra("GUIDE_DESC", selectedGuide.description)
            intent.putExtra("GUIDE_DIFF", selectedGuide.difficulty)
            intent.putExtra("GUIDE_TIME", selectedGuide.time) // 'time', not calculation

            startActivity(intent)
        }

        recyclerView.adapter = adapter

        // 4. Fetch Data
        fetchGuides(categoryId)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchGuides(categoryId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("guides")
            .whereEqualTo("categoryId", categoryId)
            // .whereEqualTo("active", true) // Uncomment if you have this field in DB
            .get()
            .addOnSuccessListener { documents ->
                guideList.clear()
                for (doc in documents) {
                    val guide = Guide(
                        id = doc.id,
                        name = doc.getString("name") ?: doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "No description available.",
                        categoryId = doc.getString("categoryId") ?: "",
                        icon = doc.getString("icon") ?: "",
                        difficulty = doc.getString("difficulty") ?: "Medium",
                        time = doc.getString("time") ?: "15 min"
                    )
                    guideList.add(guide)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("FixIt", "Error fetching guides", e)
            }
    }
}