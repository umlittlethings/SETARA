package com.chrisp.setaraapp.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textColor: Color = colorResource(id = R.color.magenta_80),
    borderColor: Color = colorResource(id = R.color.borderPink),
    placeholderColor: Color = colorResource(id = R.color.borderPink),
    iconButtonColors: Color = colorResource(id = R.color.magenta_80),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
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
                    contentDescription = null,
                    tint = iconButtonColors
                )
            }
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = textColor,
            unfocusedBorderColor = borderColor,
            focusedTextColor = textColor,
            unfocusedTextColor = borderColor,
        ),
        textStyle = TextStyle(fontSize = 14.sp, color = textColor),
        singleLine = true,
    )
}

@Composable
fun CustomIconTextFieldUI(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: Painter? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textColor: Color = colorResource(id = R.color.magenta_80),
    borderColor: Color = colorResource(id = R.color.borderPink),
    placeholderColor: Color = colorResource(id = R.color.borderPink),
    iconButtonColors: Color = colorResource(id = R.color.magenta_80),
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
                    contentDescription = null,
                    tint = iconButtonColors
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = textColor,
            unfocusedBorderColor = borderColor,
            focusedTextColor = textColor,
            unfocusedTextColor = borderColor,
        ),
        textStyle = TextStyle(fontSize = 14.sp, color = textColor),
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
        keyboardType = KeyboardType.Email
    )
}