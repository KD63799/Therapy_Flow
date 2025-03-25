package com.example.therapy_flow.design_system

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SettingsAccessibility
import androidx.compose.material.icons.filled.Task

// Définition des routes pour le Bottom Navigation
sealed class BottomNavScreen(val route: String) {
    data object Schedule : BottomNavScreen("schedule_screen")
    data object Profile : BottomNavScreen("profile_screen")
    data object Patient : BottomNavScreen("patient_screen")
    data object Todo : BottomNavScreen("todo_screen")
}

// Data class pour représenter un item de navigation
data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    // On utilise ici les routes définies dans BottomNavScreen
    val navigationItems = listOf(
        NavigationItem(
            title = "Schedule",
            icon = Icons.Filled.Schedule,
            route = BottomNavScreen.Schedule.route
        ),
        NavigationItem(
            title = "Profile",
            icon = Icons.Filled.SettingsAccessibility,
            route = BottomNavScreen.Profile.route
        ),
        NavigationItem(
            title = "Patient",
            icon = Icons.Filled.PersonSearch,
            route = BottomNavScreen.Patient.route
        ),
        NavigationItem(
            title = "Todo",
            icon = Icons.Filled.Task,
            route = BottomNavScreen.Todo.route
        )
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navigationItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationRoute ?: "") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
