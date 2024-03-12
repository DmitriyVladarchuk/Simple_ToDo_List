package com.example.simpletodolist.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodolist.R
import com.example.simpletodolist.models.Task

class TaskAdapter(private val click: Click): ListAdapter<Task, TaskAdapter.TaskItemViewHolder>(TaskDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        return TaskItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.taskDone.setOnClickListener {
            click.clickItem(holder.adapterPosition)
        }
    }

    class TaskItemViewHolder(rootView: ViewGroup): RecyclerView.ViewHolder(rootView) {

        val taskDone = rootView.findViewById<CheckBox>(R.id.task_done)!!
        companion object{
            fun inflateFrom(parent: ViewGroup): TaskItemViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.task_item, parent, false) as CardView
                return TaskItemViewHolder(view)
            }
        }

        fun bind(item: Task){
            taskDone.text = item.task
            taskDone.isChecked = item.isDone
        }

    }

    interface Click{
        fun clickItem(position: Int)
    }

}