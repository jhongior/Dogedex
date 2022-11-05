package com.jhonw.dogedex.api

import com.jhonw.dogedex.BASE_URL
import com.jhonw.dogedex.GET_ALL_DOGS
import com.jhonw.dogedex.POST_SIGN_UP_URL
import com.jhonw.dogedex.api.dto.SignUpDTO
import com.jhonw.dogedex.api.responses.DogListApiResponse
import com.jhonw.dogedex.api.responses.SignUpApiResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface ApiService {
    @GET(GET_ALL_DOGS)
    suspend fun getAllDogs(): DogListApiResponse//convierte el json a traves de Moshi

    @POST(POST_SIGN_UP_URL)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): SignUpApiResponse
}

object DogsApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}