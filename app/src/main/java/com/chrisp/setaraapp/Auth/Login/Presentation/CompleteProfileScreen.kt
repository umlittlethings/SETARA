package com.chrisp.setaraapp.Auth.Login.Presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chrisp.setaraapp.Auth.ViewModel.AuthViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chrisp.setaraapp.Model.DataAuth.AuthResponse
import kotlinx.coroutines.launch

@Composable
fun CompleteProfileScreen(
    onComplete: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var catDisability by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Complete Your Profile", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name") })
        OutlinedTextField(value = birthDate, onValueChange = { birthDate = it }, label = { Text("Birth Date") })
        OutlinedTextField(value = catDisability, onValueChange = { catDisability = it }, label = { Text("Disability Category") })
        OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Phone Number") })
        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") })

        if (errorMessage != null) {
            Text(errorMessage ?: "", color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    viewModel.signUpWithGoogleAndCreateProfile(
                        fullName,
                        birthDate,
                        catDisability,
                        phoneNumber,
                        address
                    ).collect { response ->
                        isLoading = false
                        when (response) {
                            is AuthResponse.Success -> onComplete()
                            is AuthResponse.Error -> errorMessage = response.message ?: "Error saving profile"
                        }
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Submit")
        }
    }
}
