package com.example.lab03_mytodolist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.lab03_mytodolist.data.IMPORTANCE
import com.example.lab03_mytodolist.data.TaskItem
import com.example.lab03_mytodolist.data.Tasks
import com.example.lab03_mytodolist.databinding.FragmentAddTaskBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveButton.setOnClickListener{saveTask()}
    }

    private fun saveTask(){
        //Get the values from data fields on the screen
        var title: String = binding.titleInput.text.toString()
        var description: String = binding.descriptionInput.text.toString()
        val importance = when(binding.importanceGroup.checkedRadioButtonId){
            R.id.low_radioButton -> IMPORTANCE.LOW
            R.id.normal_radioButton -> IMPORTANCE.NORMAL
            R.id.high_radioButton -> IMPORTANCE.HIGH
            else -> IMPORTANCE.NORMAL
        }
        //Handle missing EditText input
        if(title.isEmpty()) title = getString(R.string.default_task_title) + "${Tasks.ITEMS.size + 1}"
        if(description.isEmpty()) description = getString(R.string.no_desc_msg)

        //Create a new TaskItem based on input values
        val taskItem = TaskItem(
            {title + description}.hashCode().toString(),
            title,
            description,
            importance
        )
        //Add the new item to Tasks list
        Tasks.addTask(taskItem)

        //Hide the software keyboard with InputMethodManager
        val inputMethodManager: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}