package com.example.simpletodolist.DataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.simpletodolist.models.Task

@Dao
interface TaskDao {
    @Insert
    suspend fun inset(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks")
    fun getAll(): LiveData<List<Task>>
}