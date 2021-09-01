package one.digitalinnovation.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import one.digitalinnovation.todolist.databinding.ActivityAddNewTaskBinding
import one.digitalinnovation.todolist.datasource.TaskDataSource
import one.digitalinnovation.todolist.extensions.format
import one.digitalinnovation.todolist.extensions.text
import one.digitalinnovation.todolist.model.Task
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)){
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.inputTitle.text = it.title
                binding.inputDate.text = it.date
                binding.inputTime.text = it.time

            }
        }

        insertListeners()
    }

    private fun insertListeners(){
        binding.inputDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.inputDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.inputTime.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.inputTime.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager,null)
        }

        binding.btnCreateTask.setOnClickListener {
            val task = Task(
                title = binding.inputTitle.text,
                description = binding.inputDescription.text,
                date = binding.inputDate.text,
                time = binding.inputTime.text,
                id = intent.getIntExtra(TASK_ID,0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    companion object{
        const val TASK_ID = "task_id"
    }
}
