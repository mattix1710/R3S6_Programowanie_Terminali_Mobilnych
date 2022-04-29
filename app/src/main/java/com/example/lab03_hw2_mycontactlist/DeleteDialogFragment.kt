package com.example.lab03_hw2_mycontactlist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CONTACT_NAME_PARAM = "contact name"
private const val CONTACT_POS_PARAM = "contact pos"

/**
 * A simple [Fragment] subclass.
 * Use the [DeleteDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeleteDialogFragment : DialogFragment() {
    lateinit var mListener: OnDeleteDialogInteractionListener

    // TODO: Rename and change types of parameters
    private var contactNameParam: String? = null
    private var contactPosParam: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contactNameParam = it.getString(CONTACT_NAME_PARAM)
            contactPosParam = it.getInt(CONTACT_POS_PARAM)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param name Parameter 1.
         * @param pos Parameter 2.
         * @param interactionListener Parameter 3.
         * @return A new instance of fragment DeleteDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(name: String, pos: Int, interactionListener: OnDeleteDialogInteractionListener) =
            DeleteDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(CONTACT_NAME_PARAM, name)
                    putInt(CONTACT_POS_PARAM, pos)
                }
                mListener = interactionListener
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Delete this entry?" + " $contactNameParam")
        builder.setPositiveButton("Confirm", DialogInterface.OnClickListener{ dialogInterface, i ->
            mListener?.onDialogPositiveClick(contactPosParam)
        })
        builder.setNegativeButton("Discard", DialogInterface.OnClickListener{ dialogInterface, i ->
            mListener?.onDialogNegativeClick(contactPosParam)
        })
        return builder.create()
    }

    interface OnDeleteDialogInteractionListener{
        fun onDialogPositiveClick(pos: Int?)
        fun onDialogNegativeClick(pos: Int?)
    }
}