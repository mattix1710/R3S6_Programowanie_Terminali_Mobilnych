package com.example.lab03_hw2_mycontactlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab03_hw2_mycontactlist.data.ContactItem
import com.example.lab03_hw2_mycontactlist.databinding.FragmentDisplayContactBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DisplayContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DisplayContactFragment : Fragment() {
    val args: DisplayContactFragmentArgs by navArgs()
    lateinit var binding: FragmentDisplayContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDisplayContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contact: ContactItem = args.contact
        binding.displayName.text = contact.name
        binding.displayBirthday.text = contact.birthday
        binding.displayPhone.text = contact.phoneNumber
        binding.displayContactImage.setImageResource(contact.imgId)

        binding.editButton.setOnClickListener{
            val contactToEdit =
                DisplayContactFragmentDirections.actionDisplayContactFragmentToAddContactFragment(
                    contactToEdit = contact,
                    edit = true
                )
            findNavController().navigate(contactToEdit)
        }
    }
}