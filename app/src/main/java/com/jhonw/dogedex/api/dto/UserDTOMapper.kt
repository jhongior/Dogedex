package com.jhonw.dogedex.api.dto

import com.jhonw.dogedex.model.User

class UserDTOMapper {
    fun fromUserDTOToUserDomain(userDTO: UserDTO): User {
        return User(userDTO.id, userDTO.email, userDTO.authenticationToken)
    }
}