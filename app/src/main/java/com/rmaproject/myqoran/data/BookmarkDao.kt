package com.rmaproject.myqoran.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.rmaproject.myqoran.model.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("DELETE FROM bookmark")
    suspend fun deleteAllBookmark()

    @Insert
    suspend fun insertBookmark(bookmarkList: Bookmark)

    @Delete
    suspend fun deleteBookmark(bookmarkList: Bookmark)

    @Query("SELECT * FROM bookmark ORDER BY timeAdded DESC")
    fun getBookmarks(): Flow<List<Bookmark>>
}