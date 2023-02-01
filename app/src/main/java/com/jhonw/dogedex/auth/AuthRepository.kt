package com.jhonw.dogedex.auth

import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.api.ApiService
import com.jhonw.dogedex.api.dto.LoginDTO
import com.jhonw.dogedex.api.dto.SignUpDTO
import com.jhonw.dogedex.api.dto.UserDTOMapper
import com.jhonw.dogedex.api.makeNetworkCall
import com.jhonw.dogedex.model.User
import java.lang.Exception
import javax.inject.Inject

interface AuthTasks {
    suspend fun login(
        email: String,
        password: String
    ): ApiResponseStatus<User>

    suspend fun signUp(
        email: String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatus<User>
}

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) : AuthTasks {

    override suspend fun login(
        email: String,
        password: String
    ): ApiResponseStatus<User> /*List<Dog>*/ {
        //se usa la clase makeNetworkCall para manejar recursividad en caso de varias
        //peticiones a apis
        return makeNetworkCall {
            val loginDTO = LoginDTO(email, password)
            //todo lo siguiente se envia al call para que trate de ejecutar eso
            val loginResponse = apiService.login(loginDTO)

            if (!loginResponse.isSuccess) {
                throw Exception(loginResponse.message)
            }

            val userDTO =
                loginResponse.data.user//se regresa el ultimo valor que se ponga
            val userDTOMapper = UserDTOMapper()
            //dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)//se comenta por que
            //ya no se devuelve una lista de dog sino un status y este success trae la lista
            userDTOMapper.fromUserDTOToUserDomain(userDTO)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatus<User> /*List<Dog>*/ {
        //se usa la clase makeNetworkCall para manejar recursividad en caso de varias
        //peticiones a apis
        return makeNetworkCall {
            val signUpDTO = SignUpDTO(email, password, passwordConfirmation)
            //todo lo siguiente se envia al call para que trate de ejecutar eso
            val signUpResponse = apiService.signUp(signUpDTO)

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