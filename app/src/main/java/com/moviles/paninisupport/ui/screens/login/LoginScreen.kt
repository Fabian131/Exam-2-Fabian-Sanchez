package com.moviles.paninisupport.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Support
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moviles.paninisupport.data.AppContainer
import com.moviles.paninisupport.ui.components.AppButton
import com.moviles.paninisupport.ui.components.AppTextField
import com.moviles.paninisupport.ui.theme.AppPrimary
import com.moviles.paninisupport.ui.theme.AppSecondaryText
import com.moviles.paninisupport.ui.theme.AppSurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(AppContainer.authRepository)
    ),
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message)
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.user) {
        if (uiState.user != null) {
            onLoginSuccess()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = AppSurfaceVariant,
                            shape = RoundedCornerShape(22.dp)
                        )
                        .padding(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Support,
                        contentDescription = null,
                        tint = AppPrimary,
                        modifier = Modifier.height(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Panini Support",
                    style = MaterialTheme.typography.headlineMedium,
                    color = AppPrimary
                )
                Text(
                    text = "FIFA World Cup 2026",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppSecondaryText
                )

                Spacer(modifier = Modifier.height(40.dp))

                AppTextField(
                    value = email,
                    label = "Email",
                    placeholder = "admin@panini.com",
                    onValueChange = { email = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    value = password,
                    label = "Password",
                    placeholder = "\u2022\u2022\u2022\u2022\u2022\u2022\u2022",
                    onValueChange = { password = it },
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppButton(
                    text = if (uiState.isLoading) "Loading..." else "Login",
                    onClick = { viewModel.login(email = email, password = password) },
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Use admin@gmail.com / Admin1234#",
                    color = AppSecondaryText,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
