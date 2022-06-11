package com.pjyotianwar.notewithimage.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import com.pjyotianwar.notewithimage.destinations.LoginDestination
import com.pjyotianwar.notewithimage.destinations.NotesListDestination
import com.pjyotianwar.notewithimage.viewmodels.CommonViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalComposeUiApi::class)
@Destination
@Composable
fun SignUp(
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
                text = "Sign UP",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.padding(12.dp))

            OutlinedTextField(
                label = {
                    Text("Name")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                value = commonViewModel.sname.value,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    if (commonViewModel.snameErrorState.value) {
                        commonViewModel.snameErrorState.value = false
                    }
                    commonViewModel.sname.value = it
                },
                isError = commonViewModel.snameErrorState.value,
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                })
            )

            if (commonViewModel.snameErrorState.value) {
                Text(text = "Name Length should be in range 8-15", color = Color.Red)
            }

            Spacer(modifier = Modifier.padding(12.dp))

            OutlinedTextField(
                label = {
                    Text("Mobile")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                value = commonViewModel.sphone.value,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    if (commonViewModel.sphoneErrorState.value) {
                        commonViewModel.sphoneErrorState.value = false
                    }
                    commonViewModel.sphone.value = it
                },
                isError = commonViewModel.sphoneErrorState.value,
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }
                )
            )

            if (commonViewModel.sphoneErrorState.value) {
                Text(text = "Enter valid phone number", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                label = {
                    Text("Email ID")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                value = commonViewModel.semail.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    if (commonViewModel.semailErrorState.value) {
                        commonViewModel.semailErrorState.value = false
                    }
                    commonViewModel.semail.value = it
                },
                isError = commonViewModel.semailErrorState.value,
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                })
            )

            if (commonViewModel.semailErrorState.value) {
                Text(text = "Enter valid Mail ID", color = Color.Red)
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
                value = commonViewModel.spassword.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    if (commonViewModel.spasswordErrorState.value) {
                        commonViewModel.spasswordErrorState.value = false
                    }
                    commonViewModel.spassword.value = it
                },
                isError = commonViewModel.spasswordErrorState.value,
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

            if (commonViewModel.spasswordErrorState.value) {
                Text(
                    text = "Password should contain atleast\n2 Uppercase letters\n2 digits\n1 special character\nstart with Lowercase letter",
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    commonViewModel.signUpUser()
//                    when (commonViewModel.signUpUser()) {
//                        0 -> {
//                            makeToast(context, "SignUp Successful")
//                            navigator.navigate(NotesListDestination)
//                        }
//                        -1 -> {
//                            makeToast(context, "Revalidate entered details")
//                        }
//                        1 -> {
//                            makeToast(context, "User already exists.")
//                        }
//                    }
                }
            )
            {
                Text(text = "Sign UP")
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
                    navigator.navigate(LoginDestination)
                }
            ) {
                Text(text = "Login")
            }
        }
        when (commonViewModel.signUpResult.value) {
            SignUpResult.Success->{
                makeToast(context, "SignUp Successful")
                navigator.navigate(NotesListDestination)
                commonViewModel.resetSignUpResult()
                commonViewModel.resetLoginResult()
            }
            SignUpResult.UserExists->{
                makeToast(context, "User already exists.")
            }
        }
    }
}