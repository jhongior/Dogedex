package com.jhonw.dogedex.viewmodel

import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.doglist.DogListViewModel
import com.jhonw.dogedex.doglist.DogTasks
import com.jhonw.dogedex.model.Dog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DogListViewModelTest {

    // WITH
    @ExperimentalCoroutinesApi
    @get:Rule
    var dogedexCoroutineRule = DogedexCoroutineRule()

    @Test
    fun downloadDogListStatusesCorrect() {

        class FakeDogRepository : DogTasks {
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Success(
                    listOf(
                        Dog(
                            1, 1, "", "", "", "", "", "",
                            "", "", "", inCollection = false
                        ),
                        Dog(
                            2, 2, "", "", "", "", "", "",
                            "", "", "", inCollection = false
                        )
                    )
                )
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                return ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMLId(mlDogId: String): ApiResponseStatus<Dog> {
                return ApiResponseStatus.Success(
                    Dog(
                        1, 1, "", "", "", "", "", "",
                        "", "", "", inCollection = false
                    )
                )

            }
        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        assertEquals(1, viewModel.dogList.value.size)
        assert(viewModel.status.value is ApiResponseStatus.Success)
    }


    @Test
    fun downloadDogListErrorStatusesCorrect() {

        class FakeDogRepository : DogTasks {
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Error(message = "12")
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                return ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMLId(mlDogId: String): ApiResponseStatus<Dog> {
                return ApiResponseStatus.Success(
                    Dog(
                        1, 1, "", "", "", "", "", "",
                        "", "", "", inCollection = false
                    )
                )

            }
        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        assertEquals(0, viewModel.dogList.value.size)
        assert(viewModel.status.value is ApiResponseStatus.Error)
    }

    @Test
    fun resetStatusesCorrect() {

        class FakeDogRepository : DogTasks {
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Error(message = "12")
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                return ApiResponseStatus.Success(Unit)
            }

            override suspend fun getDogByMLId(mlDogId: String): ApiResponseStatus<Dog> {
                return ApiResponseStatus.Success(
                    Dog(
                        1, 1, "", "", "", "", "", "",
                        "", "", "", inCollection = false
                    )
                )

            }
        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        assert(viewModel.status.value is ApiResponseStatus.Error)
        viewModel.resetApiResponseStatus()
        assert(viewModel.status.value == null)
    }
}