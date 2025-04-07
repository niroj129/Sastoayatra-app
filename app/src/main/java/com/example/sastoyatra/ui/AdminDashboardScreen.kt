package com.example.sastoyatra.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController) {
    val primaryColor = Color(0xFF2E7D32)
    val secondaryColor = Color(0xFFFF8F00)
    val backgroundColor = Color(0xFFF5F5F5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.AdminPanelSettings,
                            contentDescription = "Admin Icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Admin Dashboard", color = Color.White, fontSize = 20.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = primaryColor) {
                val items = listOf(
                    "Home" to Icons.Default.Home,
                    "Users" to Icons.Default.People,
                    "Reports" to Icons.Default.Assessment
                )
                items.forEachIndexed { index, (title, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = title, tint = Color.White) },
                        label = { Text(title, color = Color.White) },
                        selected = index == 0,
                        onClick = {
                            when (title) {
                                "Users" -> navController.navigate("manage_users")
                                "Reports" -> navController.navigate("reports")
                                "Home" -> {}
                            }
                        }
                    )
                }
            }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Text(
                    text = "Pending Service Provider Approvals",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(listOf("Provider 1", "Provider 2", "Provider 3")) { provider ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(provider, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = "Submitted: 2025-04-01",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Row {
                            Button(
                                onClick = { /* Approve provider */ },
                                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                            ) {
                                Text("Approve", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { /* Reject provider */ },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Reject", color = Color.White)
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    text = "System Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total Users: 150", style = MaterialTheme.typography.bodyLarge)
                        Text("Active Providers: 50", style = MaterialTheme.typography.bodyLarge)
                        Text("Pending Approvals: 3", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("login") { popUpTo("admin_dashboard") { inclusive = true } } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Logout", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}