package com.megh.fixit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    // Category Variables
    private lateinit var rvCategories: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private val categoryList = mutableListOf<Category>()

    private lateinit var auth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Setup Auth & Header
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        findViewById<TextView>(R.id.tvAppName).text = "Hello, ${user?.displayName ?: "Fixer"}!"

        // 2. Setup Category List
        rvCategories = findViewById(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(this)

        adapter = CategoryAdapter(categoryList) { selectedCategory ->
            val intent = Intent(this, CategoryDetailsActivity::class.java)
            intent.putExtra("CAT_ID", selectedCategory.id)
            intent.putExtra("CAT_NAME", selectedCategory.name)
            intent.putExtra("CAT_SUBTITLE", selectedCategory.subtitle)
            startActivity(intent)
        }
        rvCategories.adapter = adapter

        // 3. Setup Search Bar (Opens new screen)
        setupSearchBar()

        // 4. Setup Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true // Already here
                R.id.nav_more -> {
                    startActivity(Intent(this, MoreActivity::class.java))
                    false // Don't highlight "More" on this screen
                }
                else -> false
            }
        }

        // 5. Load Data
        fetchCategories()
    }

    private fun setupSearchBar() {
        val searchView = findViewById<SearchView>(R.id.searchView)

        // Option A: If user clicks the magnifying glass icon
        searchView.setOnClickListener {
            openSearchScreen()
        }

        // Option B: If user taps the text area (Focus)
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                openSearchScreen()
                searchView.clearFocus() // Clear focus so it doesn't loop
            }
        }
    }

    private fun openSearchScreen() {
        val intent = Intent(this, SearchActivity::class.java)
        // No animation for "Instant" feel
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchCategories() {
        FirebaseFirestore.getInstance()
            .collection("categories")
            .whereEqualTo("active", true)
            .orderBy("order")
            .get()
            .addOnSuccessListener { snapshot ->
                categoryList.clear()
                for (doc in snapshot.documents) {
                    val active = doc.getBoolean("active") ?: false
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