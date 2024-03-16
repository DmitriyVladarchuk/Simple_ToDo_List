package com.example.simpletodolist.fragments

import android.app.AlertDialog
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodolist.DataBase.TaskDataBase
import com.example.simpletodolist.R
import com.example.simpletodolist.databinding.FragmentTasksListBinding
import com.example.simpletodolist.fragments.adapter.TaskAdapter
import com.example.simpletodolist.models.TaskViewModel
import com.example.simpletodolist.models.TaskViewModelFactory
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

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
                adapter.submitList(it)
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

        // Swipe
        val swipe = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT ->{
                        AlertDialog.Builder(requireContext())
                            .setMessage("Вы действительно хотите удалить задачу \"${
                                viewModel.tasks.value!![viewHolder.adapterPosition].task
                            }\"?")
                            .setPositiveButton("Да"){ _, _ ->
                                viewModel.deleteTask(viewHolder.adapterPosition)
                            }
                            .setNegativeButton("Отмена"){ _, _ ->
                                adapter.notifyItemChanged(viewHolder.adapterPosition)
                            }
                            .setCancelable(false)
                            .create()
                            .show()
                    }
                    ItemTouchHelper.RIGHT ->{

                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(context!!.getColor(R.color.orange))
                    .addSwipeRightActionIcon(R.drawable.edit_48px)
                    .addSwipeLeftBackgroundColor(context!!.getColor(R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.delete_48px)
                    .create()
                    .decorate()
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(binding.rvTask)
        // разгранечитель
        binding.rvTask.addItemDecoration( DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Событие нажатия на элемент task
    override fun clickItem(position: Int) {
        viewModel.changeStateTask(position)
    }

}