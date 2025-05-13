package com.chrisp.setaraapp.Home.Presentation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.chrisp.setaraapp.Auth.ViewModel.AuthViewModel
import com.chrisp.setaraapp.Model.DataAuth.AuthResponse
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    onLogoutSuccess: () -> Unit,
    navToCourseList: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to Setara App!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You have successfully logged in.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navToCourseList() }) {
            Text("Lihat Daftar Kursus")
        }

        Button(onClick = {
            lifecycleScope.launch {
                authViewModel.logout().collect { response ->
                    when (response) {
                        is AuthResponse.Success -> {
                            onLogoutSuccess()
                        }
                        is AuthResponse.Error -> {
                            Toast.makeText(
                                context,
                                "Logout failed: ${response.message}",
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


