package com.jhonw.dogedex.doglist

import com.jhonw.dogedex.R
import com.jhonw.dogedex.model.Dog
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.api.ApiService
import com.jhonw.dogedex.api.dto.AddDogToUserDTO
import com.jhonw.dogedex.api.dto.DogDTOMapper
import com.jhonw.dogedex.api.makeNetworkCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

interface DogTasks {
    suspend fun getDogCollection(): ApiResponseStatus<List<Dog>>
    suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any>
    suspend fun getDogByMLId(mlDogId: String): ApiResponseStatus<Dog>
    suspend fun getProbableDogs(probableDogsId: ArrayList<String>): Flow<ApiResponseStatus<Dog>>
}

class DogRepository @Inject constructor(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher
) : DogTasks {

    override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
        return withContext(dispatcher) {
            val allDogsListDeferred = async { downLoadDogs() }
            val userDogsListDeferred = async { getUserDogs() }

            /*
            el Deferred es asincrono y lo que hace es que empieza varias tareas al mismo tiempo pero espera que termine
            una para seguir con la otra, await espera que un proceso termine
             */
            val allDogsListResponse = allDogsListDeferred.await()
            val userDogsListResponse = userDogsListDeferred.await()

            if (allDogsListResponse is ApiResponseStatus.Error) {
                allDogsListResponse
            } else if (userDogsListResponse is ApiResponseStatus.Error) {
                userDogsListResponse
            } else if (allDogsListResponse is ApiResponseStatus.Success && userDogsListResponse is ApiResponseStatus.Success) {
                ApiResponseStatus.Success(
                    getCollectionList(
                        allDogsListResponse.data,
                        userDogsListResponse.data
                    )
                )
            } else {
                ApiResponseStatus.Error("Existe algun error")
            }
        }

    }

    private fun getCollectionList(allDogList: List<Dog>, userDogList: List<Dog>): List<Dog> {
        return allDogList.map {
            if (userDogList.contains(it)) {
                it
            } else {
                Dog(
                    0, it.index, "", "", "", "", "", "",
                    "", "", "", inCollection = false
                )
            }
        }.sorted()
    }

    private suspend fun downLoadDogs(): ApiResponseStatus<List<Dog>> /*List<Dog>*/ {
        //se usa la clase makeNetworkCall para manejar recursividad en caso de varias
        //peticiones a apis
        return makeNetworkCall {
            //todo lo siguiente se envia al call para que trate de ejecutar eso
            val dogListApiResponse = apiService.getAllDogs()
            val dogDTOList =
                dogListApiResponse.data.dogs//se regresa el ultimo valor que se ponga
            val dogDTOMapper = DogDTOMapper()
            //dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)//se comenta por que
            //ya no se devuelve una lista de dog sino un status y este success trae la lista
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }
    }

    override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> = makeNetworkCall {
        val addDogToUserDTO = AddDogToUserDTO(dogId)
        val defaultResponse = apiService.addDogToUser(addDogToUserDTO)

        if (!defaultResponse.isSuccess) {
            throw Exception(defaultResponse.message)
        }
    }

    private suspend fun getUserDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = apiService.getUserDogs()
        val dogDTOList = dogListApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }

    override suspend fun getDogByMLId(mlDogId: String): ApiResponseStatus<Dog> = makeNetworkCall {
        val response = apiService.getDogByMLId(mlDogId)

        if (!response.isSuccess) {
            throw Exception(response.message)
        }

        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOToDogDomain(response.data.dog)
    }

    //los Flow trabajan algo similar a las coroutines
    override suspend fun getProbableDogs(probableDogsId: ArrayList<String>): Flow<ApiResponseStatus<Dog>> =
        flow {
            //val dogDTOMapper = DogDTOMapper()

            //siempre hace una peticion a la api cada que recorre un item del for, hasta que no termine con un item no sigue con el otro
            for (mlDogId in probableDogsId) {

                val dog = getDogByMLId(mlDogId)
                emit(dog)
                //se comenta el codigo de este metodo debido a que se puede reutilizar el metodo getDogByMLId
                /*val response = apiService.getDogByMLId(mlDogId)

                if (response.isSuccess) {
                    emit(ApiResponseStatus.Success(dogDTOMapper.fromDogDTOToDogDomain(response.data.dog)))
                } else {
                    emit(ApiResponseStatus.Error(R.string.there_was_an_error.toString()))
                }*/
            }
        }.flowOn(dispatcher)//cuando implementamos flow tenemos que decirle en que dispatcher va a correr, en este caso IO que es para descargar datos, los flow
    //corren de abajo hacia arriba, es decir primero se ejecuta todo lo que este dentro del dispatcher

    /*.filter {//se pueden filtar diferentes datos con ayuda de esto
            it is ApiResponseStatus.Success
        }*/

    private fun getFakeDogs(): MutableList<Dog> {
        val dogList = mutableListOf<Dog>()
        dogList.add(
            Dog(
                1, 1, "Chihuahua", "Toy", "5.4",
                "6.7", "", "12 - 15", "", "10.5",
                "12.3"
            )
        )
        dogList.add(
            Dog(
                2, 1, "Labrador", "Toy", "5.4",
                "6.7", "", "12 - 15", "", "10.5",
                "12.3"
            )
        )
        dogList.add(
            Dog(
                3, 1, "Retriever", "Toy", "5.4",
                "6.7", "", "12 - 15", "", "10.5",
                "12.3"
            )
        )
        dogList.add(
            Dog(
                4, 1, "San Bernardo", "Toy", "5.4",
                "6.7", "", "12 - 15", "", "10.5",
                "12.3"
            )
        )
        dogList.add(
            Dog(
                5, 1, "Husky", "Toy", "5.4",
                "6.7", "", "12 - 15", "", "10.5",
                "12.3"
            )
        )
        dogList.add(
            Dog(
                6, 1, "Xoloscuincle", "Toy", "5.4",
                "6.7", "", "12 - 15", "", "10.5",
                "12.3"
            )
        )
        return dogList
    }
}