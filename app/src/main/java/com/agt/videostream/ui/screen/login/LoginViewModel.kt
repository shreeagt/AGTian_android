package com.agt.videostream.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agt.videostream.data.api.AjantaApi
import com.agt.videostream.domain.repository.AjantaRepository
import com.agt.videostream.util.UserPreference
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.request
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AjantaRepository,
    private val api: AjantaApi,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _message = MutableSharedFlow<String>(1)
    val message: Flow<String> = _message
    private val _isLogin = MutableStateFlow(false)
    val isLogin: Flow<Boolean> = _isLogin


    fun logIn(empId: String) = viewModelScope.launch {
        api.getVideo(empId).request { response ->
            val login = response.getOrNull()
            var isLogin = false
            Log.w("TAG", "logIn: $login", )
            if (login != null) {
                isLogin = login.status
                if (isLogin) {
                    userPreference.setUserId(empId)
                }else{
                    setMessage("You have enter incorrect credentials")
                }
            }
            _isLogin.tryEmit(isLogin)
        }
    }

    fun setMessage(msg: String) = viewModelScope.launch {
        _message.emit(msg)
    }

}