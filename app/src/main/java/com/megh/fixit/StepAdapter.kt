package com.megh.fixit

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StepAdapter(private val steps: List<Step>) : RecyclerView.Adapter<StepAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.findViewById(R.id.tvStepNumber)
        val title: TextView = view.findViewById(R.id.tvStepTitle)
        val desc: TextView = view.findViewById(R.id.tvStepDesc)
        val layoutTip: LinearLayout = view.findViewById(R.id.layoutTip)
        val tipText: TextView = view.findViewById(R.id.tvStepTip)
        val timelineLine: View = view.findViewById(R.id.viewTimeline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_step_detail, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = steps[position]

        holder.number.text = (position + 1).toString()
        holder.title.text = step.title
        holder.desc.text = step.description

        // TIP LOGIC: Show yellow box ONLY if tip exists
        if (step.tip.isNotEmpty()) {
            holder.layoutTip.visibility = View.VISIBLE
            holder.tipText.text = step.tip
        } else {
            holder.layoutTip.visibility = View.GONE
        }

        // TIMELINE LOGIC: Hide the line for the very last item
        if (position == steps.size - 1) {
            holder.timelineLine.visibility = View.INVISIBLE
        } else {
            holder.timelineLine.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = steps.size
}