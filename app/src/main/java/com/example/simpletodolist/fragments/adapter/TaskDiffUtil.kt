package com.example.simpletodolist.fragments.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.simpletodolist.models.Task

class TaskDiffUtil(
    private val oldList: List<Task>,
    private val newList: List<Task>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].task != newList[newItemPosition].task -> false
            oldList[oldItemPosition].isDone != newList[newItemPosition].isDone -> false
            else -> true
        }
    }
}