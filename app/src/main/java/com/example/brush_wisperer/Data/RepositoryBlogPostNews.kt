package com.example.brush_wisperer.Data

import android.util.Log
import com.example.brush_wisperer.Data.Local.Model.BlogPostEntity
import com.example.brush_wisperer.Data.Local.Model.Database.BlogPostDatabase
import org.jsoup.Jsoup
import java.time.LocalDate

class RepositoryBlogPostNews(private val database: BlogPostDatabase) {

    val allNews = database.dao.getAllNews()

    suspend fun insertNews(news: BlogPostEntity) {
        database.dao.insertBlogPost(news)
    }

    suspend fun deleteAllNews() {
        database.dao.deleteAllNews()
    }

    fun scrapeWebPage() {
        val doc = Jsoup.connect("https://thearmypainter.com/blogs/explore").get()
        val elements = doc.select("li[role=tab] a")
        elements.forEach { element ->
            val postLink = element.attr("href")
            Log.d("TAG", "Post link: $postLink")
        }
    }
    fun scrapeBlogPost(postLink: String): List<BlogPostEntity> {
        val doc = Jsoup.connect("https://thearmypainter.com$postLink").get()
        val blogPost = doc.select("blog-post-card")
        val blogPostEntities = mutableListOf<BlogPostEntity>()
        blogPost.forEach { post ->
            val title = post.select("a.blog-post-card__figure img").attr("alt")
            val image = post.select("img.zoom-image").attr("src")
            val text = post.select("p").text()

            val postLink = post.select("a.blog-post-card__figure").attr("href")
            val processedPostLink = "https://thearmypainter.com$postLink"

            val processedImageLink = "https:${image.substringAfter("Image: ").substringBefore("?") }"
            val date = LocalDate.now().toString()
            val news = BlogPostEntity(
                0,
                title,
                processedImageLink,
                text,
                processedPostLink,
                date
            )
            blogPostEntities.add(news)
            Log.d("TAG", "PostLink : $postLink")
        }
        return blogPostEntities
    }
}

