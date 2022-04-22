package com.example.lab03_mytodolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lab03_mytodolist.data.Tasks
import com.example.lab03_mytodolist.databinding.FragmentItemListBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment(), ToDoListListener,
    DeleteDialogFragment.OnDeleteDialogInteractionListener {
    private lateinit var binding: FragmentItemListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemListBinding.inflate(inflater, container, false)

        //Set the adapter
        with(binding.list){
            layoutManager = LinearLayoutManager(context)
            adapter = MyItemRecyclerViewAdapter(Tasks.ITEMS, this@ItemFragment)
        }

        binding.addButton.setOnClickListener{addButtonClick()}

        return binding.root
    }

    private fun addButtonClick() {
        findNavController().navigate(R.id.action_itemFragment_to_addTaskFragment)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
    }

    override fun onItemClick(position: Int) {
        Log.i("Task_click", "pos_${position}")
        val actionTaskFragmentToDisplayTaskFragment =
            ItemFragmentDirections.actionItemFragmentToDisplayTaskFragment(Tasks.ITEMS.get(position))
        findNavController().navigate(actionTaskFragmentToDisplayTaskFragment)
    }

    override fun onItemLongClick(position: Int) {
        showDeleteDialog(position)
    }

    private fun showDeleteDialog(position: Int){
        val deleteDialog = DeleteDialogFragment.newInstance(Tasks.ITEMS.get(position).title, position, this)
        deleteDialog.show(requireActivity().supportFragmentManager, "DeleteDialog")
    }

    override fun onDialogPositiveClick(pos: Int?) {
        Tasks.ITEMS.removeAt(pos!!)
        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
            .show()
        notifyDataSetChanged()
    }

    override fun onDialogNegativeClick(pos: Int?) {
        Snackbar.make(requireView(), "Delete cancelled", Snackbar.LENGTH_LONG)
            .setAction("Redo", View.OnClickListener { showDeleteDialog(pos!!) })
            .show()
    }

    private fun notifyDataSetChanged(){
        val rvAdapter = binding.list.adapter
        rvAdapter?.notifyDataSetChanged()
    }

}