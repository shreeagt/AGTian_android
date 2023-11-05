package com.agt.videostream.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agt.videostream.data.VideoData
import com.agt.videostream.data.api.AjantaApi
import com.agt.videostream.data.database.VideoDataBase
import com.agt.videostream.util.UserPreference
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.request
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val api: AjantaApi,
    private val preference: UserPreference,
    private val dataBase: VideoDataBase
) : ViewModel() {

    private val _message = MutableSharedFlow<String>()
    val message = _message

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading
    private val allVideos = MutableStateFlow<List<VideoData>>(emptyList())
    val videoList = MutableStateFlow<List<VideoData>>(emptyList())
    val approveVideo = MutableStateFlow<List<VideoData>>(emptyList())
    val shortList = MutableStateFlow<List<VideoData>>(emptyList())
    val rejectVideo = MutableStateFlow<List<VideoData>>(emptyList())
    val totalApproveVideo = MutableStateFlow<Int>(0)
    val isDialogeShow = MutableStateFlow(false)

    init {
        getAllVideo()
    }

    fun showDialoge() = viewModelScope.launch {
        isDialogeShow.emit(true)
    }
    fun hideDialoge()= viewModelScope.launch {
        isDialogeShow.emit(false)
    }

    fun getAllVideo() = viewModelScope.launch(Dispatchers.IO) {
        val videos = dataBase.getDao().getAllvideo()
        if (videos.isNotEmpty()) {
            filterVideo(videos)
            _isLoading.emit(false)
        }
        api.getVideo(preference.getUserId() ?: "").request { responce ->
            _isLoading.tryEmit(false)
            when (responce) {
                is ApiResponse.Failure.Error -> {
                    setMessage("There something wrong! can't process the request ")
                }

                is ApiResponse.Failure.Exception -> {
                    setMessage("Something Went go wrong !")
                }

                is ApiResponse.Success -> {
                    val res = responce.data.data!!
                    val videodata = res.map { it.toVideoData() }
                    CoroutineScope(Dispatchers.IO).launch {
                        dataBase.getDao().insertVideo(*videodata.toTypedArray())
                    }
                    run {
//                        videoList.tryEmit(videodata)
//                        shortList.tryEmit(videodata.filter { it.status == VideoData.VideoStatus.APPROVE.value })
//                        rejectVideo.tryEmit(videodata.filter { it.status == VideoData.VideoStatus.REJECT.value })
                        filterVideo(videos)
                    }

                }
            }
        }
    }


    fun filterVideo(videos: List<VideoData>) = viewModelScope.launch {
        allVideos.emit(videos)
        videoList.emit(videos.filter { it.shortListed == VideoData.ShorList.FALSE.value || it.shortListed == VideoData.ShorList.NONE.value && it.status == VideoData.VideoStatus.ALL.value })
        shortList.emit(videos.filter { it.shortListed == VideoData.ShorList.TRUE.value && it.status == VideoData.VideoStatus.ALL.value })
        rejectVideo.emit(videos.filter { it.status == VideoData.VideoStatus.REJECT.value })
        approveVideo.emit(videos.filter { it.status == VideoData.VideoStatus.APPROVE.value })
        totalApproveVideo.emit(videos.filter { it.status == VideoData.VideoStatus.APPROVE.value }.size)
        _isLoading.emit(false)
    }

    fun setMessage(msg: String) = viewModelScope.launch {
        _message.emit(msg)
    }

}