package com.jhonw.dogedex.doglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhonw.dogedex.model.Dog
import com.jhonw.dogedex.api.ApiResponseStatus
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {

    private val _dogList = MutableLiveData<List<Dog>>()
    val dogList: LiveData<List<Dog>>
        get() = _dogList

    private val _status = MutableLiveData<ApiResponseStatus<List<Dog>>>()
    val status: LiveData<ApiResponseStatus<List<Dog>>>
        get() = _status

    private val dogRepository = DogRepository()

    init {
        downLoadDogs()
    }

    private fun downLoadDogs() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(dogRepository.downLoadDogs())

            /*
            se comenta lo siguiente para dejar de usar los enum y usar los sealed
             */
            /*_status.value = ApiResponseStatus.LOADING
            try {
                _dogList.value = dogRepository.downLoadDogs()
                _status.value = ApiResponseStatus.SUCCESS
            } catch (e: Exception) {
                _status.value = ApiResponseStatus.ERROR
            }*/
        }
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dogList.value = apiResponseStatus.data
        }
        _status.value = apiResponseStatus
    }
}