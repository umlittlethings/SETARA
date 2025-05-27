package com.chrisp.setaraapp.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField // Pastikan menggunakan OutlinedTextField dari M3
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chrisp.setaraapp.R

@Composable
fun TextFieldUI(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable () -> Unit)? = null, // TAMBAHKAN PARAMETER INI
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textColor: Color = colorResource(id = R.color.magenta_80),
    borderColor: Color = colorResource(id = R.color.borderPink),
    placeholderColor: Color = colorResource(id = R.color.borderPink),
    iconButtonColors: Color = colorResource(id = R.color.magenta_80), // Ini untuk leadingIcon
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
    singleLine: Boolean = true // Tambahkan parameter singleLine
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = placeholderColor
            )
        },
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null, // Deskripsi bisa ditambahkan jika perlu untuk aksesibilitas
                    tint = iconButtonColors
                )
            }
        },
        trailingIcon = trailingIcon, // GUNAKAN PARAMETER trailingIcon DI SINI
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = textColor,
            unfocusedBorderColor = borderColor,
            focusedTextColor = textColor,
            unfocusedTextColor = if (value.isEmpty()) placeholderColor else textColor, // Teks yang diketik user
            focusedLeadingIconColor = iconButtonColors,
            unfocusedLeadingIconColor = iconButtonColors,
            // Anda mungkin juga ingin mengatur warna trailingIcon jika defaultnya tidak sesuai
            // focusedTrailingIconColor = ...,
            // unfocusedTrailingIconColor = ...,
        ),
        textStyle = TextStyle(fontSize = 14.sp, color = textColor),
        singleLine = singleLine, // Terapkan singleLine
    )
}

@Composable
fun CustomIconTextFieldUI(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: Painter? = null,
    trailingIcon: (@Composable () -> Unit)? = null, // TAMBAHKAN PARAMETER INI
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textColor: Color = colorResource(id = R.color.magenta_80),
    borderColor: Color = colorResource(id = R.color.borderPink),
    placeholderColor: Color = colorResource(id = R.color.borderPink),
    iconButtonColors: Color = colorResource(id = R.color.magenta_80),
    singleLine: Boolean = true // Tambahkan parameter singleLine
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = placeholderColor
            )
        },
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    painter = it,
                    contentDescription = null,  // Deskripsi bisa ditambahkan jika perlu untuk aksesibilitas
                    tint = iconButtonColors
                )
            }
        },
        trailingIcon = trailingIcon, // GUNAKAN PARAMETER trailingIcon DI SINI
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = textColor,
            unfocusedBorderColor = borderColor,
            focusedTextColor = textColor,
            unfocusedTextColor = if (value.isEmpty()) placeholderColor else textColor,
            focusedLeadingIconColor = iconButtonColors,
            unfocusedLeadingIconColor = iconButtonColors,
        ),
        textStyle = TextStyle(fontSize = 14.sp, color = textColor),
        singleLine = singleLine, // Terapkan singleLine
    )
}



@Preview(showBackground = true)
@Composable
private fun TextFieldComponentPrev() {
    TextFieldUI(
        value = "",
        onValueChange = {},
        placeholder = "Email",
        leadingIcon = Icons.Default.Email,
        keyboardType = KeyboardType.Email,
        trailingIcon = { Icon(Icons.Default.Email, contentDescription = null)} // Contoh penggunaan trailing icon di preview
    )
}