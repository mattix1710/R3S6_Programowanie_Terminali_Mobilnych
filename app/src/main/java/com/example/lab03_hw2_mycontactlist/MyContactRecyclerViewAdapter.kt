package com.example.lab03_hw2_mycontactlist

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.lab03_hw2_mycontactlist.data.ContactItem

import com.example.lab03_hw2_mycontactlist.databinding.FragmentContactBinding
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

/**
 * [RecyclerView.Adapter] that can display a [ContactItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyContactRecyclerViewAdapter(
    private val values: List<ContactItem>,
    private val eventListener: ContactListListener
) : RecyclerView.Adapter<MyContactRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
    * Responsible for fetching data for "ViewHolders" at given position in the list;
    * The onBindViewHolder method is called only for the items that are currently displayed on the screen;
    * */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        //Log.i("ITEM_ID: ${item.name}", item.imgId.toString())
        holder.imgView.setImageResource(item.imgId)
        holder.contentView.text = item.name

        holder.contactContainer.setOnClickListener{
            eventListener.onContactClick(position)
        }

        //set View of an ImageButton - deleting contact
        val button: ImageButton = holder.contactContainer.findViewById(R.id.deleteButton)
        button.setOnClickListener { eventListener.onDeleteButtonClick(position) }

        holder.contactContainer.setOnClickListener{
            eventListener.onContactClick(position)
        }

        holder.contactContainer.setOnLongClickListener{
            eventListener.onContactLongClick(position)
            return@setOnLongClickListener true
        }


    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imgView: ImageView = binding.contactImage
        //val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.contactName
        val contactContainer: View = binding.root

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}