package com.example.lab03_hw2_mycontactlist

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.core.view.isEmpty
import androidx.core.view.size
import androidx.navigation.fragment.findNavController
import com.example.lab03_hw2_mycontactlist.data.Contacts
import com.example.lab03_hw2_mycontactlist.databinding.FragmentContactListBinding
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import java.util.jar.Manifest

/**
 * A fragment representing a list of Contacts.
 */
class ContactFragment : Fragment(), ContactListListener,
    DeleteDialogFragment.OnDeleteDialogInteractionListener,
    CallDialogFragment.OnCallDialogInteractionListener {
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

        //Log.i("Q in List", binding.list.adapter?.getItemCount().toString())
        onCheckContactsQuantity()

        binding.addButton.setOnClickListener{ addButtonClick() }

        return binding.root
    }

    private fun addButtonClick() {
        findNavController().navigate(R.id.action_contactFragment_to_addContactFragment)
    }

    override fun onContactLongClick(position: Int) {
        showCallDialog(position)
    }

    override fun onContactClick(position: Int) {
        val actionContactFragmentToDisplayContactFragment =
            ContactFragmentDirections.actionContactFragmentToDisplayContactFragment(Contacts.ITEMS.get(position))
        findNavController().navigate(actionContactFragmentToDisplayContactFragment)
    }

    /**
     * "PROCEEDING A CALL" FUNCTIONS...
     */

    private fun showCallDialog(position: Int){
        val callDialog = CallDialogFragment.newInstance(position, Contacts.ITEMS.get(position).phoneNumber, Contacts.ITEMS.get(position).name, this)
        callDialog.show(requireActivity().supportFragmentManager, "CallDialog")
    }

    override fun onCallPositiveClick(num: String?) {
        Snackbar.make(requireView(), "Połączenie trwa...", Snackbar.LENGTH_LONG).show()

        val phoneIntent: Intent = Intent(Intent.ACTION_DIAL)
        phoneIntent.setData(Uri.parse("tel:$num"))
        startActivity(phoneIntent)
    }

    override fun onCallNegativeClick(pos: Int?) {
        Snackbar.make(requireView(), "Połączenie anulowane", Snackbar.LENGTH_LONG)
            .setAction("Powtórzyć?", View.OnClickListener { showCallDialog(pos!!) })
            .show()
    }


    /**
     * DELETING CONTACT FUNCTIONS...
     *
     * onDeleteButtonClick declaration exist in ContactListListener interface, however its initialization appears in
     * MyContactRecyclerViewAdapter class at onBindViewHolder function
     */

    override fun onDeleteButtonClick(position: Int) {
        showDeleteDialog(position)
    }

    private fun showDeleteDialog(position: Int){
        val deleteDialog = DeleteDialogFragment.newInstance(Contacts.ITEMS.get(position).name, position, this)
        deleteDialog.show(requireActivity().supportFragmentManager, "DeleteDialog")
    }

    override fun onDialogPositiveClick(pos: Int?) {
        Contacts.ITEMS.removeAt(pos!!)
        onCheckContactsQuantity()
        Snackbar.make(requireView(), "Task Deleted", Snackbar.LENGTH_LONG)
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

    private fun onCheckContactsQuantity(){
        if(binding.list.adapter?.getItemCount() == 0)
            binding.noContactsMessage.visibility = TextView.VISIBLE
        else
            binding.noContactsMessage.visibility = TextView.GONE
    }
}