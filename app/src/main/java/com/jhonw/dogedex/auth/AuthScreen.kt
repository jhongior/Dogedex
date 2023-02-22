package com.jhonw.dogedex.auth

import SignUpScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.auth.AuthNavDestinations.LoginScreenDestination
import com.jhonw.dogedex.auth.AuthNavDestinations.SignUpScreenDestination
import com.jhonw.dogedex.composables.ErrorDialog
import com.jhonw.dogedex.composables.LoadingWheel
import com.jhonw.dogedex.model.User

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalCoilApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun AuthScreen(
    onUserLoggedIn: (User) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()//se inject el viewmodel desde aqui para facilitar el testing
) {

    val user = authViewModel.user

    val userValue = user.value
    if (userValue != null) {
        onUserLoggedIn(userValue)
    }

    val navController = rememberNavController()
    val status = authViewModel.status.value

    AuthNavHost(
        navController = navController,
        onLoginButtonClick = { email, password -> authViewModel.login(email, password) },
        onSignUpButtonClick = { email, password, confirmPassword ->
            authViewModel.signUp(
                email,
                password,
                confirmPassword
            )
        },
        authViewModel = authViewModel
    )

    if (status is ApiResponseStatus.Loading)
        LoadingWheel()
    else if (status is ApiResponseStatus.Error)
        ErrorDialog(status.message) { authViewModel.resetApiResponseStatus() }
}

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun AuthNavHost(
    navController: NavHostController,
    onLoginButtonClick: (String, String) -> Unit,
    onSignUpButtonClick: (email: String, password: String, passwordConfirmation: String) -> Unit,
    authViewModel: AuthViewModel
) {
    NavHost(navController = navController, startDestination = LoginScreenDestination) {
        composable(route = LoginScreenDestination) {
            LoginScreen(
                onLoginButtonClick = onLoginButtonClick,
                onRegisterButtonClick = {
                    navController.navigate(route = SignUpScreenDestination)
                },
                authViewModel = authViewModel
            )
        }

        composable(route = SignUpScreenDestination) {
            SignUpScreen(
                onSignUpButtonClick = onSignUpButtonClick,
                onNavigationIconClick = { navController.navigateUp() },
                authViewModel = authViewModel
            )
        }
    }
}
