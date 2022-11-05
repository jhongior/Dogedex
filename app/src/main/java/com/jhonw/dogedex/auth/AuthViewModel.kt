package com.jhonw.dogedex.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.model.User
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        //el LiveData se puede leer pero no editar
        get() = _user

    private val _status = MutableLiveData<ApiResponseStatus<User>>()
    val status: LiveData<ApiResponseStatus<User>>
        get() = _status

    private val authRepository = AuthRepository()

    fun signUp(email: String, password: String, passwordConfirmation: String) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(authRepository.signUp(email, password, passwordConfirmation))
        }
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<User>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _user.value = apiResponseStatus.data
        }
        _status.value = apiResponseStatus
    }
}