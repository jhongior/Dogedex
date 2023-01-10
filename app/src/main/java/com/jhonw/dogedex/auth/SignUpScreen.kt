import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.jhonw.dogedex.R
import com.jhonw.dogedex.auth.AuthViewModel
import com.jhonw.dogedex.composables.AuthField
import com.jhonw.dogedex.composables.BackNavigationIcon

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalCoilApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun SignUpScreen(
    onSignUpButtonClick: (email: String, password: String, passwordConfirmation: String) -> Unit,
    onNavigationIconClick: () -> Unit,
    authViewModel: AuthViewModel
) {
    Scaffold(
        topBar = { SignUpScreenToolbar(onNavigationIconClick = onNavigationIconClick) }
    ) {
        Content(
            resetFieldErrors = { authViewModel.resetErrors() },
            onSignUpButtonClick = onSignUpButtonClick,
            authViewModel = authViewModel
        )
    }
}

@Composable
private fun Content(
    resetFieldErrors: () -> Unit,
    onSignUpButtonClick: (email: String, password: String, passwordConfirmation: String) -> Unit,
    authViewModel: AuthViewModel
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
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
            onTextChanged = {
                email.value = it
                resetFieldErrors()
            },
            modifier = Modifier.fillMaxWidth(),
            errorMessageId = authViewModel.emailError.value
        )
        AuthField(
            label = stringResource(id = R.string.password),
            email = password.value,
            onTextChanged = {
                password.value = it
                resetFieldErrors()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            errorMessageId = authViewModel.passwordError.value,
            visualTransformation = PasswordVisualTransformation(),
        )
        AuthField(
            label = stringResource(id = R.string.confirm_password),
            email = confirmPassword.value,
            onTextChanged = {
                confirmPassword.value = it
                resetFieldErrors()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            errorMessageId = authViewModel.confirmPasswordError.value,
            visualTransformation = PasswordVisualTransformation(),
        )

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            onClick = { onSignUpButtonClick(email.value, password.value, confirmPassword.value) }) {
            Text(
                stringResource(id = R.string.sign_up),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SignUpScreenToolbar(
    onNavigationIconClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        backgroundColor = Color.Red,
        contentColor = Color.White,
        elevation = 10.dp,
        navigationIcon = {
            BackNavigationIcon {
                onNavigationIconClick()
            }
        }
    )
}