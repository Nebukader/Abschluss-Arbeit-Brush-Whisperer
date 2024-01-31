package com.example.brush_wisperer.ui.HomeFragment


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import org.jsoup.Jsoup

class HomeViewModel : ViewModel() {

    fun scrapeWebPage() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val doc = Jsoup.connect("https://thearmypainter.com/blogs/explore").get()
            val title = doc.title()
            Log.d("TAG", "Title: $title")
        }

    }

}


