package com.jhonw.dogedex.api

import com.jhonw.dogedex.BASE_URL
import com.jhonw.dogedex.Dog
import com.jhonw.dogedex.GET_ALL_DOGS
import com.jhonw.dogedex.api.responses.DogListApiResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface ApiService {
    @GET(GET_ALL_DOGS)
    suspend fun getAllDogs(): DogListApiResponse//convierte el json a traves de Moshi
}

object DogsApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}