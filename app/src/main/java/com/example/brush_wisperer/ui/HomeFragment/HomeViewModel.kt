package com.example.brush_wisperer.ui.HomeFragment


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode

class HomeViewModel : ViewModel() {

    fun scrapeWebPage() = viewModelScope.launch {
    withContext(Dispatchers.IO) {
        val doc = Jsoup.connect("https://thearmypainter.com/blogs/explore").get()
        val elements = doc.select("li[role=tab] a")
        elements.forEach { element ->
            val postLink = element.attr("href")
            Log.d("TAG", "Post link: $postLink")
        }
    }
}
    fun scrapeBlogPost(postLink: String) = viewModelScope.launch {
    withContext(Dispatchers.IO) {
        val doc = Jsoup.connect("https://thearmypainter.com$postLink").get()
        val blogPost =doc.select("blog-post-card")
        blogPost.forEach {post ->
            val title = post.select("a.blog-post__figure").text()
            val image = post.select("img.zoom-image").attr("src")
            val text = post.select("p").text()
        Log.d("TAG", "Title: $title, Image: $image, Text: $text")
    }
}
}
}



