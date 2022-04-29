package com.example.lab03_hw2_mycontactlist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab03_hw2_mycontactlist.data.ContactItem
import com.example.lab03_hw2_mycontactlist.data.Contacts
import com.example.lab03_hw2_mycontactlist.data.makeImage
import com.example.lab03_hw2_mycontactlist.databinding.FragmentAddContactBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AddContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddContactFragment : Fragment() {
    val args: AddContactFragmentArgs by navArgs()

    private lateinit var binding: FragmentAddContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveButton.setOnClickListener{ saveContact() }
        binding.nameInput.setText(args.contactToEdit?.name)
        binding.dateInput.setText(args.contactToEdit?.birthday)
        binding.phoneInput.setText(args.contactToEdit?.phoneNumber)
        /**
         * ?. - null safety operator
         * Check if imgId is NULL, if yes - draw a new image from 16 possible
         * Otherwise - display already saved image
        */
        binding.contactImageIn.setImageResource(args.contactToEdit?.imgId.let { makeImage() })

        //if image was clicked - randomize new image
        val imgChange: ImageView = binding.contactImageIn
        imgChange.setOnClickListener { binding.contactImageIn.setImageResource(makeImage()) }


        //display different prompt whether the contact is new or already exists
        val promptText: TextView = binding.root.findViewById(R.id.label)
        if(args.edit)
            promptText.text = R.string.enter_contact_details_existing.toString()
        else
            promptText.text = R.string.enter_contact_details_new.toString()

    }

    private fun saveContact() {
        //Get the values from data fields on the screen
        var name: String = binding.nameInput.text.toString()
        var phone: String = binding.phoneInput.text.toString()
        var birthday: String = binding.dateInput.text.toString()
        var imgId: Int = binding.contactImageIn.id

        //Handle missing EditText input
        if(name.isEmpty()) name = "Jan Kowalski"
        if(phone.isEmpty()) phone = "+48 123 456 789"
        if(birthday.isEmpty()) birthday = "01/01/1990"

        //Create a new ContactItem based on input values
        val contactItem = ContactItem(
            {name + phone}.hashCode().toString(),
            name,
            birthday,
            phone,
            imgId
        )

        if(!args.edit) {
            //Add the new item to Contacts list
            Contacts.addContact(contactItem)
        }else{
            Contacts.updateContact(args.contactToEdit, contactItem)
        }

        //Hide the software keyboard with InputMethodManager
        val inputMethodManager: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

        //Use "popBackStack" in order to be able to go back to a given fragment in the back stack
        //We then specify the id of the fragment we want to navigate to...
        findNavController().popBackStack(R.id.contactFragment, false)
    }
}