package com.jhonw.dogedex.auth

import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.api.DogsApi
import com.jhonw.dogedex.api.dto.DogDTOMapper
import com.jhonw.dogedex.api.dto.SignUpDTO
import com.jhonw.dogedex.api.dto.UserDTOMapper
import com.jhonw.dogedex.api.makeNetworkCall
import com.jhonw.dogedex.model.User
import java.lang.Exception

class AuthRepository {

    suspend fun signUp(
        email: String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatus<User> /*List<Dog>*/ {
        //se usa la clase makeNetworkCall para manejar recursividad en caso de varias
        //peticiones a apis
        return makeNetworkCall {
            val signUpDTO = SignUpDTO(email, password, passwordConfirmation)
            //todo lo siguiente se envia al call para que trate de ejecutar eso
            val signUpResponse = DogsApi.retrofitService.signUp(signUpDTO)

            if (!signUpResponse.isSuccess) {
                throw Exception(signUpResponse.message)
            }

            val userDTO =
                signUpResponse.data.user//se regresa el ultimo valor que se ponga
            val userDTOMapper = UserDTOMapper()
            //dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)//se comenta por que
            //ya no se devuelve una lista de dog sino un status y este success trae la lista
            userDTOMapper.fromUserDTOToUserDomain(userDTO)
        }
    }
}