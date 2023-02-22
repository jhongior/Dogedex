package com.jhonw.dogedex

import android.Manifest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.GrantPermissionRule
import coil.annotation.ExperimentalCoilApi
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.auth.AuthRepository
import com.jhonw.dogedex.auth.AuthTasks
import com.jhonw.dogedex.auth.LoginActivity
import com.jhonw.dogedex.di.AuthTasksModule
import com.jhonw.dogedex.model.User
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

//testing con androidComposeTestRule y se puede combinar con expresso
@ExperimentalCoilApi
@ExperimentalMaterial3Api
@UninstallModules(AuthTasksModule::class)//se usa para que corra solo en test y no en produccion, si son más Modulos se agregan ahí mismo
//se asegura que use el modulo de test
@HiltAndroidTest
class LoginActivityTest {

    @get:Rule(order = 0)//se usa cuando se tienen varios test y se quiere tener un orden
    //siempre debe estar de primero el HiltAnroidRule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val runtimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.CAMERA)

    @ExperimentalFoundationApi
    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    //con androidComposeTestRule solo se puede usar un Fake por test
    class FakeAuthRepository @Inject constructor() : AuthTasks {
        override suspend fun login(email: String, password: String): ApiResponseStatus<User> {
            return ApiResponseStatus.Success(
                User(1L, "jhongioring@gmail.com", "dsafdfdfddfd")
            )
        }

        override suspend fun signUp(
            email: String,
            password: String,
            passwordConfirmation: String
        ): ApiResponseStatus<User> {
            TODO("Not yet implemented")
        }

    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class AuthTasksTestModule {

        @Binds
        abstract fun bindDogTasks(
            fakeAuthRepository: FakeAuthRepository
        ): AuthTasks
    }

    @ExperimentalFoundationApi
    @Test
    fun mainActivityOpenAfterUserLogin() {
        val context = composeTestRule.activity//se puede tomar el contexto de composetestrule

        //gracias al contexto podemos ir directamente componente
        composeTestRule.onNodeWithText(context.getString(R.string.login))
            .assertIsDisplayed()//valida que exista un boton de login

        //inserta el email
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "email-field")
            .performTextInput("jhongioring@gmail.com")

        //inserta el password
        composeTestRule.onNodeWithTag(useUnmergedTree = true, testTag = "password-field")
            .performTextInput("123456")

        //da click en el boton de login
        composeTestRule.onNodeWithText(context.getString(R.string.login))
            .performClick()

        /*
        luego de dar click en login se debe abrir el mainActivity y se valida a traves de expresso que exista por lo menos
        un view de dicha activity como a continuacion
         */
        onView(withId(R.id.take_photo_fab)).check(matches(isDisplayed()))
    }
}