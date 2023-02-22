package com.jhonw.dogedex.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import coil.annotation.ExperimentalCoilApi
import com.jhonw.dogedex.R
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalCoilApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun LoginScreen(
    onLoginButtonClick: (String, String) -> Unit,
    onRegisterButtonClick: () -> Unit,
    authViewModel: AuthViewModel
) {
    Scaffold(
        topBar = { LoginScreenToolbar() }
    ) {
        Content(
            onLoginButtonClick = onLoginButtonClick,
            onRegisterButtonClick = onRegisterButtonClick
        )
    }
}

@Composable
private fun Content(
    onLoginButtonClick: (String, String) -> Unit,
    onRegisterButtonClick: () -> Unit,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 65.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthField(
            label = stringResource(id = R.string.email),
            email = email.value,
            onTextChanged = { email.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { testTag = "email-field" }
        )
        AuthField(
            label = stringResource(id = R.string.password),
            email = password.value,
            onTextChanged = { password.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .semantics { testTag = "password-field" },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .semantics { testTag = "login-button" },
            onClick = { onLoginButtonClick(email.value, password.value) }) {
            Text(
                stringResource(id = R.string.login),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.do_not_have_an_account)
        )

        Text(
            modifier = Modifier
                .clickable(enabled = true, onClick = { onRegisterButtonClick() })
                .fillMaxWidth()
                .semantics { testTag = "login-screen-register-button" }
                .padding(16.dp),
            text = stringResource(R.string.register),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun AuthField(
    label: String,
    email: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        label = { Text(label) },
        modifier = modifier,
        value = email,
        onValueChange = { onTextChanged(it) },
        visualTransformation = visualTransformation
    )
}

@Composable
fun LoginScreenToolbar(
) {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        backgroundColor = Color.Red,
        contentColor = White
    )
}