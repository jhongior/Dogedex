package com.jhonw.dogedex

import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.api.ApiService
import com.jhonw.dogedex.api.dto.AddDogToUserDTO
import com.jhonw.dogedex.api.dto.DogDTO
import com.jhonw.dogedex.api.dto.LoginDTO
import com.jhonw.dogedex.api.dto.SignUpDTO
import com.jhonw.dogedex.api.responses.*
import com.jhonw.dogedex.doglist.DogRepository
import com.jhonw.dogedex.model.Dog
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.UnknownHostException

class DogRepositoryTest {

    /*
    runBlocking se usa cuando hay funciones suspend, es decir que se esta manejando un hilo
     */
    @Test
    fun testGetDogCollectionSuccess(): Unit = runBlocking {

        class FakeApiService : ApiService {
            override suspend fun getAllDogs(): DogListApiResponse {
                return DogListApiResponse(
                    message = "",
                    isSuccess = true,
                    data = DogListResponse(
                        dogs =
                        listOf(
                            DogDTO(
                                1, 1, "Wartoortle", "", "", "", "", "",
                                "", "", ""
                            ),
                            DogDTO(
                                19, 2, "Charmeleon", "", "", "", "", "",
                                "", "", ""
                            )
                        )
                    )
                )
            }

            override suspend fun signUp(signUpDTO: SignUpDTO): AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: LoginDTO): AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: AddDogToUserDTO): DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserDogs(): DogListApiResponse {
                return DogListApiResponse(
                    message = "",
                    isSuccess = true,
                    data = DogListResponse(
                        dogs =
                        listOf(
                            DogDTO(
                                19, 2, "Charmeleon", "", "", "", "", "",
                                "", "", ""
                            )
                        )
                    )
                )
            }

            override suspend fun getDogByMLId(mlId: String): DogApiResponse {
                TODO("Not yet implemented")
            }

        }

        val dogRepository = DogRepository(
            apiService = FakeApiService(),
            dispatcher = TestCoroutineDispatcher()
        )

        val apiResponseStatus =
            dogRepository.getDogCollection()//si muestra error toca agregar runBloking al iniciar la clase y lo esto lo que hace
        //es decirle que espere mientras se ejecuta otro hilo

        assert(apiResponseStatus is ApiResponseStatus.Success)
        val dogCollection = (apiResponseStatus as ApiResponseStatus.Success).data
        assertEquals(2, dogCollection.size)
        assertEquals("Charmeleon", dogCollection[1].name)
        assertEquals("", dogCollection[0].name)
    }

    @Test
    fun testGetAllDogsError(): Unit = runBlocking {

        class FakeApiService : ApiService {
            override suspend fun getAllDogs(): DogListApiResponse {
                throw UnknownHostException()
            }

            override suspend fun signUp(signUpDTO: SignUpDTO): AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: LoginDTO): AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: AddDogToUserDTO): DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserDogs(): DogListApiResponse {
                return DogListApiResponse(
                    message = "",
                    isSuccess = true,
                    data = DogListResponse(
                        dogs =
                        listOf(
                            DogDTO(
                                19, 2, "Charmeleon", "", "", "", "", "",
                                "", "", ""
                            )
                        )
                    )
                )
            }

            override suspend fun getDogByMLId(mlId: String): DogApiResponse {
                TODO("Not yet implemented")
            }

        }

        val dogRepository = DogRepository(
            apiService = FakeApiService(),
            dispatcher = TestCoroutineDispatcher()
        )

        val apiResponseStatus =
            dogRepository.getDogCollection()//si muestra error toca agregar runBloking al iniciar la clase y lo esto lo que hace
        //es decirle que espere mientras se ejecuta otro hilo

        assert(apiResponseStatus is ApiResponseStatus.Error)
        assertEquals(
            "No hay conexión a internet",
            (apiResponseStatus as ApiResponseStatus.Error).message
        )
    }

    @Test
    fun getDogByMlSuccess() = runBlocking {
        class FakeApiService : ApiService {
            override suspend fun getAllDogs(): DogListApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(signUpDTO: SignUpDTO): AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: LoginDTO): AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: AddDogToUserDTO): DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserDogs(): DogListApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMLId(mlId: String): DogApiResponse {
                return DogApiResponse(
                    message = "",
                    isSuccess = true,
                    data = DogResponse(
                        dog = DogDTO(
                            19, 2, "Charmeleon", "", "", "", "", "",
                            "", "", ""
                        )
                    )
                )
            }

        }

        val dogRepository = DogRepository(
            apiService = FakeApiService(),
            dispatcher = TestCoroutineDispatcher()
        )

        val apiResponseStatus = dogRepository.getDogByMLId("1554")
        assert(apiResponseStatus is ApiResponseStatus.Success)
        assertEquals(19, (apiResponseStatus as ApiResponseStatus.Success).data.id)
    }

    @Test
    fun getDogByMlError() = runBlocking {
        class FakeApiService : ApiService {
            override suspend fun getAllDogs(): DogListApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(signUpDTO: SignUpDTO): AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun login(loginDTO: LoginDTO): AuthApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun addDogToUser(addDogToUserDTO: AddDogToUserDTO): DefaultResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getUserDogs(): DogListApiResponse {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMLId(mlId: String): DogApiResponse {
                return DogApiResponse(
                    message = "error obteniendo el perro",
                    isSuccess = false,
                    data = DogResponse(
                        dog = DogDTO(
                            19, 2, "Charmeleon", "", "", "", "", "",
                            "", "", ""
                        )
                    )
                )
            }

        }

        val dogRepository = DogRepository(
            apiService = FakeApiService(),
            dispatcher = TestCoroutineDispatcher()
        )

        val apiResponseStatus = dogRepository.getDogByMLId("1554")
        assert(apiResponseStatus is ApiResponseStatus.Error)
        assertEquals("error desconocido", (apiResponseStatus as ApiResponseStatus.Error).message)
    }
}