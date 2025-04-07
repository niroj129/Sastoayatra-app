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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ResetPasswordScreen(navController: NavController, email: String) {
    val auth = Firebase.auth
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Set New Password", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 16.dp))
            OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("New Password") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
            OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
            errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp)) }
            Button(onClick = {
                scope.launch { // Moved the scope.launch here.
                    when {
                        newPassword.isEmpty() || confirmPassword.isEmpty() -> errorMessage = "Please fill all fields"
                        newPassword != confirmPassword -> errorMessage = "Passwords do not match"
                        newPassword.length < 6 -> errorMessage = "Password must be at least 6 characters"
                        else -> {
                            try {
                                auth.signInWithEmailAndPassword(email, newPassword).addOnFailureListener {
                                    scope.launch { // Added scope.launch for the await() call within the listener.
                                        auth.createUserWithEmailAndPassword(email, newPassword).await() // Create if not exists
                                    }
                                }.await()
                                val role = when { // Replace with Firestore fetch
                                    email.contains("admin") -> "Admin"
                                    email.contains("provider") -> "Service Provider"
                                    else -> "Traveler"
                                }
                                when (role) {
                                    "Traveler" -> navController.navigate("traveler_dashboard") { popUpTo("login") { inclusive = true } }
                                    "Service Provider" -> {
                                        val isApproved = true // Replace with Firestore check
                                        if (isApproved) navController.navigate("provider_dashboard") { popUpTo("login") { inclusive = true } }
                                        else navController.navigate("pending_approval") { popUpTo("login") { inclusive = true } }
                                    }
                                    "Admin" -> navController.navigate("admin_dashboard") { popUpTo("login") { inclusive = true } }
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error: ${e.message}"
                            }
                        }
                    }
                }
            }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text("Reset Password") }
        }
    }
}