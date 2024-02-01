package com.example.brush_wisperer.Data.Local.Model.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.brush_wisperer.Data.Local.Model.BlogPostEntity

@Database(entities = [BlogPostEntity::class], version = 1)
abstract class BlogPostDatabase : RoomDatabase() {
    abstract val dao: BlogPostDao
}

object BlogPostDatabaseInstance {
    private lateinit var INSTANCE: BlogPostDatabase

    fun getDatabase(context: Context): BlogPostDatabase {
        synchronized(BlogPostDatabase::class.java) {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    BlogPostDatabase::class.java,
                    "news_table"
                ).build()
            }
        }
        return INSTANCE
    }
}