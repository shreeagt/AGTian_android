package com.agt.videostream.ui.screen.shortlist

import androidx.lifecycle.ViewModel
import com.agt.videostream.data.api.AjantaApi
import com.agt.videostream.data.database.VideoDataBase
import com.agt.videostream.util.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShortListviewModel @Inject constructor(
    private val api : AjantaApi,
    private val dataBase: VideoDataBase,
    private val userPreference: UserPreference
) : ViewModel() {











}