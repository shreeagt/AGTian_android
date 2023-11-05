package com.agt.videostream.ui.screen.video

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.agt.videostream.data.VideoData
import com.agt.videostream.data.api.AjantaApi
import com.agt.videostream.data.database.VideoDataBase
import com.agt.videostream.util.ScreenState
import com.agt.videostream.util.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import retrofit2.awaitResponse
import javax.inject.Inject


@HiltViewModel
class VideoViewModel @Inject constructor(
    private val api: AjantaApi,
    private val database: VideoDataBase,
    private val userPreference: UserPreference
) : ViewModel() {


    private val _screenState = MutableStateFlow<ScreenState<VideoData>>(ScreenState.Loading)
    val screenState = _screenState

    private val _message = MutableSharedFlow<String>()
    val message = _message

    private val _allvideoList = MutableStateFlow<List<VideoData>>(emptyList())
    val allvideo = _allvideoList

    val totalApproveVideo = MutableStateFlow<Int>(0)

    init {
        getAllVideo()
    }

    fun getAllVideo() = viewModelScope.launch(Dispatchers.IO) {
        val videos = database.getDao().getAllvideo()
        _allvideoList.emit(videos)
        totalApproveVideo.emit(videos.filter { it.status == VideoData.VideoStatus.APPROVE.value }.size)
    }

    fun onSetUP(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        delay(1000)
        val video = _allvideoList.value.indexOfFirst { it.id == id }
        _screenState.emit(ScreenState.Success(allvideo.value[video]))
    }

    fun shortList(videoId: Int) = viewModelScope.launch(Dispatchers.IO) {
        database.getDao().updateShortList(VideoData.ShorList.TRUE.value, videoId)
    }

    fun onApprove(videoId: Int) = viewModelScope.launch(Dispatchers.IO) {
        api.approveVideo(videoId, userPreference.getUserId()!!).awaitResponse()
        database.getDao().updateStatus(VideoData.VideoStatus.APPROVE.value, videoId)
//        if (totalApproveVideo.value == 1) {
            val restVideo = allvideo.value.filter { it.status == VideoData.VideoStatus.ALL.value && it.id != videoId}
            restVideo.forEach {
                onReject(it.id)
            }
//        }
    }

    fun onReject(videoId: Int) = viewModelScope.launch(Dispatchers.IO) {
        api.rejectVideo(videoId, userPreference.getUserId()!!).awaitResponse()
        database.getDao().updateStatus(VideoData.VideoStatus.REJECT.value, videoId)

    }


}