package com.example.therapy_flow.userInterface.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import com.example.therapy_flow.R
import com.example.therapy_flow.Screen
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel
) {
    LaunchedEffect(key1 = true) {
        viewModel.goToDestination.collect { hasToken ->
            if (hasToken) {
                navController.navigate(Screen.Landing.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            } else {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }
    }
    SplashContent()
}

@Composable
fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.DarkGray else Color.White),
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            imageModel = { R.drawable.splash_logo },
            modifier = Modifier.fillMaxSize(),
            imageOptions = ImageOptions(
                contentScale = ContentScale.Fit,
                contentDescription = "Splash Screen GIF"
            )
        )
    }
}
