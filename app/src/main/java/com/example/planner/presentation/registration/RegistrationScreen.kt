package com.example.planner.presentation.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.planner.R



@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RegistrationScreen( navController: NavController,viewModel: RegistrationScreenViewModel = hiltViewModel()) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A7DDF), Color(0xFF4AD6DF)),
                    startY = 0.0f,
                    endY = 1500.0f
                )
            )
    ) {
        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
        )

        {

            Text(
                text = "Create Account",
                fontSize = 42.sp,
                color = Color.White

            )

        }

        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 180.dp)
        )

        {

            Image(
                painter = painterResource(id = R.drawable.office),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)

            )

        }


        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 360.dp)
        )

        {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)


                )

                {

                    val state = viewModel.viewState.value

                    TextField(
                        value = state.firstname,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true,
                        onValueChange = {
                            viewModel.handleEvents(RegistrationScreenEvent.FirstnameChanged(it))
                        },
                        placeholder = { Text("First Name") },

                        )
                    TextField(
                        value = state.lastname,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true,
                        onValueChange = {
                            viewModel.handleEvents(RegistrationScreenEvent.LastnameChanged(it))
                        },
                        placeholder = { Text("Last Name") },

                        )



                    TextField(
                        value = state.username,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true,
                        onValueChange = {
                            viewModel.handleEvents(RegistrationScreenEvent.UsernameChanged(it))
                        },
                        placeholder = { Text("Email adress") },

                        )


                    var passwordVisible by rememberSaveable { mutableStateOf(false) }

                    TextField(
                        value = state.password,
                        onValueChange = {

                            viewModel.handleEvents(RegistrationScreenEvent.PasswordChanged(it))
                        },

                        label = { Text("Password") },
                        singleLine = true,
                        placeholder = { Text("Password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )

                        )

                    Column(

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(bottom = 20.dp)

                    ) {


                        Button(
                            onClick = {
                                viewModel.handleEvents(
                                    RegistrationScreenEvent.LoginButtonPressed(
                                        state.firstname,
                                        state.lastname,
                                        state.username,
                                        state.password
                                    )
                                )

                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent // providna pozadina
                            ),

                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .width(200.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color(0xFF4A7DDF), Color(0xFF4AD6DF)),
                                        startY = 0.0f,
                                        endY = 150.0f
                                    ),
                                    shape = RoundedCornerShape(24.dp)
                                )
                        ) {
                            Text(
                                text = "Create Account",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }

                        val signUpText = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Black)) {
                                append("Already have an account? ")
                            }
                            withStyle(style = SpanStyle(color = Color(0xFF4AD6DF))) {
                                append("Login")
                            }
                        }

                        LaunchedEffect(viewModel.effect) {
                            viewModel.effect.collect { effect ->
                                when (effect) {
                                    is RegistrationScreenEffect.NavigateToLoginScreen -> {
                                        navController.navigate("login")
                                    }

                                    is RegistrationScreenEffect.NavigateToDashboardScreen -> {
                                        navController.navigate("dashboard")
                                    }

                                }
                            }
                        }



                        Text(
                            text = signUpText,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable {

                                viewModel.handleEvents(RegistrationScreenEvent.SignUpButtonPressed())

                            }
                        )

                    }

                }


            }


        }


    }
}
@Preview
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen( navController= rememberNavController())
}