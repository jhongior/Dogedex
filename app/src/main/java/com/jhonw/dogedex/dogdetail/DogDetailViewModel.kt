package com.jhonw.dogedex.dogdetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import coil.annotation.ExperimentalCoilApi
import com.jhonw.dogedex.R
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.di.DogTasksModule
import com.jhonw.dogedex.doglist.DogRepository
import com.jhonw.dogedex.doglist.DogTasks
import com.jhonw.dogedex.model.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@ExperimentalCoilApi
@HiltViewModel
class DogDetailViewModel @Inject constructor(
    private val dogRepository: DogTasks,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    @ExperimentalCoilApi
    var dog: MutableState<Dog?> = mutableStateOf(
        savedStateHandle.get<Dog>(DogDetailComposeActivity.DOG_KEY)
    )
        private set

    private var probableDogsIds = mutableStateOf(
        savedStateHandle.get<ArrayList<String>>(DogDetailComposeActivity.MOST_PROBABLE_DOGS_IDS)
            ?: arrayListOf()
    )

    var isRecognition = mutableStateOf(
        savedStateHandle.get<Boolean>(DogDetailComposeActivity.IS_RECOGNITION_KEY)
            ?: false//indica que si es null entonces ponga false
    )
        private set

    var status = mutableStateOf<ApiResponseStatus<Any>?>(null)
        private set

    //pasa los flow a status
    private var _probableDogList = MutableStateFlow<MutableList<Dog>>(mutableListOf())
    val probableDogList: StateFlow<MutableList<Dog>>
        get() = _probableDogList

    fun getProbableDogs() {
        _probableDogList.value.clear()
        viewModelScope.launch {
            dogRepository.getProbableDogs(probableDogsIds.value)//de la siguiente manera se manejan excepciones con flow
                /*.catch { exception ->
                    if (exception is UnknownHostException) {
                        status.value =
                            ApiResponseStatus.Error(R.string.unknown_host_exception_error.toString())
                    }
                }*/
                .collect { apiResponseStatus ->
                    if (apiResponseStatus is ApiResponseStatus.Success) {
                        val probableDogMutableList = _probableDogList.value.toMutableList()
                        probableDogMutableList.add(apiResponseStatus.data)
                        _probableDogList.value = probableDogMutableList
                    }
                }
        }
    }

    fun updateDog(newDog: Dog) {
        dog.value = newDog
    }

    fun addDogToUser() {
        //status.value = ApiResponseStatus.Error(message = R.string.error.toString())
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dog.value!!.id))//!!quiere decir que no va ser null
        }
    }

    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {

        }
        status.value = apiResponseStatus
    }

    fun resetApiResponseStatus() {
        status.value = null
    }

    /*
    de esta manera se usa cuando se emplea databinding, como se empezo a utilizar
    compose entonces se cambio a como aparece arriba

    private val _status = MutableLiveData<ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>>
        get() = _status

    private val dogRepository = DogRepository()

    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))
        }
    }

    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {

        }
        _status.value = apiResponseStatus
    }*/
}