package com.agt.videostream.util


sealed class ScreenState<out T> {

    object Loading : ScreenState<Nothing>()
    data class Success<T>(val data: T) : ScreenState<T>()
    data class Error(val message: String) : ScreenState<Nothing>()

}
