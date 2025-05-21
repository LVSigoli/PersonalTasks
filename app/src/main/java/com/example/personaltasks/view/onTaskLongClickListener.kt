package com.example.personaltasks.view

interface onTaskLongClickListener {
    fun onDetailsClick(position:Int)
    fun onEditClick(position: Int)
    fun onRemoveClick(position: Int)
}