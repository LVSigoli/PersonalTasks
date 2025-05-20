package com.example.personaltasks.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    // Methods
    @Insert
    fun createTask(task: Task): Long

    @Delete
    fun removeTask(task: Task): Int

    @Update
    fun updateTask(task: Task): Int

    // Queries
    @Query("SELECT * FROM Task")
    fun fetchTasks(): MutableList<Task>

    @Query("SELECT * FROM Task WHERE id = :id")
    fun fetchTask(id: Int): Task

}