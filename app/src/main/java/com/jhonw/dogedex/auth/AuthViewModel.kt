package com.jhonw.dogedex.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhonw.dogedex.R
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.model.User
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    var user = mutableStateOf<User?>(null)
        private set

    var emailError = mutableStateOf<Int?>(null)
        private set

    var passwordError = mutableStateOf<Int?>(null)
        private set

    var confirmPasswordError = mutableStateOf<Int?>(null)
        private set

    var status = mutableStateOf<ApiResponseStatus<User>?>(null)
        private set

    private val authRepository = AuthRepository()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            handleResponseStatus(authRepository.login(email, password))
        }
    }

    fun signUp(
        email: String,
        password: String,
        passwordConfirmation: String
    ) {

        if (email.isEmpty()) {
            emailError.value = R.string.email_no_valido
        } else if (password.isEmpty()) {
            passwordError.value = R.string.password_incorrecto
        } else if (passwordConfirmation.isEmpty()) {
            confirmPasswordError.value = R.string.password_incorrecto
        } else if (password != passwordConfirmation) {
            passwordError.value = R.string.password_no_coinciden
            confirmPasswordError.value = R.string.password_no_coinciden
        } else {

            viewModelScope.launch {
                status.value = ApiResponseStatus.Loading()
                handleResponseStatus(authRepository.signUp(email, password, passwordConfirmation))
            }
        }
    }

    fun resetErrors() {
        emailError.value = null
        passwordError.value = null
        confirmPasswordError.value = null
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<User>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            user.value = apiResponseStatus.data!!
        }
        status.value = apiResponseStatus
    }

    fun resetApiResponseStatus() {
        status.value = null
    }
}