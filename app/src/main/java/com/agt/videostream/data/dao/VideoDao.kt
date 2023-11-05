package com.agt.videostream.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agt.videostream.data.VideoData
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVideo(vararg videos : VideoData)

    @Query("UPDATE ${VideoData.VideoTable} SET  status=:status WHERE id =:videoId")
    fun updateStatus(status: String,videoId: Int)

    @Query("UPDATE ${VideoData.VideoTable} SET  shortListed=:shortList WHERE id =:videoId")
    fun updateShortList(shortList: String,videoId: Int)

    @Query("SELECT * FROM ${VideoData.VideoTable} WHERE status=:status")
    fun getVideoByStatus(status: String):List<VideoData>
    @Query("SELECT * FROM ${VideoData.VideoTable}")
    fun getAllvideo() : List<VideoData>



}