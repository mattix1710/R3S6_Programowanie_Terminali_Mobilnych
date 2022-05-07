package com.example.lab03_hw2_mycontactlist

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab03_hw2_mycontactlist.data.ContactItem
import com.example.lab03_hw2_mycontactlist.data.Contacts
import com.example.lab03_hw2_mycontactlist.data.makeImage
import com.example.lab03_hw2_mycontactlist.databinding.FragmentAddContactBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [AddContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddContactFragment : Fragment() {
    val args: AddContactFragmentArgs by navArgs()

    private lateinit var binding: FragmentAddContactBinding
    private val myFormat = "dd/MM/yyyy"
    private val sdf = SimpleDateFormat(myFormat)
    private var imgIdAux: Int = 0;

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
         * ?: - Elvis operator
         * If the expression to the left of "?:" is not NULL, the Elvis operator returns it,
         * otherwise it returns the expression to the right;
        */
        imgIdAux = args.contactToEdit?.imgId ?: makeImage()
        binding.contactImageIn.setImageResource(imgIdAux)

        //if image was clicked - randomize new image
        binding.contactImageIn.setOnClickListener { imgIdAux = makeImage()
            binding.contactImageIn.setImageResource(imgIdAux)
        }

        Log.i("IMG_ID", binding.contactImageIn.id.toString())

        /**
         * setting NUMBER INPUT checker
         */

        binding.phoneInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    if(p0.length > 9)
                        binding.phoneInput.setText(p0.substring(0,9))
                }
            }

        })


        /**
         * setting CALENDAR VIEW - for birthday setting purposes
         */
        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener{ view, year, month, day ->

            //save date to calendar - MONTH is -1 number
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)

            val getDay: Int = cal.get(Calendar.DAY_OF_MONTH)
            val getMonth: Int = cal.get(Calendar.MONTH)+1               //has to be "+1", because months in Calendar class are iterated from 0 to 11
            val getYear: Int = cal.get(Calendar.YEAR)
            Log.i("CAL_D", cal.get(Calendar.DAY_OF_MONTH).toString())
            Log.i("CAL_M", cal.get(Calendar.MONTH).toString())
            Log.i("CAL_Y", cal.get(Calendar.YEAR).toString())


            var date: Date = sdf.parse("$getDay/$getMonth/$getYear")
            binding.dateInput.setText(sdf.format(date))
        }

        binding.dateInput.showSoftInputOnFocus = false
        //binding.dateInput.onFocusChangeListener.
        binding.dateInput.setOnClickListener {
            DatePickerDialog(
                this.requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


    }

    private fun saveContact() {
        //Get the values from data fields on the screen
        var name: String = binding.nameInput.text.toString()
        var phone: String = binding.phoneInput.text.toString()
        var birthday: String = binding.dateInput.text.toString()
        var imgId: Int = imgIdAux

        //binding.contactImageIn.id
        //^^^
        //It's not an image ID, but ID of an ImageView

        Log.i("IMG_SAVE", imgId.toString())

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
        hideSoftwareKeyboard()

        //Use "popBackStack" in order to be able to go back to a given fragment in the back stack
        //We then specify the id of the fragment we want to navigate to...
        findNavController().popBackStack(R.id.contactFragment, false)
    }

    private fun hideSoftwareKeyboard(){
        val inputMethodManager: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}