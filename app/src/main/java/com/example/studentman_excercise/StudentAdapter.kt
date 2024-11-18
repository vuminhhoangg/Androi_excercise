package com.example.studentman_excercise

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(val students: List<StudentModel>): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
        val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
        val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
        val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item,
            parent, false)
        return StudentViewHolder(itemView)
    }

    override fun getItemCount(): Int = students.size

    /*override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]

        holder.textStudentName.text = student.studentName
        holder.textStudentId.text = student.studentId
    }*/

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]

        holder.textStudentName.text = student.studentName
        holder.textStudentId.text = student.studentId

        // Xử lý khi nhấn nút Edit
        holder.imageEdit.setOnClickListener {
            // Hiển thị dialog để chỉnh sửa thông tin
            showEditDialog(holder.itemView.context, student, position)
        }

        // Xử lý khi nhấn nút Delete
        holder.imageRemove.setOnClickListener {
            showDeleteDialog(holder.itemView.context, student, position)
        }
    }

    private fun showEditDialog(context: Context, student: StudentModel, position: Int) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_edit_student, null)
        builder.setView(dialogView)

        val editName = dialogView.findViewById<EditText>(R.id.edit_name)
        val editId = dialogView.findViewById<EditText>(R.id.edit_id)

        // Hiển thị thông tin sinh viên hiện tại
        editName.setText(student.studentName)
        editId.setText(student.studentId)

        builder.setTitle("Edit Student")
        builder.setPositiveButton("Save") { _, _ ->
            val newName = editName.text.toString()
            val newId = editId.text.toString()

            if (newName.isNotEmpty() && newId.isNotEmpty()) {
                // Cập nhật thông tin sinh viên
                val updatedStudent = StudentModel(newName, newId)
                (students as MutableList)[position] = updatedStudent
                notifyItemChanged(position) // Cập nhật lại item trong RecyclerView
            }
        }
        builder.setNegativeButton("Cancel", null)

        builder.create().show()
    }

    private fun showDeleteDialog(context: Context, student: StudentModel, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Student")
        builder.setMessage("Are you sure you want to delete ${student.studentName}?")
        builder.setPositiveButton("Delete") { _, _ ->
            deleteStudent(context, student, position)
        }
        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }

    private fun deleteStudent(context: Context, student: StudentModel, position: Int) {
        val mutableStudents = students.toMutableList()
        mutableStudents.removeAt(position)
        notifyItemRemoved(position)

        // Hiển thị AppCompatActivity
        val parentView = (context as AppCompatActivity).findViewById<View>(android.R.id.content)
        Snackbar.make(parentView, "${student.studentName} has been deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                // Khôi phục sinh viên khi nhấn Undo
                mutableStudents.add(position, student)
                notifyItemInserted(position)
            }
            .show()

        // Cập nhật lại danh sách
        (students as MutableList).clear()
        (students as MutableList).addAll(mutableStudents)
    }

}