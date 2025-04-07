package com.example.sastoyatra.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.animation.animateContentSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelerDashboardScreen(navController: NavController) {
    // Existing dashboard code remains largely unchanged
    val primaryColor = Color(0xFF2E7D32)
    val secondaryColor = Color(0xFFFF8F00)
    val backgroundColor = Color(0xFFF5F5F5)
    val accentColor = Color(0xFF0288D1)

    var serviceOffset by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            serviceOffset = if (serviceOffset == 0f) -100f else 0f
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.TravelExplore,
                            contentDescription = "App Icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sasto Yatra", color = Color.White, fontSize = 20.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User Profile",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        bottomBar = {
            NavigationBar(containerColor = primaryColor) {
                val items = listOf(
                    "Home" to Icons.Default.Home,
                    "Profile" to Icons.Default.Person,
                    "Activity" to Icons.Default.List
                )
                items.forEachIndexed { index, (title, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = title, tint = Color.White) },
                        label = { Text(title, color = Color.White) },
                        selected = index == 0,
                        onClick = {
                            when (title) {
                                "Profile" -> navController.navigate("profile")
                                "Activity" -> navController.navigate("activity")
                                "Home" -> {}
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create_bid") },
                containerColor = secondaryColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Bid")
            }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = { /* Handle search input */ },
                placeholder = { Text("Search Guides, Porters, Agencies...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = accentColor,
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            Text(
                text = "Our Services",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
            LazyRow(
                modifier = Modifier
                    .padding(16.dp)
                    .animateContentSize(animationSpec = tween(1000))
            ) {
                val services = listOf(
                    "Guide" to Icons.Default.People,
                    "Trip" to Icons.Default.Map,
                    "Food" to Icons.Default.Restaurant,
                    "Stay" to Icons.Default.Hotel
                )
                items(services) { (name, icon) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .offset(x = serviceOffset.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = name,
                            tint = primaryColor,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(8.dp)
                        )
                        Text(name, fontSize = 12.sp, color = primaryColor)
                    }
                }
            }

            Text(
                text = "Our Spotlight",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
            LazyRow(modifier = Modifier.padding(16.dp)) {
                items(listOf("Spotlight 1", "Spotlight 2", "Spotlight 3")) { title ->
                    Card(
                        modifier = Modifier
                            .width(200.dp)
                            .height(120.dp)
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.background(primaryColor.copy(alpha = 0.7f))
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate("search_providers") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
                ) {
                    Text("Search Service Providers", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("book_trip") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
                ) {
                    Text("Book Your Trip", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("feedback") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("Rate & Review", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("login") { popUpTo("traveler_dashboard") { inclusive = true } } },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Logout", color = Color.White)
                }
            }
        }
    }
}