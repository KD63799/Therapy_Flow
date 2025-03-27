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
import com.example.therapy_flow.userInterface.schedule.ScheduleViewModel
import com.example.therapy_flow.userInterface.todo.TodoScreen
import com.example.therapy_flow.userInterface.todo.TodoViewModel

@Composable
fun LandingScreen(
    navController: NavHostController,
    viewModel: LandingViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.navigateToLoginFlow.collect { navigate ->
            if (navigate) {
                navController.navigate("login") {
                    popUpTo("landing") { inclusive = true }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiMessageFlow.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LandingContent(viewModel = viewModel)
}

@Composable
fun LandingContent(viewModel: LandingViewModel) {
    val bottomNavController = rememberNavController()
    Scaffold(
        topBar = {
            TopBar(
                title = "Therapy Flow",
                onLogoutClick = { viewModel.disconnectUser() }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = bottomNavController,
                startDestination = BottomNavScreen.Schedule.route
            ) {
                composable(BottomNavScreen.Schedule.route) {
                    val scheduleViewModel: ScheduleViewModel = hiltViewModel()
                    ScheduleScreen(navController = bottomNavController, scheduleViewModel)
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
