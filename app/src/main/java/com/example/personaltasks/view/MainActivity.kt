package com.example.personaltasks.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.mutableIntListOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapter.TaskRecyclerViewAdapter
import com.example.personaltasks.controller.TaskController
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Constants.ADDITIONAL_TASK
import com.example.personaltasks.model.Constants.ADDITIONAL_VIEW_TASK
import com.example.personaltasks.model.Task
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(),onClickListener{

    private val activityBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val taskList: MutableList<Task> = mutableListOf()

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val mainController: TaskController by lazy{
        TaskController(this)
    }

    private val taskAdapter: TaskRecyclerViewAdapter by lazy {
        TaskRecyclerViewAdapter(
            taskList,
            onTaskClick = { position -> onTaskClick(position) },
            onTaskContextMenu = { position, menu -> onTaskContextMenu(position, menu) }
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityBinding.root)

        setSupportActionBar(activityBinding.toolbarIn.toolbar)

        activityBinding.taskRv.layoutManager = LinearLayoutManager(this)

        activityBinding.taskRv.adapter = taskAdapter

        registerForContextMenu(activityBinding.taskRv)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(ADDITIONAL_TASK, Task::class.java)
                } else {
                    result.data?.getParcelableExtra(ADDITIONAL_TASK, Task::class.java)
                }

                task?.let { receivedTask ->
                    val position = taskList.indexOfFirst { it.id == receivedTask.id }
                    if (position == -1) {
                        taskList.add(receivedTask)
                        thread {
                            mainController.createTask(receivedTask)
                        }
                        taskAdapter.notifyItemInserted(taskList.size - 1)
                    } else {
                        taskList[position] = receivedTask
                        thread {
                            mainController.updateTask(receivedTask)
                        }
                        taskAdapter.notifyItemChanged(position)
                    }
                }
            }
        }

            fillTaskList()
    }

    private fun fillTaskList(){
        taskList.clear()

        thread {
            val storedTasks = mainController.getTasks()

            runOnUiThread{
                taskList.addAll(storedTasks)
                taskAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_task_mi -> {
                resultLauncher.launch(Intent(this, TaskFormActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTaskClick(position: Int) {
        Intent(this, TaskFormActivity::class.java).apply {
            putExtra(ADDITIONAL_TASK, taskList[position])
            putExtra(ADDITIONAL_VIEW_TASK, true)
            startActivity(this)
        }
    }

    override fun onTaskContextMenu(position: Int, menu: ContextMenu) {
        menu.add("Editar").setOnMenuItemClickListener {
            val intent = Intent(this, TaskFormActivity::class.java).apply {
                putExtra(ADDITIONAL_TASK, taskList[position])
            }
            resultLauncher.launch(intent)
            true
        }

        menu.add("Remover").setOnMenuItemClickListener {
            val taskToRemove = taskList.removeAt(position)
            mainController.deleteTask(taskToRemove)
            taskAdapter.notifyItemRemoved(position)
            true
        }
    }
}