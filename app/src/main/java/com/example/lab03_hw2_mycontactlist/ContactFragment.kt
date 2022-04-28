package com.example.lab03_hw2_mycontactlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lab03_hw2_mycontactlist.data.Contacts
import com.example.lab03_hw2_mycontactlist.databinding.FragmentContactListBinding

/**
 * A fragment representing a list of Contacts.
 */
class ContactFragment : Fragment(), ContactListListener {
    private lateinit var binding: FragmentContactListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        //val view = inflater.inflate(R.layout.fragment_contact_list, container, false)

        // Set the adapter
        with(binding.list){
            layoutManager = LinearLayoutManager(context)
            adapter = MyContactRecyclerViewAdapter(Contacts.ITEMS, this@ContactFragment)
        }

        binding.addButton.setOnClickListener{ addButtonClick() }

        return binding.root
    }

    private fun addButtonClick() {
        findNavController().navigate(R.id.action_contactFragment_to_addContactFragment)
    }

    override fun onContactClick(position: Int) {
        val actionContactFragmentToDisplayContactFragment =
            ContactFragmentDirections.actionContactFragmentToDisplayContactFragment(Contacts.ITEMS.get(position))
                findNavController().navigate(actionContactFragmentToDisplayContactFragment)
    }

    override fun onContactLongClick(position: Int) {
        //TODO("Not yet implemented")
    }
}