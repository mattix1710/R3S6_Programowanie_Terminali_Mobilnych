package com.example.lab03_hw2_mycontactlist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CONTACT_POS_PARAM = "contact position"
private const val CONTACT_NUMBER_PARAM = "contact number"
private const val CONTACT_NAME_PARAM = "contact name"

/**
 * A simple [Fragment] subclass.
 * Use the [CallDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CallDialogFragment : DialogFragment() {
    lateinit var mListener: OnCallDialogInteractionListener

    // TODO: Rename and change types of parameters
    private var contactPosParam: Int? = null
    private var contactNumberParam: String? = null
    private var contactNameParam: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contactPosParam = it.getInt(CONTACT_POS_PARAM)
            contactNumberParam = it.getString(CONTACT_NUMBER_PARAM)
            contactNameParam = it.getString(CONTACT_NAME_PARAM)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param position Parameter 1.
         * @param number Parameter 2.
         * @param name Parameter 3.
         * @return A new instance of fragment CallDialogFragment.
         */
        @JvmStatic
        fun newInstance(position: Int, number: String, name: String, interactionListener: OnCallDialogInteractionListener) =
            CallDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(CONTACT_POS_PARAM, position)
                    putString(CONTACT_NUMBER_PARAM, number)
                    putString(CONTACT_NAME_PARAM, name)
                }
                mListener = interactionListener
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Zadzwonić pod numer $contactNumberParam? ($contactNameParam)")
        builder.setPositiveButton("Potwierdź", DialogInterface.OnClickListener { dialogInterface, i ->
            mListener.onCallPositiveClick(contactNumberParam)
        })
        builder.setNegativeButton("Zrezygnuj", DialogInterface.OnClickListener { dialogInterface, i ->
            mListener.onCallNegativeClick(contactPosParam)
        })
        return builder.create()
    }

    interface OnCallDialogInteractionListener{
        fun onCallPositiveClick(num: String?)
        fun onCallNegativeClick(pos: Int?)
    }
}