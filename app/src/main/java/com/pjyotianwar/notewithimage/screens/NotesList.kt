package com.pjyotianwar.notewithimage.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Note
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.pjyotianwar.notewithimage.MainActivity
import com.pjyotianwar.notewithimage.destinations.AddNoteDestination
import com.pjyotianwar.notewithimage.destinations.LoginDestination
import com.pjyotianwar.notewithimage.viewmodels.CommonViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun NotesList(
    navigator: DestinationsNavigator,
    commonViewModel: CommonViewModel
) {
    val context = LocalContext.current

    commonViewModel.getUserDetails()
    commonViewModel.getNotes()

    val notesList = commonViewModel.noteList.collectAsState().value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.navigate(AddNoteDestination())
                }
            ) {
                Icon(imageVector = Icons.Default.Note, contentDescription = null)
            }
        },
        topBar = {
            TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                AllTitles("Notes List")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AllIcons(
                        imageVector = Icons.Default.Logout,
                        onIconClicked = {
                            if (commonViewModel.logout()){
                                navigator.navigate(LoginDestination)
                            }
                            makeToast(context, "Log Out Successful")
                        }
                    )
                }
            }
        }
    ) {
        LazyColumn {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 8.dp),
                    elevation = 5.dp,
                    backgroundColor = MaterialTheme.colors.primary.copy(.7f),
                    border = BorderStroke(width = 4.dp, MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "Welcome!",
                            modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )

                        Text(
                            text = "${commonViewModel.currentUserName.value}",
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
            items(notesList) { note ->
                NoteItem(
                    note,
                    onDeleteClicked = { commonViewModel.removeNote(note) },
                    onEditClicked = {
                        commonViewModel.editNote.value = true
                        commonViewModel.setNoteValues(note)
                        navigator.navigate(AddNoteDestination())
                    },
                    onClick = {
                        commonViewModel.noteDetail.value = true
                        commonViewModel.setNoteValues(note)
                        navigator.navigate(AddNoteDestination())
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}