package com.jhonw.dogedex

import android.support.test.InstrumentationRegistry
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.di.ClassifierModule
import com.jhonw.dogedex.di.DogTasksModule
import com.jhonw.dogedex.doglist.DogRepository
import com.jhonw.dogedex.doglist.DogTasks
import com.jhonw.dogedex.machinelearning.ClassifierRepository
import com.jhonw.dogedex.machinelearning.ClassifierTasks
import com.jhonw.dogedex.machinelearning.DogRecognition
import com.jhonw.dogedex.main.MainActivity
import com.jhonw.dogedex.model.Dog
import com.jhonw.dogedex.testutils.EspressoIdlingResource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalFoundationApi
@UninstallModules(DogTasksModule::class, ClassifierModule::class)
@HiltAndroidTest
//se hace testing con expresso
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @ExperimentalMaterial3Api
    @get:Rule(order = 2)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    class FakeDogRepository @Inject constructor() : DogTasks {
        override suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
            return ApiResponseStatus.Success(
                listOf(
                    Dog(
                        1, 1, "", "", "", "", "", "",
                        "", "", "", inCollection = true
                    ),
                    Dog(
                        19, 2, "", "", "", "", "", "",
                        "", "", "", inCollection = true
                    )
                )
            )
        }

        override suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> {
            TODO("Not yet implemented")
        }

        override suspend fun getDogByMLId(mlDogId: String): ApiResponseStatus<Dog> {
            return ApiResponseStatus.Success(
                Dog(
                    89, 70, "Chow chow", "", "", "", "", "",
                    "", "", "", inCollection = true
                )
            )
        }

    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class DogTasksTestModule {
        @Binds
        abstract fun bindDogTasks(
            fakeDogRepository: FakeDogRepository
        ): DogTasks
    }

    class FakeClassifierRepository @Inject constructor() : ClassifierTasks {
        override suspend fun recognizeImage(imageProxy: ImageProxy): List<DogRecognition> {
            return listOf(DogRecognition("fdfdfd", 80.0f))
        }

    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class ClassifierTestModule {
        @Binds
        abstract fun bindClassifierTasks(
            fakeClassifierRepository: FakeClassifierRepository
        ): ClassifierTasks
    }

    //lo antetrior es para preparar el ambiente de testing

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun showAllFab() {
        onView(withId(R.id.take_photo_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.dog_list_fab)).check(matches(isDisplayed()))
        onView(withId(R.id.settings_fab)).check(matches(isDisplayed()))
    }

    @Test
    fun dogListOpensWhenClickingButton() {
        onView(withId(R.id.dog_list_fab)).perform(click())

        //esta es otra manera de tomar el contexto de una activity con expresso
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val string = context.getString(R.string.my_dog_collection)
        //onView(withText(string)).check(matches(isDisplayed()))
        composeTestRule.onNodeWithText(string).assertIsDisplayed()
    }

    @Test
    fun whenRecognizingDogDetailScreenOpens() {
        onView(withId(R.id.take_photo_fab)).perform(click())
        composeTestRule.onNodeWithTag(testTag = "close-details-screen-fab").assertIsDisplayed()
    }
}