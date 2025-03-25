package com.example.therapy_flow

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.therapy_flow.userInterface.landing.LandingScreen
import com.example.therapy_flow.userInterface.landing.LandingViewModel
import com.example.therapy_flow.userInterface.login.LoginScreen
import com.example.therapy_flow.userInterface.login.LoginViewModel
import com.example.therapy_flow.userInterface.register.RegisterScreen
import com.example.therapy_flow.userInterface.register.RegisterViewModel
import com.example.therapy_flow.userInterface.splash.SplashScreen
import com.example.therapy_flow.userInterface.splash.SplashViewModel

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Landing : Screen("landing")
    data object Create : Screen("create")
    data object Edit : Screen("edit")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController = navController, viewModel = splashViewModel)
        }
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController = navController, loginViewModel)
        }
        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(navController = navController, registerViewModel)
        }
        composable(Screen.Landing.route) {
            val landingViewModel: LandingViewModel = hiltViewModel()
            LandingScreen(navController = navController, landingViewModel)
        }
    }
}
