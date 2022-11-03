package com.jhonw.dogedex.api

sealed class ApiResponseStatus<T>() {
    class Success<T>(val data: T) : ApiResponseStatus<T>()
    class Loading<T> : ApiResponseStatus<T>()
    class Error<T>(val message: String) : ApiResponseStatus<T>()
}

/*enum class ApiResponseStatus {
    LOADING,
    ERROR,
    SUCCESS
}*
/*el enum se reemplaza por el sealed class*/