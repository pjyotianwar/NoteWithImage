package com.pjyotianwar.notewithimage.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pjyotianwar.notewithimage.destinations.NotesListDestination
import com.pjyotianwar.notewithimage.destinations.SignUpDestination
import com.pjyotianwar.notewithimage.viewmodels.CommonViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalComposeUiApi::class)
@Destination()
@Composable
fun Login(
    navigator: DestinationsNavigator,
    commonViewModel: CommonViewModel
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Login",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.padding(12.dp))

            OutlinedTextField(
                label = {
                    if (commonViewModel.loginWithMail.value)
                        Text("Email ID")
                    else
                        Text("Phone")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType =
                    if (commonViewModel.loginWithMail.value)
                        KeyboardType.Email
                    else
                        KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                value = commonViewModel.luserName.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    if (commonViewModel.luserNameErrorState.value) {
                        commonViewModel.luserNameErrorState.value = false
                    }
                    commonViewModel.luserName.value = it
                },
                trailingIcon = {
                    IconButton(onClick = {
                        commonViewModel.loginWithMail.value = !commonViewModel.loginWithMail.value
                        commonViewModel.luserName.value = ""
                    }) {
                        Icon(
                            imageVector =
                            if (commonViewModel.loginWithMail.value)
                                Icons.Default.Mail
                            else
                                Icons.Default.Phone,
                            contentDescription = "login with",
                            tint = Color.Blue
                        )
                    }
                },
                isError = commonViewModel.luserNameErrorState.value,
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                })
            )

            if (commonViewModel.luserNameErrorState.value) {
                Text(text = "Required", color = Color.Red)
            }

            Spacer(modifier = Modifier.padding(8.dp))

            val passwordVisibility = remember { mutableStateOf(true) }

            OutlinedTextField(
                label = {
                    Text("Password")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                value = commonViewModel.lpassword.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    if (commonViewModel.lpasswordErrorState.value) {
                        commonViewModel.lpasswordErrorState.value = false
                    }
                    commonViewModel.lpassword.value = it
                },
                isError = commonViewModel.lpasswordErrorState.value,
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                        Icon(
                            imageVector =
                            if (passwordVisibility.value)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility,
                            contentDescription = "visibility",
                            tint = Color.Red
                        )
                    }
                },
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }),
                visualTransformation =
                if (passwordVisibility.value)
                    PasswordVisualTransformation()
                else
                    VisualTransformation.None
            )

            if (commonViewModel.lpasswordErrorState.value) {
                Text(text = "Required", color = Color.Red)
            }

            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    commonViewModel.login()
                }
            )
            {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.padding(12.dp))

            Text(
                text = "Or",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(12.dp))

            Button(
                onClick = {
                    navigator.navigate(SignUpDestination){
                        popUpToRoute
                    }
                }
            ) {
                Text(text = "Sign UP")
            }
        }
        when(commonViewModel.loginResult.value){
            LoginResult.NoUser->{
                makeToast(context, "No such user!")
            }
            LoginResult.Wrong->{
                makeToast(context, "Wrong Username or password")
            }
            LoginResult.Success->{
                makeToast(context, "Login Successful")
                navigator.navigate(NotesListDestination){
                    popUpToRoute
                }
                commonViewModel.resetSignUpResult()
                commonViewModel.resetLoginResult()
            }
        }
    }
}