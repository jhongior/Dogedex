package com.jhonw.dogedex.doglist

import com.jhonw.dogedex.model.Dog
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.api.DogsApi.retrofitService
import com.jhonw.dogedex.api.dto.AddDogToUserDTO
import com.jhonw.dogedex.api.dto.DogDTOMapper
import com.jhonw.dogedex.api.makeNetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Exception

class DogRepository {

    suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
        return withContext(Dispatchers.IO) {
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
            val dogListApiResponse = retrofitService.getAllDogs()
            val dogDTOList =
                dogListApiResponse.data.dogs//se regresa el ultimo valor que se ponga
            val dogDTOMapper = DogDTOMapper()
            //dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)//se comenta por que
            //ya no se devuelve una lista de dog sino un status y este success trae la lista
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }
    }

    suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> = makeNetworkCall {
        val addDogToUserDTO = AddDogToUserDTO(dogId)
        val defaultResponse = retrofitService.addDogToUser(addDogToUserDTO)

        if (!defaultResponse.isSuccess) {
            throw Exception(defaultResponse.message)
        }
    }

    private suspend fun getUserDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = retrofitService.getUserDogs()
        val dogDTOList = dogListApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }

    suspend fun getDogByMLId(mlDogId: String): ApiResponseStatus<Dog> = makeNetworkCall {
        val response = retrofitService.getDogByMLId(mlDogId)

        if (!response.isSuccess) {
            throw Exception(response.message)
        }

        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOToDogDomain(response.data.dog)
    }

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