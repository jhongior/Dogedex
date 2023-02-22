package com.jhonw.dogedex

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.auth.AuthScreen
import com.jhonw.dogedex.auth.AuthTasks
import com.jhonw.dogedex.auth.AuthViewModel
import com.jhonw.dogedex.model.User
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterial3Api
class AuthScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalFoundationApi
    @Test
    fun testTappingRegisterButtonOpenSignUpScreen() {

        class FakeAuthRepository : AuthTasks {
            override suspend fun login(email: String, password: String): ApiResponseStatus<User> {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): ApiResponseStatus<User> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        composeTestRule.setContent {
            AuthScreen(
                onUserLoggedIn = {},
                authViewModel = viewModel
            )
        }
        //estos testTag deben ser declarados en los semantics de los screen

        composeTestRule.onNodeWithTag(testTag = "login-button")
            .assertIsDisplayed()//comprobamos que el botón exista en la pantalla para comprobar que estamos en el login
        composeTestRule.onNodeWithTag(testTag = "login-screen-register-button")
            .performClick()//para dar click al boton
        composeTestRule.onNodeWithTag(testTag = "sign-up-button")
            .assertIsDisplayed()//comprobamos que nos dirigimos al modulo de registro y existe el boton
    }

    @ExperimentalFoundationApi
    @Test
    fun testEmailErrorShowsIfTappingLoginButtonAndNotEmail() {

        class FakeAuthRepository : AuthTasks {
            override suspend fun login(email: String, password: String): ApiResponseStatus<User> {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): ApiResponseStatus<User> {
                TODO("Not yet implemented")
            }

        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        composeTestRule.setContent {
            AuthScreen(
                onUserLoggedIn = {},
                authViewModel = viewModel
            )
        }
        //estos testTag deben ser declarados en los semantics de los screen

        composeTestRule.onNodeWithTag(testTag = "login-button")
            .performClick()//comprobamos que el botón exista en la pantalla para comprobar que estamos en el login
        composeTestRule.onNodeWithTag(testTag = "login-screen-register-button")
            .performClick()//para dar click al boton
        composeTestRule.onNodeWithTag(testTag = "sign-up-button")
            .assertIsDisplayed()//comprobamos que nos dirigimos al modulo de registro y existe el boton
    }
}