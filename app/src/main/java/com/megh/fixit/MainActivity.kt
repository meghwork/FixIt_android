package com.megh.fixit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private val categoryList = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rvCategories)
// Use a Grid with 2 columns
        recyclerView.layoutManager = LinearLayoutManager(this)

        // CHANGED: We now pass a "lambda" function (the code inside {}) to the adapter
        adapter = CategoryAdapter(categoryList, this) { selectedCategory ->

            // This code runs when you click a category
            val intent = Intent(this, CategoryDetailsActivity::class.java)

            // We pass data to the next screen so it knows what to load
            intent.putExtra("CAT_ID", selectedCategory.id)        // The Doc ID (e.g., "electronics")
            intent.putExtra("CAT_NAME", selectedCategory.name)    // The Name (e.g., "Electronics")
            intent.putExtra("CAT_SUBTITLE", selectedCategory.subtitle)

            startActivity(intent)
        }

        recyclerView.adapter = adapter

        fetchCategories()
        // Removed loadCategories() call because fetchCategories() seems to be the one you want to use
        // If you were using loadCategories() before, just make sure to only use ONE method to avoid duplicates.
    }

    private fun fetchCategories() {
        FirebaseFirestore.getInstance()
            .collection("categories")
            .whereEqualTo("active", true)
            .orderBy("order")
            .get()
            .addOnSuccessListener { snapshot ->
                Log.d("FixIt", "Fetched categories count = ${snapshot.size()}")

                categoryList.clear()
                for (doc in snapshot.documents) {
                    // Manual parsing to ensure safety
                    val active = doc.getBoolean("active") ?: false
                    // If your Firestore uses numbers for order, handle Long vs Int safety
                    val order = doc.getLong("order") ?: 999

                    val category = Category(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        subtitle = doc.getString("subtitle") ?: "",
                        icon = doc.getString("icon") ?: "",
                        order = order,
                        active = active
                    )
                    categoryList.add(category)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("FixIt", "Firestore error: ${e.message}", e)
            }
    }
}