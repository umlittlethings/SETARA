package com.chrisp.setaraapp.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chrisp.setaraapp.R

@Composable
fun ButtonUI(
    modifier: Modifier = Modifier,
    text: String = "Tombol",
    backgroundColor: Color = colorResource(id = R.color.magenta_80),
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    fontWeight: FontWeight = FontWeight.ExtraBold,
    fontSize: Int = 16,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier,
        onClick = onClick, colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor, contentColor = textColor
        ), shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            fontWeight = fontWeight,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }


}


@Preview
@Composable
fun LanjutButton() {

    ButtonUI(text = "Lanjut") {

    }

}

@Preview
@Composable
fun LewatiButton() {
    ButtonUI(text = "Lewati",
        backgroundColor = Color.Transparent,
        textColor = Color.Gray
    ) {

    }


}