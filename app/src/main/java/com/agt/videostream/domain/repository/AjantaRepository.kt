package com.agt.videostream.domain.repository

import com.agt.videostream.data.VideoData
import com.agt.videostream.data.api.AjantaApi
import com.agt.videostream.data.database.VideoDataBase
import com.agt.videostream.util.UserPreference
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.getOrElse
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.request
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class AjantaRepository constructor(
    private val api: AjantaApi,
    private val database: VideoDataBase,
    private val preference: UserPreference,

    ) {


    suspend fun getVideos(message: MutableSharedFlow<String>) =
        withContext(Dispatchers.IO) {
            /*    var videos: List<VideoData>
                videos = database.getDao().getVideoByStatus("")
                if (videos.isNotEmpty()) {
                    return@withContext videos
                }
                api.getVideo(preference.getUserId()!!).request { response ->
                    when (response) {
                        is ApiResponse.Failure.Error -> {
                            when (response.statusCode) {
                                StatusCode.InternalServerError -> {
                                    message.tryEmit("Internal Server Error")
                                }

                                StatusCode.BadGateway -> {
                                    message.tryEmit("Bad Gateway Error")
                                }

                                else -> {
                                    message.tryEmit("Some Unknown Error Occur ")
                                }
                            }

                        }

                        is ApiResponse.Failure.Exception -> {
                            message.tryEmit("Something went go wrong")
                        }

                        is ApiResponse.Success -> {
                            val responseVideo = response.data.data!!
                            videos = responseVideo.map { it.toVideoData() }
                            database.getDao().insertVideo(*videos.toTypedArray())
                        }
                    }
                }

                return@withContext videos*/
        }




}