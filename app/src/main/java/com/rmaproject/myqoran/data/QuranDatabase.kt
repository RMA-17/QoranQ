package com.rmaproject.myqoran.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.model.*

@Database(entities = arrayOf(Quran::class), views = arrayOf(Surah::class, Jozz::class, Page::class, AyahTerakhirFinder::class), version = 1)
abstract class QuranDatabase : RoomDatabase(){
    abstract fun quranDao(): QuranDao

    companion object {

        @Volatile private var INSTANCE: QuranDatabase? = null
        fun getInstance(context: Context): QuranDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): QuranDatabase {
            return Room.databaseBuilder(context.applicationContext, QuranDatabase::class.java, "quran.db")
                .createFromInputStream {
                    context.resources.openRawResource(R.raw.quran)
                }
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}