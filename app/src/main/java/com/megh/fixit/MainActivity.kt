package com.megh.fixit

import Category
import CategoryAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CategoryAdapter(categoryList, this)
        recyclerView.adapter = adapter

        fetchCategories()
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
                    Log.d("FixIt", "DocID=${doc.id} data=${doc.data}")

                    val category = Category(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        subtitle = doc.getString("subtitle") ?: "",
                        icon = doc.getString("icon") ?: ""
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
