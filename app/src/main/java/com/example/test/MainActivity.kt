package com.example.test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Гугл карты
            Button(onClick = {
                // Создаем Intent для открытия Google Maps
                val gmmIntentUri = Uri.parse("geo:0,0?q=Новосибирск") // Можно заменить на нужный адрес
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps") // Указываем, что хотим открыть Google Maps
                // Проверяем, установлен ли Google Maps
                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                } else {
                    // Если Google Maps не установлен, открываем в браузере
                    val webIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    context.startActivity(webIntent)
                }
            }) {
                Text("Открыть Google Maps")
            }
            // Launcher для запуска камеры
            val cameraLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == android.app.Activity.RESULT_OK) {
                    val photo = result.data?.extras?.get("data") as? android.graphics.Bitmap
                    // Обработай фото (например, сохрани его или покажи в ImageView)
                    if (photo != null) {
                        // Тут можно обработать фото
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    // Создаем Intent для открытия камеры
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    // Проверяем, установлена ли камера
                    if (cameraIntent.resolveActivity(context.packageManager) != null) {
                        cameraLauncher.launch(cameraIntent)
                    } else {
                        // Если камера не установлена, показываем сообщение
                        // (например, через Toast или Snackbar)
                    }
                }) {
                    Text("Открыть камеру")
                }
            }
            SwitchWithIconExample()
            SwitchWithIconExample()
            CheckboxWithIconExample()
            CheckboxWithIconExample()
        }
    }
}

@Composable
fun CheckboxWithIconExample() {
    var checked by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it }
        )
        Spacer(modifier = Modifier.width(8.dp)) // Add some space between the checkbox and icon
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = if (checked) Color.Red else Color.Gray
        )
    }
}
@Composable
fun SwitchWithIconExample() {
    var isChecked by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
        Spacer(modifier = Modifier.width(8.dp)) // Add some space between the switch and icon
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            tint = if (isChecked) Color.Yellow else Color.Gray
        )
    }
}