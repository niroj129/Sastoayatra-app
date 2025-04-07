package com.example.sastoyatra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sastoyatra.ui.*
import androidx.navigation.NavType
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.auth // Initialize Firebase Auth
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val firebaseUser = Firebase.auth.currentUser

    // Decide where to start
    val startDestination = when {
        firebaseUser == null -> "signup" // Not logged in yet
        else -> "provider_dashboard"     // Default dashboard for now (can be role-based later)
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("pending_approval") { PendingApprovalScreen(navController) }
        composable("traveler_dashboard") { TravelerDashboardScreen(navController) }
        composable("provider_dashboard") { ProviderDashboardScreen(navController) }
        composable("admin_dashboard") { AdminDashboardScreen(navController) }
        composable("forgot_password") { ForgotPasswordScreen(navController) }
        composable(
            "verify_otp/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            VerifyOTPScreen(navController, backStackEntry.arguments?.getString("email") ?: "")
        }
        composable(
            "reset_password/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            ResetPasswordScreen(navController, backStackEntry.arguments?.getString("email") ?: "")
        }
    }
}
