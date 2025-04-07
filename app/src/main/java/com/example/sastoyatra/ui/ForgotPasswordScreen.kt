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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import android.util.Log
import kotlin.random.Random

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    val db = Firebase.firestore
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val tag = "ForgotPassword"

    // Replace with your SendGrid API key
    val sendGridApiKey = "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\n" // Replace with your actual API key
    val client = OkHttpClient()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Reset Password", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 16.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
            errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp)) }
            successMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 8.dp)) }
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
            }
            Button(onClick = {
                if (email.isNotEmpty()) {
                    isLoading = true
                    scope.launch {
                        Log.d(tag, "Button clicked, email: $email")
                        try {
                            // Generate OTP
                            val otp = Random.nextInt(1000, 9999).toString()
                            Log.d(tag, "Generated OTP: $otp")

                            // Store in Firestore
                            Log.d(tag, "Attempting Firestore write")
                            db.collection("otp_verifications").document(email).set(mapOf("otp" to otp, "timestamp" to System.currentTimeMillis())).await()
                            Log.d(tag, "Firestore write successful")

                            // SendGrid request
                            val json = JSONObject().apply {
                                put("personalizations", JSONArray().put(JSONObject().put("to", JSONArray().put(JSONObject().put("email", email)))))
                                put("from", JSONObject().put("email", "yourname@gmail.com")) // Replace with your verified sender email
                                put("subject", "Your Sasto Yatra OTP")
                                put("content", JSONArray().put(JSONObject().put("type", "text/plain").put("value", "Your OTP is $otp. It expires in 5 minutes.")))
                            }
                            val requestBody = json.toString().toRequestBody("application/json".toMediaType())
                            val request = Request.Builder()
                                .url("https://api.sendgrid.com/v3/mail/send")
                                .header("Authorization", "Bearer $sendGridApiKey")
                                .post(requestBody)
                                .build()

                            Log.d(tag, "Sending SendGrid request")
                            val response = client.newCall(request).execute()
                            Log.d(tag, "SendGrid response: ${response.code} - ${response.message}")
                            if (response.isSuccessful) {
                                successMessage = "OTP sent to $email"
                                errorMessage = null
                                Log.d(tag, "Navigating to verify_otp")
                                navController.navigate("verify_otp/$email")
                            } else {
                                errorMessage = "SendGrid failed: ${response.message} (Code: ${response.code})"
                                successMessage = null
                            }
                            response.close()
                        } catch (e: Exception) {
                            Log.e(tag, "Exception: ${e.message}", e)
                            errorMessage = "Error: ${e.message}"
                            successMessage = null
                        } finally {
                            Log.d(tag, "Resetting isLoading")
                            isLoading = false
                        }
                    }
                } else {
                    errorMessage = "Please enter your email"
                    successMessage = null
                }
            }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp), enabled = !isLoading) { Text("Send OTP") }
            TextButton(onClick = { navController.navigate("login") }, modifier = Modifier.padding(top = 8.dp)) { Text("Back to Login") }
        }
    }
}