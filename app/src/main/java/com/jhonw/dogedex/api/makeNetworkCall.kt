package com.jhonw.dogedex.api

import com.jhonw.dogedex.api.dto.DogDTOMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException

suspend fun <T> makeNetworkCall(call: suspend () -> T): ApiResponseStatus<T> {
    return withContext(Dispatchers.IO) {
        try {
            //el call trae todos los datos enviados desde el DogRepository para ejecutarsen
            ApiResponseStatus.Success(call())
        } catch (e: UnknownHostException) {
            ApiResponseStatus.Error("No hay conexión a internet")
        } catch (e: HttpException) {
            val errorMessage =
                if (e.code() == 401) {
                    "usuario o password incorrecto"
                } else {
                    "error desconocido"
                }
            ApiResponseStatus.Error(errorMessage)
        } catch (e: Exception) {
            val errorMessage = when (e.message) {
                "sign_up_error" -> "sign_up_error"
                "sign_in_error" -> "sign_in_error"
                "user_already_exists" -> "user_already_exists"
                else -> "error desconocido"
            }
            ApiResponseStatus.Error(errorMessage)
        }

        //getFakeDogs()
    }
}