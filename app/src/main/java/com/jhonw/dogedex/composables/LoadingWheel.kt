package com.jhonw.dogedex.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag

@Composable
fun LoadingWheel() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { testTag = "loading-wheel" },//semantics se usa para testing con composeRule, se ejecuta solo en modo test
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.Red
        )
    }
}
