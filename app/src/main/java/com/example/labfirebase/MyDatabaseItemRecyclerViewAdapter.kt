package com.example.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labfirebase.SensorValue
import com.example.labfirebase.databinding.DatabaseItemBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyDatabaseItemRecyclerViewAdapter(
        private val values: List<SensorValue>)
    : RecyclerView.Adapter<MyDatabaseItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    return ViewHolder(DatabaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        // Use a SimpleDateFormat class to convert timestamp
        // in millis to hh:mm:ss format
        val pattern = "hh:mm:ss"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val date: String = simpleDateFormat.format(Date(item.timestamp))

        holder.timestampTextView.text = date
        holder.valueTextView.text = item.value.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: DatabaseItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val timestampTextView: TextView = binding.timestampTextView
        val valueTextView: TextView = binding.valueTextView

        override fun toString(): String {
            return super.toString() + " '" + valueTextView.text + "'"
        }
    }

}