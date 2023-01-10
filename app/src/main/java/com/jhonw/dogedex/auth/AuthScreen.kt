package com.jhonw.dogedex.auth

import SignUpScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
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
    status: ApiResponseStatus<User>?,
    onLoginButtonClick: (String, String) -> Unit,
    onSignUpButtonClick: (email: String, password: String, passwordConfirmation: String) -> Unit,
    onErrorDialogDismiss: () -> Unit,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    AuthNavHost(
        navController = navController,
        onLoginButtonClick = onLoginButtonClick,
        onSignUpButtonClick = onSignUpButtonClick,
        authViewModel = authViewModel
    )

    if (status is ApiResponseStatus.Loading)
        LoadingWheel()
    else if (status is ApiResponseStatus.Error)
        ErrorDialog(status.message, onErrorDialogDismiss)
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
