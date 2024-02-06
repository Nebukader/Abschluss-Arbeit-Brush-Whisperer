package com.example.brush_wisperer.Data.Local.Model.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.brush_wisperer.Data.Local.Model.ColourEntity

@Database(entities = [ColourEntity::class], version = 1)
abstract class ColourDatabase : RoomDatabase() {
    abstract val dao: ColourDao
}

object ColourDatabaseInstance {
    private lateinit var INSTANCE: ColourDatabase

    fun getDatabase(context: Context): ColourDatabase {
        synchronized(ColourDatabase::class.java) {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ColourDatabase::class.java,
                    "colour_table"
                ).build()
            }
        }
        return INSTANCE
    }
}
