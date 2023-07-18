package com.agt.videostream.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agt.videostream.data.VideoData.Companion.VideoTable

@Entity(tableName = VideoTable )
data class VideoData(
    @PrimaryKey
    val id : Int,
    val name : String,
    val url : String,
    val city: String,
    val empId: String,
    val shortListed : String,
    val status :String?
){
    companion object{
        const val VideoTable = "Video_Table"
    }

    enum class VideoStatus(val value : String){
        ALL(""),APPROVE("Approve"),REJECT("Reject")
    }
    enum class ShorList(val value :String){
        TRUE("true"),FALSE("false"),NONE("none")
    }

}

