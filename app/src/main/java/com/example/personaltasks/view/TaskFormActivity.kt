package com.example.personaltasks.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.ActivityTaskFormBinding
import com.example.personaltasks.model.Constants
import com.example.personaltasks.model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskFormActivity : AppCompatActivity() {

    private val binding: ActivityTaskFormBinding by lazy {
        ActivityTaskFormBinding.inflate(layoutInflater)
    }

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var selectedDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.subtitle = "New Task"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        onBackPressedDispatcher.addCallback(this) {
            setResult(RESULT_CANCELED)
            finish()
        }

        val task = getTaskFromIntent()

        if (task != null) {
            prepareView(task)
        }

        prepareButtonBehaviour(task)
    }

    private fun getTaskFromIntent(): Task? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.ADDITIONAL_TASK, Task::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.ADDITIONAL_TASK)
        }
    }

    private fun prepareView(task: Task) {
        supportActionBar?.subtitle = "Edit Task"
        with(binding) {
            editTitle.setText(task.title)
            editDescription.setText(task.description)
            textSelectedDate.text = formatDate(task.dueDate)
            selectedDate = task.dueDate

            val viewOnly = intent.getBooleanExtra(Constants.ADDITIONAL_VIEW_TASK, false)
            if (viewOnly) {
                supportActionBar?.subtitle = "View Task"
                editTitle.isEnabled = false
                editDescription.isEnabled = false
                textSelectedDate.isEnabled = false
                buttonContainer.isEnabled = false
                buttonSave.visibility = View.GONE
                buttonCancel.visibility = View.GONE
            }
        }
    }

    private fun prepareButtonBehaviour(receivedTask: Task?) {
        with(binding) {
            textSelectedDate.setOnClickListener {
                showDatePickerDialog()
            }

            buttonCancel.setOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }

            buttonSave.setOnClickListener {
                val task = Task(
                    id = receivedTask?.id ?: 0,
                    title = editTitle.text.toString(),
                    description = editDescription.text.toString(),
                    dueDate = selectedDate ?: Date()
                )

                Intent().apply {
                    putExtra(Constants.ADDITIONAL_TASK, task)
                    setResult(RESULT_OK, this)
                }

                finish()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        selectedDate?.let { calendar.time = it }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, y, m, d ->
            val pickedCalendar = Calendar.getInstance()
            pickedCalendar.set(y, m, d)
            selectedDate = pickedCalendar.time
            binding.textSelectedDate.text = formatDate(selectedDate)
        }, year, month, day).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                setResult(RESULT_CANCELED)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun formatDate(date: Date?): String {
        return date?.let { dateFormatter.format(it) } ?: ""
    }
}
