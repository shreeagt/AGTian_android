package com.agt.videostream.util



data class ApiResponce<T>(
    var data: T?,
    val status: Boolean,
    val message: String
)
