package com.megh.fixit

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

        // 1. Get Data from Intent (passed from MainActivity)
        val categoryId = intent.getStringExtra("CAT_ID") ?: ""
        val categoryName = intent.getStringExtra("CAT_NAME") ?: "Category"
        val categorySubtitle = intent.getStringExtra("CAT_SUBTITLE") ?: ""

        // 2. Setup UI Header
        findViewById<TextView>(R.id.tvCategoryTitle).text = categoryName
        findViewById<TextView>(R.id.tvCategorySubtitle).text = categorySubtitle

        // 3. Setup RecyclerView
        recyclerView = findViewById(R.id.rvGuides)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // --- IMPORTANT CHANGE STARTS HERE ---
        // We initialize the adapter with the list AND the "Click Logic"
        adapter = GuideAdapter(guideList) { selectedGuide ->

            // This code runs only when a user clicks a guide card
            val intent = Intent(this, GuideDetailsActivity::class.java)

            // Pass the details of the selected guide to the next screen
            intent.putExtra("GUIDE_ID", selectedGuide.id)
            intent.putExtra("GUIDE_TITLE", selectedGuide.title)
            intent.putExtra("GUIDE_DESC", selectedGuide.description)
            intent.putExtra("GUIDE_DIFF", selectedGuide.difficulty)
            intent.putExtra("GUIDE_TIME", "${selectedGuide.durationMin}-${selectedGuide.durationMax} mins")

            startActivity(intent)
        }
        // --- IMPORTANT CHANGE ENDS HERE ---

        recyclerView.adapter = adapter

        // 4. Fetch Data from Firestore
        fetchGuides(categoryId)
    }

    private fun fetchGuides(categoryId: String) {
        // Log the ID we are searching for (helps debug typo issues like "elctronics")
        Log.d("FixIt", "Fetching guides for Category ID: '$categoryId'")

        val db = FirebaseFirestore.getInstance()

        db.collection("guides")
            .whereEqualTo("categoryId", categoryId)
            .whereEqualTo("active", true)
            .get()
            .addOnSuccessListener { documents ->
                guideList.clear()
                for (doc in documents) {
                    // Convert Firestore document to Guide object
                    val guide = doc.toObject(Guide::class.java).copy(id = doc.id)
                    guideList.add(guide)
                }
                adapter.notifyDataSetChanged()
                Log.d("FixIt", "Loaded ${guideList.size} guides")
            }
            .addOnFailureListener { e ->
                Log.e("FixIt", "Error fetching guides", e)
            }
    }
}