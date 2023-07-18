package com.agt.videostream.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agt.videostream.data.VideoData
import com.agt.videostream.data.dao.VideoDao


@Database(entities = [VideoData::class], version = 1)
abstract class VideoDataBase : RoomDatabase() {
    abstract fun getDao(): VideoDao
}