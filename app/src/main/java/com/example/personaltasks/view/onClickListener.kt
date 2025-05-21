package com.example.personaltasks.view

import android.view.ContextMenu

interface onClickListener {
    fun onTaskClick(position: Int)

    fun onTaskContextMenu(position: Int, menu: ContextMenu)
}