package com.example.sastoyatra.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun VerifyOTPScreen(navController: NavController, email: String) {
    val db = Firebase.firestore
    var otp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Verify OTP", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 16.dp))
            Text(text = "Enter the OTP sent to $email", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 16.dp))
            OutlinedTextField(value = otp, onValueChange = { otp = it }, label = { Text("OTP") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp)) }
            Button(onClick = {
                if (otp.isNotEmpty()) {
                    scope.launch {
                        try {
                            val doc = db.collection("otp_verifications").document(email).get().await()
                            val storedOtp = doc.getString("otp")
                            if (otp == storedOtp) {
                                navController.navigate("reset_password/$email") { popUpTo("verify_otp/$email") { inclusive = true } }
                            } else {
                                errorMessage = "Invalid OTP"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error: ${e.message}"
                        }
                    }
                } else errorMessage = "Enter OTP"
            }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text("Verify OTP") }
            TextButton(onClick = { navController.navigate("forgot_password") }, modifier = Modifier.padding(top = 8.dp)) { Text("Resend OTP") }
        }
    }
}