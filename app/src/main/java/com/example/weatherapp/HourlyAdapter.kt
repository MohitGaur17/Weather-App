package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HourlyAdapter(
    private val timeList: List<String>,
    private val tempList: List<Double>
) : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeHourly: TextView = itemView.findViewById(R.id.timeHourly)
        val temperatureHourly: TextView = itemView.findViewById(R.id.temperatureHourly)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hourly, parent, false)
        return HourlyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val hour = timeList[position].substring(11,16)
        holder.timeHourly.text = hour
        holder.temperatureHourly.text = "${tempList[position]}°C"
    }

    override fun getItemCount(): Int = timeList.size
}