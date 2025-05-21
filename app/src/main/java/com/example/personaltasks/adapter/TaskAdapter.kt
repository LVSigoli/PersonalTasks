package com.example.personaltasks.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.personaltasks.databinding.TaskCardBinding
import com.example.personaltasks.model.Task


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(
    context: Context,
    private val taskList: MutableList<Task>,
    private val onClick: (Task) -> Unit
) : ArrayAdapter<Task>(context, 0, taskList) {

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val binding: TaskCardBinding

        if (convertView == null) {
            binding = TaskCardBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as TaskCardBinding
        }

        val task = taskList[position]

        binding.titleTv.text = task.title
        binding.descriptionTv.text = task.description // ✅ Linha adicionada
        binding.dateTv.text = task.dueDate.toFormattedString()

        val isOverdue = task.dueDate?.before(Date()) == true
        binding.dateTv.setTextColor(if (isOverdue) Color.RED else Color.BLACK)

        binding.root.setOnClickListener { onClick(task) }

        return view
    }

    private fun Date?.toFormattedString(): String {
        return this?.let { dateFormatter.format(it) } ?: ""
    }
}
