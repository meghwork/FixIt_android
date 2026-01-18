package com.megh.fixit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// CHANGED: Added 'onItemClick' to constructor
class GuideAdapter(
    private val guideList: List<Guide>,
    private val onItemClick: (Guide) -> Unit
) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    class GuideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // ... (Keep existing view lookups) ...
        val title: TextView = view.findViewById(R.id.tvGuideTitle)
        val description: TextView = view.findViewById(R.id.tvGuideDesc)
        val difficulty: TextView = view.findViewById(R.id.chipDifficulty)
        val duration: TextView = view.findViewById(R.id.tvDuration)
        val rating: TextView = view.findViewById(R.id.tvRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_guide, parent, false)
        return GuideViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = guideList[position]

        holder.title.text = guide.title
        holder.description.text = guide.description
        holder.difficulty.text = guide.difficulty
        holder.rating.text = guide.rating.toString()
        holder.duration.text = "${guide.durationMin}-${guide.durationMax} min"

        // CHANGED: Handle Click
        holder.itemView.setOnClickListener {
            onItemClick(guide)
        }
    }

    override fun getItemCount() = guideList.size
}