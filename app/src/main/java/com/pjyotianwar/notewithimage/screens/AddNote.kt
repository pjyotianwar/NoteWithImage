package com.pjyotianwar.notewithimage

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.pjyotianwar.notewithimage.destinations.NotesListDestination
import com.pjyotianwar.notewithimage.screens.*
import com.pjyotianwar.notewithimage.viewmodels.CommonViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.qualifiers.ApplicationContext


@OptIn(ExperimentalComposeUiApi::class)
@Destination
@Composable
fun AddNote(
    navigator: DestinationsNavigator,
    commonViewModel: CommonViewModel,
    applicationContext: Context
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar {
                AllIcons(
                    imageVector = Icons.Default.ArrowBack,
                    onIconClicked = {
                        Log.v("Back", "Note")
                        commonViewModel.resetNoteValues()
                        navigator.navigate(NotesListDestination(), onlyIfResumed = true)
                    }
                )

                if (commonViewModel.noteDetail.value)
                    AllDescription(desc = "Note Details")
                else
                    AllDescription(desc = "Add Note")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (commonViewModel.noteDetail.value)
                        AllIcons(
                            imageVector = Icons.Default.Edit,
                            onIconClicked = {
                                commonViewModel.noteDetail.value = false
                            }
                        )
                    else
                        AllIcons(
                        imageVector = Icons.Default.Done,
                        onIconClicked = {
                            if(commonViewModel.validateNote()) {
                                commonViewModel.resetNoteValues()
                                navigator.navigate(NotesListDestination(), onlyIfResumed = true)
                            }
                            else{
                                Toast.makeText(context, "Recheck values entered.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    ) {
        if (commonViewModel.noteDetail.value == true) {
            displayNote(commonViewModel)
        } else {
            editNote(commonViewModel, applicationContext)
        }
    }
}

@Composable
fun editNote(commonViewModel: CommonViewModel, applicationContext: Context) {

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument())
    {
        commonViewModel.addImageUri(it.toString())
        getPath(applicationContext, it)
        Log.d("Uri path", "${it.path} ${it} ${it.authority} ${it.encodedPath}")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            InputText(
                text = commonViewModel.noteTitle.value,
                label = "Title",
                onTextChange = {
                    if (it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        })
                        commonViewModel.noteTitle.value = it
                    if (commonViewModel.noteTitleErrorState.value) {
                        commonViewModel.noteTitleErrorState.value = false
                    }
                },
                maxLine = 3,
                modifier = Modifier.fillMaxWidth(),
                isError = commonViewModel.noteTitleErrorState.value
            )

            if (commonViewModel.noteTitleErrorState.value) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Title length should be in range 5..100", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(6.dp))

            InputText(
                text = commonViewModel.noteDescription.value,
                label = "Description",
                onTextChange = {
                    if (it.all { char -> char.isLetter() || char.isWhitespace() })
                        commonViewModel.noteDescription.value = it
                    if (commonViewModel.noteDescriptionErrorState.value) {
                        commonViewModel.noteDescriptionErrorState.value = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                maxLine = 7,
                isError = commonViewModel.noteDescriptionErrorState.value
            )

            if (commonViewModel.noteTitleErrorState.value) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Description length should be in range 100..1000", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Button(onClick = {
                launcher.launch(arrayOf("image/*"))
            }) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .background(color = MaterialTheme.colors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Add Image")
                        Image(
                            painter = painterResource(id = R.drawable.note),
                            contentDescription = "Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                items(commonViewModel.noteImage){ imageUri->
                    Card(
                        modifier = Modifier
                            .height(500.dp)
                            .width(375.dp)
                            .padding(8.dp),
                        elevation = 4.dp,
                        border = BorderStroke(2.dp, color = Color.Blue)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {

                            AllIcons(
                                imageVector = Icons.Default.Delete,
                                onIconClicked = {
                                    commonViewModel.removeImageUri(imageUri)
                                },
//                                modifier = Modifier.fillMaxWidth().padding(start = 250.dp)
                            )

                            imageLoad(
                                imageData = imageUri.toUri(),
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun displayNote(commonViewModel: CommonViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            AllTitles(title = commonViewModel.noteTitle.value)

            Spacer(modifier = Modifier.height(6.dp))

            AllDescription(desc = commonViewModel.noteDescription.value)

            LazyRow(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                items(commonViewModel.noteImage){ imageUri->
                    Card(
                        modifier = Modifier
                            .height(500.dp)
                            .width(375.dp)
                            .padding(8.dp),
                        elevation = 4.dp
                    ) {
                        imageLoad(
                            imageData = imageUri.toUri(),
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

fun getPath(applicationContext: Context, uri: Uri){
    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        applicationContext.contentResolver.takePersistableUriPermission(uri, takeFlags)
}