package com.example.simpletodolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpletodolist.DataBase.TaskDataBase
import com.example.simpletodolist.R
import com.example.simpletodolist.databinding.FragmentTasksListBinding
import com.example.simpletodolist.fragments.adapter.TaskAdapter
import com.example.simpletodolist.models.TaskViewModel
import com.example.simpletodolist.models.TaskViewModelFactory

class TasksListFragment : Fragment() {

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

        val adapter = TaskAdapter()
        binding.rvTask.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvTask.adapter = adapter

//        viewModel.newTask = "Тест создания и вывода задачи"
//        viewModel.addTask()

        viewModel.tasks.observe(lifecycleOwner, Observer {
            it?.let{
                adapter.data = it
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}