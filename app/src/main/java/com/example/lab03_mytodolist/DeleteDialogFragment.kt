package com.example.lab03_mytodolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TASK_NAME_PARAM = "task name"
private const val TASK_POS_PARAM = "task pos"

/**
 * A simple [Fragment] subclass.
 * Use the [DeleteDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeleteDialogFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var taskNameParam: String? = null
    private var taskPosParam: Int? = null

    lateinit var mListener: OnDeleteDialogInteractionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            taskNameParam = it.getString(TASK_NAME_PARAM)
            taskPosParam = it.getInt(TASK_POS_PARAM)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Delete this entry?" + " $taskNameParam")
        builder.setPositiveButton("Confirm", DialogInterface.OnClickListener{ dialogInterface, i ->
            mListener?.onDialogPositiveClick(taskPosParam)
        })
        builder.setNegativeButton("Discard", DialogInterface.OnClickListener{ dialogInterface, i ->
            mListener?.onDialogNegativeClick(taskPosParam)
        })
        return builder.create()
    }

    companion object {
        fun newInstance(name: String, pos: Int, interactionListener: OnDeleteDialogInteractionListener) =
            DeleteDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(TASK_NAME_PARAM, name)
                    putInt(TASK_POS_PARAM, pos)
                }
                mListener = interactionListener
            }
    }

    interface OnDeleteDialogInteractionListener{
        fun onDialogPositiveClick(pos: Int?)
        fun onDialogNegativeClick(pos: Int?)
    }
}