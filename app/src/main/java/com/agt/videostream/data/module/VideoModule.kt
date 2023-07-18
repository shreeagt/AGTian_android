package com.agt.videostream.data.module

import androidx.room.Entity
import com.agt.videostream.data.VideoData


data class VideoModule(
    val city: String,
    val empid: String,
    val id: Int,
    val name: String,
    val video_path: String
){

    fun toVideoData(): VideoData{
        return VideoData(
            id = id,
            name = name,
            city = city,
            empId = empid,
            url = video_path,
            shortListed = VideoData.ShorList.NONE.value,
            status = VideoData.VideoStatus.ALL.value
        )
    }

}