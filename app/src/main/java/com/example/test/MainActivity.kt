package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.database.AppDatabase
import com.example.test.database.UserViewModel
import com.example.test.database.UserViewModelFactory
import com.example.test.ui.theme.TestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            val database = AppDatabase.getDatabase(context)
            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModelFactory(database.userDao())
            )
            TestTheme {
                Navigation(userViewModel)
            }
        }
    }
}

@Composable
fun Navigation(userViewModel: UserViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { StartScreen(userViewModel, navController)  }
        composable("second_screen") { AnotherScreen(userViewModel, navController) }
    }
}

@Composable
fun StartScreen(userViewModel: UserViewModel, navController: NavController){
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Login") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (login.isNotEmpty() && password.isNotEmpty()){
                userViewModel.saveUser(login, password)
                navController.navigate("second_screen")
            }
        }) {
            Text("Save and Go to Second Screen")
        }
    }
}

@Composable
fun AnotherScreen(userViewModel: UserViewModel, navController: NavController){
    var greetingMessage by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        val login = userViewModel.getUserLogin()
        greetingMessage = if (login != null) {
            "Hello, $login!"
        } else {
            "User not found."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button( onClick = {navController.navigate("home")}) {
            Text("Go back")
        }
        Text(
            text = greetingMessage,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}