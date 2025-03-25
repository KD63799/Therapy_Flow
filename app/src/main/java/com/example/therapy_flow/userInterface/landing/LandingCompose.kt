package com.example.therapy_flow.userInterface.landing

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.therapy_flow.design_system.BottomNavigationBar
import com.example.therapy_flow.design_system.BottomNavScreen
import com.example.therapy_flow.design_system.TopBar
import com.example.therapy_flow.userInterface.schedule.ScheduleScreen
import com.example.therapy_flow.userInterface.patient.PatientScreen
import com.example.therapy_flow.userInterface.patient.PatientViewModel
import com.example.therapy_flow.userInterface.profile.ProfileScreen
import com.example.therapy_flow.userInterface.profile.ProfileViewModel
import com.example.therapy_flow.userInterface.todo.TodoScreen
import com.example.therapy_flow.userInterface.todo.TodoViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun LandingScreen(
    navController: NavHostController,
    viewModel: LandingViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Collecte des événements de navigation (déconnexion)
    LaunchedEffect(Unit) {
        viewModel.navigateToLoginFlow.collect { navigate ->
            if (navigate) {
                navController.navigate("login") {
                    popUpTo("landing") { inclusive = true }
                }
            }
        }
    }

    // Collecte et affichage du toast
    LaunchedEffect(Unit) {
        viewModel.toastMessageFlow.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LandingContent(viewModel = viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingContent(viewModel: LandingViewModel) {
    // Créer un NavController dédié pour la navigation du Bottom Navigation
    val bottomNavController = rememberNavController()
    Scaffold(
        topBar = {
            TopBar(
                title = "Therapy Flow",
                onLogoutClick = { viewModel.disconnectUser() }
            )
        },
        bottomBar = {
            // Réutilisation du BottomNavigationBar défini dans votre design system
            BottomNavigationBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // Utiliser un NavHost interne avec les routes de BottomNavScreen
            NavHost(
                navController = bottomNavController,
                startDestination = BottomNavScreen.Schedule.route
            ) {
                composable(BottomNavScreen.Schedule.route) {
                    // Appeler le contenu de l'accueil, par exemple ScheduleScreen
                    ScheduleScreen(navController = bottomNavController)
                }
                composable(BottomNavScreen.Profile.route) {
                    val profileViewModel: ProfileViewModel = hiltViewModel()
                    ProfileScreen(navController = bottomNavController, profileViewModel)
                }
                composable(BottomNavScreen.Patient.route) {
                    val patientViewModel: PatientViewModel = hiltViewModel()
                    PatientScreen(navController = bottomNavController, patientViewModel)
                }
                composable(BottomNavScreen.Todo.route) {
                    val todoViewModel: TodoViewModel = hiltViewModel()
                    TodoScreen(navController = bottomNavController, todoViewModel)
                }
            }
        }
    }
}
