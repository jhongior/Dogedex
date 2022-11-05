package com.jhonw.dogedex.doglist

import com.jhonw.dogedex.model.Dog
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.api.DogsApi.retrofitService
import com.jhonw.dogedex.api.dto.DogDTOMapper
import com.jhonw.dogedex.api.makeNetworkCall

class DogRepository {

    suspend fun downLoadDogs(): ApiResponseStatus<List<Dog>> /*List<Dog>*/ {
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