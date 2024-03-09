package com.example.simpletodolist.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpletodolist.DataBase.TaskDataBase
import com.example.simpletodolist.R
import com.example.simpletodolist.databinding.FragmentTasksListBinding
import com.example.simpletodolist.fragments.adapter.TaskAdapter
import com.example.simpletodolist.models.TaskViewModel
import com.example.simpletodolist.models.TaskViewModelFactory

class TasksListFragment : Fragment(), TaskAdapter.Click {

    private lateinit var viewModel: TaskViewModel
    private var _binding: FragmentTasksListBinding? = null
    val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksListBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val dao = TaskDataBase.getInstance(application).taskDao

        val viewModelFactory = TaskViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)
        val lifecycleOwner = viewLifecycleOwner

        val adapter = TaskAdapter(this)
        binding.rvTask.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvTask.adapter = adapter

        viewModel.tasks.observe(lifecycleOwner, Observer {
            it?.let{
                adapter.data = it
            }
        })

        binding.fabAdd.setOnClickListener {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)
            val inputTask = mDialogView.findViewById<EditText>(R.id.etAddTask)
            AlertDialog.Builder(requireContext())
                .setTitle("Новая задача")
                .setView(mDialogView)
                .setPositiveButton("Сохранить"){ _, _ ->
                    if(inputTask.text.isNotBlank())
                        viewModel.addTask(inputTask.text.toString())
                }
                .setNegativeButton("Отмена", null)
                .setCancelable(true)
                .setView(mDialogView)
                .create()
                .show()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun clickItem(position: Int) {
        viewModel.changeStateTask(position)
    }

}