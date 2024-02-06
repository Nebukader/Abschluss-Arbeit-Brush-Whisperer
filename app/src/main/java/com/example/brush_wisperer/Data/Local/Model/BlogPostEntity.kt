package com.example.brush_wisperer.Data.Local.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity ( tableName = "news_table")
data class BlogPostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "newsContent")
    val newsContent: String,
    @ColumnInfo(name = "postLink")
    val postLink: String,
    @ColumnInfo(name = "date")
    val date: String
)