package com.example.personaltasks.controller

import androidx.room.Room

import android.content.Context

import com.example.personaltasks.model.Task

import com.example.personaltasks.model.TaskDao

import com.example.personaltasks.model.TaskRoomDB

class TaskController(context: Context) {

    private val taskDao: TaskDao = Room.databaseBuilder(
        context,
        TaskRoomDB::class.java,
        "task-database"
    ).build().taskDao()

    fun getTasks() = taskDao.fetchTasks()

    fun getTask(id: Int) = taskDao.fetchTask(id)

    fun createTask(task: Task) = taskDao.createTask(task)

    fun updateTask(task: Task) = taskDao.updateTask(task)

    fun deleteTask(task: Task) = taskDao.removeTask(task)
}
