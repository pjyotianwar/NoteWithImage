package com.pjyotianwar.notewithimage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pjyotianwar.notewithimage.destinations.*
import com.pjyotianwar.notewithimage.screens.Login
import com.pjyotianwar.notewithimage.screens.NotesList
import com.pjyotianwar.notewithimage.screens.SignUp
import com.pjyotianwar.notewithimage.screens.SplashScreen
import com.pjyotianwar.notewithimage.ui.theme.NoteWithImageTheme
import com.pjyotianwar.notewithimage.viewmodels.CommonViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (!granted) {
            Toast.makeText(this, "You must grant permission!", Toast.LENGTH_LONG).show()
            finishAffinity()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                // relaunch app
                startActivity(Intent(this, MainActivity::class.java))
            }, 800)

            finishAffinity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            setContent {

                val commonViewModel = viewModel<CommonViewModel>()
                val context = this

                NoteWithImageTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        DestinationsNavHost(navGraph = NavGraphs.root,) {
                            composable(SplashScreenDestination) {
                                SplashScreen(
                                    navigator = destinationsNavigator,
                                    commonViewModel = commonViewModel
                                )
                            }

                            composable(AddNoteDestination) {
                                AddNote(
                                    navigator = destinationsNavigator,
                                    commonViewModel = commonViewModel,
                                    applicationContext = applicationContext
                                )
                            }

                            composable(NotesListDestination) {
                                NotesList(
                                    navigator = destinationsNavigator,
                                    commonViewModel = commonViewModel
                                )
                            }

                            composable(LoginDestination) {
                                Login(
                                    navigator = destinationsNavigator,
                                    commonViewModel = commonViewModel
                                )
                            }

                            composable(SignUpDestination) {
                                SignUp(
                                    navigator = destinationsNavigator,
                                    commonViewModel = commonViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Destination
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NoteWithImageTheme {
        Greeting("Android")
    }
}