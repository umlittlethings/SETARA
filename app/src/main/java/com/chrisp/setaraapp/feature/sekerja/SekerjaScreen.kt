package com.chrisp.setaraapp.feature.sekerja

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.chrisp.setaraapp.navigation.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SekerjaScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Sekerja")
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
            }
        }
    }
}