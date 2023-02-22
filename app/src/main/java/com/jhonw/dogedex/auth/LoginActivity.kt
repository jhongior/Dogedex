package com.jhonw.dogedex.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.findNavController
import coil.annotation.ExperimentalCoilApi
import com.jhonw.dogedex.main.MainActivity
import com.jhonw.dogedex.R
import com.jhonw.dogedex.api.ApiResponseStatus
import com.jhonw.dogedex.databinding.ActivityLoginBinding
import com.jhonw.dogedex.dogdetail.ui.theme.DogedexTheme
import com.jhonw.dogedex.model.User
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class LoginActivity : ComponentActivity(), LoginFragment.LoginFragmentActions {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /*val user = viewModel.user

            val userValue = user.value
            if (userValue != null) {
                User.setLoggedInUser(this, userValue)
                startMainActivity()
            }*/

            DogedexTheme {
                AuthScreen(
                    onUserLoggedIn = ::startMainActivity
                )
                //SignUpScreen()
            }
        }


        /*val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.status.observe(this) { status ->

            when (status) {
                is ApiResponseStatus.Error -> {
                    //ocultar el progress bar
                    binding.loadingWheel.visibility = View.GONE
                    showErrorDialog(status.message)
                    //Toast.makeText(this, status.message, Toast.LENGTH_LONG)
                    //  .show()
                }
                is ApiResponseStatus.Loading -> binding.loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> binding.loadingWheel.visibility = View.GONE
            }
        }

        viewModel.user.observe(this) { user ->
            if (user != null) {
                User.setLoggedInUser(this, user)
                startMainActivity()
            }
        }*/
    }

    private fun startMainActivity(userValue: User) {
        User.setLoggedInUser(this, userValue)
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showErrorDialog(messageId: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(messageId)
            .setPositiveButton("ok") { _,
                                       _ ->
            }
            .create()
            .show()
    }

    //para usar esta navegación se deben agregar plugin en ambos gradle
    override fun onRegisterButtonClick() {
        findNavController(R.id.nav_host_fragment)
            .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    override fun onLoginFieldsValidated(email: String, password: String) {
        //viewModel.login(email, password)
    }

    /*override fun onSignUpFieldsValidated(
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
        viewModel.signUp(email, password, passwordConfirmation)
    }*/
}