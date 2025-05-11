package com.chrisp.setaraapp.feature.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chrisp.setaraapp.feature.auth.AuthResponse
import com.chrisp.setaraapp.feature.auth.AuthViewModel
import com.chrisp.setaraapp.navigation.BottomNavigationBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel(),
    onLogoutSuccess: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Profile")
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    lifecycleScope.launch {
                        viewModel.logout().collect { response ->
                            when (response) {
                                is AuthResponse.Success -> {
                                    onLogoutSuccess()
                                }
                                is AuthResponse.Error -> {
                                    Toast.makeText(
                                        context,
                                        "Logout gagal: ${response.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }) {
                    Text("Logout")
                }
            }
        }
    }
}