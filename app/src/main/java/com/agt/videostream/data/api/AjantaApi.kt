package com.agt.videostream.data.api

import com.agt.videostream.data.module.VideoModule
import com.agt.videostream.util.ApiResponce
import com.skydoves.sandwich.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AjantaApi {

    @GET("videos/{userId}")
    fun getVideo(@Path("userId") userid : String) : Call<ApiResponce<List<VideoModule>>>

    @GET("/videos/{videoId}/approve/{userId}")
    fun approveVideo(@Path("videoId")videoId : Int,@Path("userId")userId: String) : Call<String>

    @GET("/videos/{videoId}/reject/{userId}")
    fun rejectVideo(@Path("videoId")videoId : Int,@Path("userId")userId: String) :Call<String>


}