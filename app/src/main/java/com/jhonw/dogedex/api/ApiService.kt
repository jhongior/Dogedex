package com.jhonw.dogedex.api

import com.jhonw.dogedex.*
import com.jhonw.dogedex.api.dto.AddDogToUserDTO
import com.jhonw.dogedex.api.dto.LoginDTO
import com.jhonw.dogedex.api.dto.SignUpDTO
import com.jhonw.dogedex.api.responses.DogListApiResponse
import com.jhonw.dogedex.api.responses.AuthApiResponse
import com.jhonw.dogedex.api.responses.DefaultResponse
import com.jhonw.dogedex.api.responses.DogApiResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private val okHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(ApiServiceInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface ApiService {
    @GET(GET_ALL_DOGS)
    suspend fun getAllDogs(): DogListApiResponse//convierte el json a traves de Moshi

    @POST(POST_SIGN_UP_URL)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AuthApiResponse

    @POST(POST_SIGN_IN_URL)
    suspend fun login(@Body loginDTO: LoginDTO): AuthApiResponse

    @Headers("${NEEDS_AUTH_HEADER_KEY}: true")//un interceptor usado para reconocer el token
    @POST(ADD_DOG_TO_USER_URL)
    suspend fun addDogToUser(@Body addDogToUserDTO: AddDogToUserDTO): DefaultResponse

    @Headers("${NEEDS_AUTH_HEADER_KEY}: true")//devuelve la coleccion del usuario que tenga el token del interceptor
    @GET(GET_USER_DOGS_URL)
    suspend fun getUserDogs(): DogListApiResponse

    @GET(GET_DOG_BY_ML_ID)
    suspend fun getDogByMLId(@Query("ml_id") mlId: String): DogApiResponse
}

object DogsApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}