package com.megh.fixit

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GuideAdapter(
    private var guideList: List<Guide>, // Must be 'var' to allow updates
    private val context: Context,
    private val onItemClick: (Guide) -> Unit
) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    class GuideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // FIXED: IDs now match your item_guide.xml
        val tvName: TextView = view.findViewById(R.id.tvGuideTitle)
        val tvDifficulty: TextView = view.findViewById(R.id.chipDifficulty)
        val tvTime: TextView = view.findViewById(R.id.tvDuration)
        // Removed imgIcon because your XML doesn't have an ImageView for the guide
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_guide, parent, false)
        return GuideViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = guideList[position]

        // Bind Data
        holder.tvName.text = guide.name
        holder.tvDifficulty.text = guide.difficulty
        holder.tvTime.text = guide.time

        // Click Listener
        holder.itemView.setOnClickListener { onItemClick(guide) }
    }

    override fun getItemCount(): Int = guideList.size

    // Search Functionality
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Guide>) {
        guideList = newList
        notifyDataSetChanged()
    }
}