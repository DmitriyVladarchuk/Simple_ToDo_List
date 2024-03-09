package com.example.simpletodolist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "task_name")
    var task: String = "",

    @ColumnInfo(name = "task_done")
    var isDone: Boolean = false
)
