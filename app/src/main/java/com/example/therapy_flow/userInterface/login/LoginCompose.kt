package com.example.therapy_flow.userInterface.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.therapy_flow.R
import com.example.therapy_flow.Screen
import com.example.therapy_flow.design_system.MyTextField
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val localContext = LocalContext.current

    LaunchedEffect(Unit) {
        loginViewModel.run {
            launch {
                messageFlow.collect { message ->
                    Toast.makeText(localContext, message, Toast.LENGTH_SHORT).show()
                }
            }
            launch {
                navigateToMain.collect { shouldNavigate ->
                    if (shouldNavigate) {
                        navController.navigate(Screen.Landing.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }

    LoginContent(
        onLoginPressed = { username, password ->
            loginViewModel.performLogin(username, password)
        },
        onNavigateToRegister = {
            navController.navigate(Screen.Register.route)
        }
    )
}

@Composable
fun LoginContent(
    onLoginPressed: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
    val isEmailValid = emailRegex.matches(usernameInput)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.login),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.login),
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
        }

        // Champ Email
        MyTextField(
            value = usernameInput,
            onValueChange = { usernameInput = it },
            hint = stringResource(id = R.string.email),
            hintColor = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            isPassword = false,
            leadingIcon = Icons.Outlined.Email,
            trailingIcon = if (isEmailValid) Icons.Outlined.Check else null,
            onTrailingClick = { /* Action éventuelle */ }
        )

        // Champ Password avec icône de visibilité
        MyTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            hint = stringResource(id = R.string.password),
            hintColor = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            isPassword = !passwordVisible,
            leadingIcon = Icons.Outlined.Lock,
            trailingIcon = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
            onTrailingClick = { passwordVisible = !passwordVisible }
        )

        // Bouton de connexion
        Button(
            onClick = { onLoginPressed(usernameInput, passwordInput) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.login),
                fontSize = 17.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Lien vers l'inscription
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = stringResource(id = R.string.no_account_prompt),
                fontSize = 16.sp
            )
            Text(
                text = stringResource(id = R.string.register),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
        Spacer(modifier = Modifier.height(1.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginContent(
        onLoginPressed = { _, _ -> },
        onNavigateToRegister = { }
    )
}
