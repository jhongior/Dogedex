package com.jhonw.dogedex.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.jhonw.dogedex.R
import com.jhonw.dogedex.api.ApiResponseStatus

@Composable
fun ErrorDialog(
    message: String,
    onDialogDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.semantics { testTag = "error-dialog" },
        onDismissRequest = { /*TODO*/ },
        title = {
            Text(stringResource(R.string.dialog_error_title))
        },
        text = {
            Text(stringResource(id = message.toString().toInt()))
        },
        confirmButton = {
            Button(onClick = { onDialogDismiss() }) {
                Text(stringResource(R.string.try_again))
            }
        }
    )
}
