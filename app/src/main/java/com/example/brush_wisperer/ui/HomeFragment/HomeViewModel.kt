package com.example.brush_wisperer.ui.HomeFragment


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.brush_wisperer.Data.Local.Model.BlogPostEntity
import com.example.brush_wisperer.Data.Local.Model.Database.BlogPostDatabaseInstance
import com.example.brush_wisperer.Data.RepositoryBlogPostNews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel (application: Application): AndroidViewModel(application) {

    private val repository = RepositoryBlogPostNews(BlogPostDatabaseInstance.getDatabase(application))

    val news: LiveData<List<BlogPostEntity>> = repository.allNews

    fun insertNews(){
        viewModelScope.launch(Dispatchers.IO) {
            val news = repository.scrapeBlogPost("/blogs/explore/tagged/news")
            for (each in news) {
                Log.d("TAG", "News: $each")
                repository.insertNews(each)
            }
        }
    }

    fun getNews(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.scrapeWebPage()
            val news = repository.scrapeBlogPost("/blogs/explore/tagged/news")
            Log.d("TAG", "News: $news")

        }
    }
}



