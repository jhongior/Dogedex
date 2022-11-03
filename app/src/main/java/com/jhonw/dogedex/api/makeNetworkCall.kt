package com.jhonw.dogedex.api

import com.jhonw.dogedex.api.dto.DogDTOMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

suspend fun <T> makeNetworkCall(call: suspend () -> T) : ApiResponseStatus<T> {
    return withContext(Dispatchers.IO) {
        try {
            //el call trae todos los datos enviados desde el DogRepository para ejecutarsen
            ApiResponseStatus.Success(call())
        } catch (e: UnknownHostException) {
            ApiResponseStatus.Error("No hay conexión a internet")
        } catch (e: Exception) {
            ApiResponseStatus.Error("Error desconocido")
        }

        //getFakeDogs()
    }
}