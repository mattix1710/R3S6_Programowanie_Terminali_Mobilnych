package com.example.lab03_mytodolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab03_mytodolist.data.Tasks
import com.example.lab03_mytodolist.databinding.FragmentItemListBinding

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {
    private lateinit var binding: FragmentItemListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemListBinding.inflate(inflater, container, false)

        //Set the adapter
        with(binding.list){
            layoutManager = LinearLayoutManager(context)
            adapter = MyItemRecyclerViewAdapter(Tasks.ITEMS)
        }
        return binding.root
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
    }
}