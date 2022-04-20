package com.example.lab03_mytodolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab03_mytodolist.data.IMPORTANCE
import com.example.lab03_mytodolist.data.TaskItem
import com.example.lab03_mytodolist.databinding.FragmentDisplayTaskBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DisplayTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DisplayTaskFragment : Fragment() {
    val args: DisplayTaskFragmentArgs by navArgs()
    lateinit var binding: FragmentDisplayTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDisplayTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val task: TaskItem = args.task
        binding.displayTitle.text = task.title
        binding.displayDescritpion.text = task.description

        val resource = when(task.importance){
            IMPORTANCE.LOW -> R.drawable.circle_drawable_green
            IMPORTANCE.NORMAL -> R.drawable.circle_drawable_orange
            IMPORTANCE.HIGH -> R.drawable.circle_drawable_red
        }
        binding.displayImportance.setImageResource(resource)

        binding.displayEdit.setOnClickListener{
            val taskToEdit =
                DisplayTaskFragmentDirections.actionDisplayTaskFragmentToAddTaskFragment(
                    taskToEdit = task,
                    edit = true)
            findNavController().navigate(taskToEdit)
        }
    }
}