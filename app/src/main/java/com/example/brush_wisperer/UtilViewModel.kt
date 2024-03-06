package com.example.brush_wisperer

import android.app.Application
import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel

class UtilViewModel(application: Application) : AndroidViewModel(application) {

    fun showToastAtTop(context: Context, message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }
}

