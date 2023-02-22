package com.jhonw.dogedex.viewmodel

import com.jhonw.dogedex.R
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.auth.AuthTasks
import com.jhonw.dogedex.auth.AuthViewModel
import com.jhonw.dogedex.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {
    // WITH
    @ExperimentalCoroutinesApi
    @get:Rule
    var dogedexCoroutineRule = DogedexCoroutineRule()

    @Test
    fun testLoginValidationsCorrect() {

        class FakeAuthRepository : AuthTasks {
            override suspend fun login(email: String, password: String): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(User(1, "jhongioring@gmail.com", ""))
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(User(1, "", ""))
            }

        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        viewModel.login("", "123456")

        assertEquals(R.string.email_no_valido, viewModel.emailError.value)

        viewModel.login("jhongioring@gmail.com", "")

        assertEquals(R.string.password_no_valido, viewModel.passwordError.value)
    }

    @Test
    fun testLoginStatesCorrect() {

        val fakeUser = User(1, "jhongioring@gmail.com", "")

        class FakeAuthRepository : AuthTasks {
            override suspend fun login(email: String, password: String): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(fakeUser)
            }

            override suspend fun signUp(
                email: String,
                password: String,
                passwordConfirmation: String
            ): ApiResponseStatus<User> {
                return ApiResponseStatus.Success(User(1, "", ""))
            }

        }

        val viewModel = AuthViewModel(
            authRepository = FakeAuthRepository()
        )

        viewModel.login("jhongioring@gmail.com", "123456")

        assertEquals(fakeUser.email, viewModel.user.value?.email)
    }
}