package com.jhonw.dogedex

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.doglist.DogListScreen
import com.jhonw.dogedex.doglist.DogListViewModel
import com.jhonw.dogedex.doglist.DogTasks
import com.jhonw.dogedex.model.Dog
import org.junit.Rule
import org.junit.Test

@ExperimentalFoundationApi
class DogListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalMaterial3Api
    @Test
    fun progressBarShowsWhenLoadingState() {

        class FakeDogRepository : DogTasks {
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Loading()
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMLId(mlDogId: String): ApiResponseStatus<Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        composeTestRule.setContent {
            DogListScreen(
                onDogClicked = {},
                onNavigationIconClick = { /*TODO*/ },
                viewModel = viewModel
            )
        }

        //en este caso un Node es un view
        composeTestRule.onNodeWithTag(testTag = "loading-wheel").assertIsDisplayed()
    }

    @ExperimentalMaterial3Api
    @Test
    fun errorDialogShowsIfErrorGettingDogs() {

        class FakeDogRepository : DogTasks {
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Error(message = "error-dialog")
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMLId(mlDogId: String): ApiResponseStatus<Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        composeTestRule.setContent {
            DogListScreen(
                onDogClicked = {},
                onNavigationIconClick = { /*TODO*/ },
                viewModel = viewModel
            )
        }

        //en este caso un Node es un view
        //composeTestRule.onNodeWithText(text = "there was an error").assertIsDisplayed()//se trabajo con este para poner como ejemplo que de esta
        //manera no se puede usar los getstring y por eso composeTestRule tiene una desventaja frente a espresso
        composeTestRule.onNodeWithTag(testTag = "error-dialog")
            .assertIsDisplayed()//de esta manera no dependemos del idioma
    }

    @ExperimentalMaterial3Api
    @Test
    fun dogListShowsIfSuccesssGettingDogs() {

        class FakeDogRepository : DogTasks {
            override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
                return ApiResponseStatus.Success(
                    listOf(
                        Dog(
                            1, 1, "Chihuahua", "", "", "", "", "",
                            "", "", "", inCollection = true
                        ),
                        Dog(
                            19, 2, "Guillermo", "", "", "", "", "",
                            "", "", "", inCollection = true
                        )
                    )
                )
            }

            override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
                TODO("Not yet implemented")
            }

            override suspend fun getDogByMLId(mlDogId: String): ApiResponseStatus<Dog> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = DogListViewModel(
            dogRepository = FakeDogRepository()
        )

        composeTestRule.setContent {
            DogListScreen(
                onDogClicked = {},
                onNavigationIconClick = { /*TODO*/ },
                viewModel = viewModel
            )
        }

        //para separar los nodos padres de los hijos se usa useUnmergedTree en la siguiente linea, de lo contrario
        //el test no va ser exitoso, si no android coge el primer nodo o view que encuentra y cree que solo existe ese, esto es solo con tag por que con texto no es necesario
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("ALGO")

        //en este caso un Node es un view
        //composeTestRule.onNodeWithText(text = "there was an error").assertIsDisplayed()//se trabajo con este para poner como ejemplo que de esta
        //manera no se puede usar los getstring y por eso composeTestRule tiene una desventaja frente a espresso
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "dog-Chihuahua")
            .assertIsDisplayed()//de esta manera no dependemos del idioma
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "dog-Guillermo").assertIsDisplayed()
    }
}