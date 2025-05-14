package com.chrisp.setaraapp.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chrisp.setaraapp.R

@Composable
fun LongTextFieldUI(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textColor: Color = colorResource(id = R.color.magenta_80),
    borderColor: Color = colorResource(id = R.color.borderPink),
    placeholderColor: Color = colorResource(id = R.color.borderPink),
    minHeight: Int = 100,
    maxHeight: Int = 200
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight.dp, max = maxHeight.dp),
        placeholder = {
            Text(
                text = placeholder,
                color = placeholderColor
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
            focusedTextColor = textColor,
            unfocusedTextColor = borderColor,
        ),
        textStyle = TextStyle(fontSize = 14.sp, color = textColor),
        maxLines = Int.MAX_VALUE
    )
}

@Preview(showBackground = true)
@Composable
private fun TextFieldComponentPrev() {
    LongTextFieldUI(
        value = "",
        onValueChange = {},
        placeholder = "Placeholder",
        keyboardType = KeyboardType.Text,
        visualTransformation = VisualTransformation.None,
        textColor = colorResource(id = R.color.magenta_80),
        borderColor = colorResource(id = R.color.borderPink),
        placeholderColor = colorResource(id = R.color.borderPink),
    )
}