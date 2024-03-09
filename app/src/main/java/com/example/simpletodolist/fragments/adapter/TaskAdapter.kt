package com.example.simpletodolist.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodolist.R
import com.example.simpletodolist.models.Task

class TaskAdapter(val click: Click): RecyclerView.Adapter<TaskAdapter.TaskItemViewHolder>() {

    var data = listOf<Task>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        return TaskItemViewHolder.inflateFrom(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.taskDone.setOnClickListener {
            click.clickItem(position)
        }
    }

    class TaskItemViewHolder(rootView: ViewGroup): RecyclerView.ViewHolder(rootView) {

        val taskDone = rootView.findViewById<CheckBox>(R.id.task_done)

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