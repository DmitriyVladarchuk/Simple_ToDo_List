package com.example.simpletodolist.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpletodolist.DataBase.TaskDao
import kotlinx.coroutines.launch

class TaskViewModel(private val dao: TaskDao): ViewModel() {

    val tasks: LiveData<List<Task>> = dao.getAll()

    fun addTask(newTask: String){
        viewModelScope.launch {
            val task = Task(task = newTask)
            dao.inset(task)
        }
    }

    fun changeStateTask(position: Int){
        val currentTasks = tasks.value ?: return
        val task = currentTasks[position]
        task.isDone = !task.isDone
        viewModelScope.launch {
            dao.update(task)
        }
    }

    fun getAllTask(): LiveData<List<Task>> = dao.getAll()
}