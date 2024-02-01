package com.example.brush_wisperer.Data.Local.Model.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.brush_wisperer.Data.Local.Model.BlogPostEntity

@Dao
interface BlogPostDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlogPost(blogPostEntity: BlogPostEntity)

    @Query("SELECT * FROM news_table")
    fun getAllNews(): LiveData<List<BlogPostEntity>>

    @Query("DELETE FROM news_table")
    suspend fun deleteAllNews()

}