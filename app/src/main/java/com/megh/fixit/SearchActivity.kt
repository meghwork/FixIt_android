package com.megh.fixit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : AppCompatActivity() {

    private lateinit var adapter: GuideAdapter
    private val allGuides = mutableListOf<Guide>()
    private val filteredGuides = mutableListOf<Guide>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val rv = findViewById<RecyclerView>(R.id.rvSearchResults)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = GuideAdapter(filteredGuides, this) { guide ->
            val intent = Intent(this, GuideDetailsActivity::class.java)
            intent.putExtra("GUIDE_ID", guide.id)
            intent.putExtra("GUIDE_TITLE", guide.name)
            intent.putExtra("GUIDE_DESC", guide.description)
            intent.putExtra("GUIDE_DIFF", guide.difficulty)
            intent.putExtra("GUIDE_TIME", guide.time)
            startActivity(intent)
        }
        rv.adapter = adapter

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.onActionViewExpanded()
        searchView.requestFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })

        fetchAllGuides()
    }

    private fun filter(query: String?) {
        val text = query?.lowercase()?.trim() ?: ""
        filteredGuides.clear()

        if (text.isNotEmpty()) {
            for (guide in allGuides) {
                if (guide.name.lowercase().contains(text)) {
                    filteredGuides.add(guide)
                }
            }
        }
        adapter.updateList(filteredGuides)
    }

    private fun fetchAllGuides() {
        FirebaseFirestore.getInstance().collection("guides")
            .get()
            .addOnSuccessListener { snapshot ->
                allGuides.clear()
                for (doc in snapshot.documents) {
                    val guide = Guide(
                        id = doc.id,
                        name = doc.getString("name") ?: doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        categoryId = doc.getString("categoryId") ?: "",
                        icon = doc.getString("icon") ?: "",
                        difficulty = doc.getString("difficulty") ?: "Medium",
                        time = doc.getString("time") ?: "10 min"
                    )
                    allGuides.add(guide)
                }
            }
    }
}