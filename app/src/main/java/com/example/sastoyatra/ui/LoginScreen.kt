package com.example.sastoyatra.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Traveler") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val roles = listOf("Traveler", "Service Provider", "Admin")
    var expanded by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Sasto Yatra Login", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 16.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(value = role, onValueChange = {}, readOnly = true, label = { Text("Role") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).menuAnchor())
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    roles.forEach { selectionOption ->
                        DropdownMenuItem(text = { Text(selectionOption) }, onClick = { role = selectionOption; expanded = false })
                    }
                }
            }
            errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp)) }
            Button(onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    when (role) {
                        "Traveler" -> navController.navigate("traveler_dashboard") { popUpTo("login") { inclusive = true } }
                        "Service Provider" -> {
                            val isApproved = true // Replace with backend check
                            if (isApproved) navController.navigate("provider_dashboard") { popUpTo("login") { inclusive = true } }
                            else navController.navigate("pending_approval")
                        }
                        "Admin" -> navController.navigate("admin_dashboard") { popUpTo("login") { inclusive = true } }
                    }
                } else errorMessage = "Please fill all fields"
            }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text("Login") }
            TextButton(onClick = { navController.navigate("signup") }, modifier = Modifier.padding(top = 8.dp)) { Text("Don't have an account? Sign Up") }
            TextButton(onClick = { navController.navigate("forgot_password") }, modifier = Modifier.padding(bottom = 8.dp)) { Text("Forgot Password?") }
        }
    }
}