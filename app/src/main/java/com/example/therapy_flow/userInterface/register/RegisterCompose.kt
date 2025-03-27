package com.example.therapy_flow.userInterface.register

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.therapy_flow.R
import com.example.therapy_flow.design_system.MyTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff

@Composable
fun RegisterScreen(
    navController: NavHostController,
    registerViewModel: RegisterViewModel
) {
    val context = LocalContext.current

    // Collecte des messages toast via le SharedFlow du ViewModel
    LaunchedEffect(Unit) {
        registerViewModel.uiMessageFlow.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Collecte du résultat d'inscription pour la navigation
    LaunchedEffect(Unit) {
        registerViewModel.registerResult.collect { success ->
            if (success) {
                navController.navigate("landing") {
                    popUpTo("register") { inclusive = true }
                    Log.e("Register", "Collecte de l'emit pour naviguer")
                }
            }
        }
    }

    RegisterContent(
        onRegisterPressed = { email, password, confirmPassword ->
            registerViewModel.performRegister(email, password, confirmPassword)
        },
        onNavigateToLogin = {
            navController.popBackStack()
        }
    )
}

@Composable
fun RegisterContent(
    onRegisterPressed: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }


    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()

    val trailingIcon = when {
        emailInput.isEmpty() -> null
        emailRegex.matches(emailInput) -> Icons.Outlined.Check
        else -> Icons.Outlined.Error
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.register),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Register",
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
        }

        MyTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            hint = "Email",
            hintColor = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            isPassword = false,
            leadingIcon = Icons.Outlined.Email,
            trailingIcon = trailingIcon,
            onTrailingClick = { }
        )

        MyTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            hint = "Password",
            hintColor = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            isPassword = !passwordVisible,
            leadingIcon = Icons.Outlined.Lock,
            trailingIcon = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
            onTrailingClick = { passwordVisible = !passwordVisible }
        )

        MyTextField(
            value = confirmPasswordInput,
            onValueChange = { confirmPasswordInput = it },
            hint = "Confirm Password",
            hintColor = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            isPassword = !confirmPasswordVisible,
            leadingIcon = Icons.Outlined.Lock,
            trailingIcon = if (confirmPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
            onTrailingClick = { confirmPasswordVisible = !confirmPasswordVisible }
        )

        Button(
            onClick = { onRegisterPressed(emailInput, passwordInput, confirmPasswordInput) },
            modifier = Modifier.wrapContentWidth()

        ) {
            Text(
                text = "Register",
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Already an account? ",
                fontSize = 16.sp
            )
            Text(
                text = "Login",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }

        Spacer(modifier = Modifier.height(1.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterContent(
        onRegisterPressed = { _, _, _ -> },
        onNavigateToLogin = { }
    )
}
