package com.rmaproject.myqoran.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.model.*

@Database(entities = [Bookmark::class], version = 2)
abstract class BookmarkDatabase : RoomDatabase(){
    abstract fun quranDao(): BookmarkDao

    companion object {

        @Volatile private var INSTANCE: BookmarkDatabase? = null
        fun getInstance(context: Context): BookmarkDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): BookmarkDatabase {
            return Room.databaseBuilder(context.applicationContext, BookmarkDatabase::class.java, "bookmark.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}