package com.example.wollyapp.presentation.showDialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun ShowDialogScreen(
    message: String,
    onConfirmPressed: () -> Unit,
    onCancelPressed: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.border(
            3.dp,
            Brush.verticalGradient(
                colors = listOf(Color(0xFF4AD6DF), Color(0xFF4A7DDF)),
                startY = 0.0f,
                endY = 150.0f
            ),
            RoundedCornerShape(size = 24.dp)
        ),
        onDismissRequest = {
            onCancelPressed()
        },

        text = {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = message
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {

                OutlinedButton(

                    colors = ButtonDefaults.outlinedButtonColors(Color.White),
                    shape = RoundedCornerShape(size = 8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF4A7DDF), Color(0xFF4AD6DF)),
                            startY = 0.0f,
                            endY = 150.0f
                        ),
                    ),
                    onClick = {  onConfirmPressed() },
                    modifier = Modifier
                        .width(110.dp)
                        .height(45.dp),
                    contentPadding = PaddingValues(
                        top = 0.dp,
                        bottom = 0.dp,
                        start = 8.dp,
                        end = 8.dp,
                    ),
                ) {


                    Text(
                        text = "Confirm",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )

                }

            }
        },
        dismissButton = {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                OutlinedButton(

                    colors = ButtonDefaults.outlinedButtonColors(Color.White),
                    shape = RoundedCornerShape(size = 8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF4A7DDF), Color(0xFF4AD6DF)),
                            startY = 0.0f,
                            endY = 150.0f
                        ),
                    ),
                    onClick = { onCancelPressed() },
                    modifier = Modifier
                        .width(110.dp)
                        .height(45.dp),
                    contentPadding = PaddingValues(
                        top = 0.dp,
                        bottom = 0.dp,
                        start = 8.dp,
                        end = 8.dp,
                    ),
                ) {


                    Text(
                        text = "Cancel",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )

                }
            }
        },
        containerColor = Color.White
    )
}