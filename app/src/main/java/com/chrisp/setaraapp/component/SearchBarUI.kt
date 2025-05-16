package com.chrisp.setaraapp.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.chrisp.setaraapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarUI() {
    var searchQuery by remember { mutableStateOf("") }
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Cari program", color = colorResource(id = R.color.magenta_80).copy(alpha = 0.6f) ) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = colorResource(id = R.color.magenta_80)
            )
        },
        shape = RoundedCornerShape(8.dp), // Highly rounded corners
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = colorResource(id = R.color.magenta_80),
            unfocusedIndicatorColor = colorResource(id = R.color.magenta_80).copy(alpha = 0.7f),
            disabledIndicatorColor = colorResource(id = R.color.magenta_80).copy(alpha = 0.5f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            cursorColor = colorResource(id = R.color.magenta_80),
            focusedLeadingIconColor = colorResource(id = R.color.magenta_80),
            unfocusedLeadingIconColor = colorResource(id = R.color.magenta_80),
        ),
        singleLine = true
    )
}