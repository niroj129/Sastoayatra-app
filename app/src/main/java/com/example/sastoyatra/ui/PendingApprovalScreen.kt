package com.example.sastoyatra.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PendingApprovalScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Account Pending Approval",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Your account is under review by the admin. You will be notified once approved.",
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = { navController.navigate("login") {
                    popUpTo("pending_approval") { inclusive = true }
                } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Login")
            }
        }
    }
}