package com.jhonw.dogedex.api.responses

import com.squareup.moshi.Json

class DogListApiResponse(
    val message: String,
    @field:Json(name = "is_success") val isSuccess: Boolean,//@Json es por que se llama así en el Json
    val data: DogListResponse
)