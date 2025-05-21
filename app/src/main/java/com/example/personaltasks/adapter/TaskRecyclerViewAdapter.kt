package com.example.personaltasks.adapter

import com.example.personaltasks.databinding.TaskCardBinding
import com.example.personaltasks.model.Task



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskRecyclerViewAdapter(
    private val taskList: MutableList<Task>,
    private val onTaskClick: (Int) -> Unit,
    private val onTaskContextMenu: (Int, android.view.ContextMenu) -> Unit
) : RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder>() {

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    inner class TaskViewHolder(val binding: TaskCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onTaskClick(position)
                }
            }

            binding.root.setOnCreateContextMenuListener { menu, _, _ ->
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onTaskContextMenu(position, menu)
                }
            }
        }

        fun bind(task: Task) {
            binding.titleTv.text = task.title
            binding.descriptionTv.text = task.description
            binding.dateTv.text = formatDate(task.dueDate)
        }

        private fun formatDate(date: Date?): String {
            return date?.let { dateFormatter.format(it) } ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int = taskList.size
}
