package com.pjyotianwar.notewithimage.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.pjyotianwar.notewithimage.R
import com.pjyotianwar.notewithimage.destinations.LoginDestination
import com.pjyotianwar.notewithimage.destinations.NotesListDestination
import com.pjyotianwar.notewithimage.viewmodels.CommonViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@Destination(start = true)
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    commonViewModel: CommonViewModel
) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    // AnimationEffect
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )
        delay(3000L)
        if (commonViewModel.isUserLoggedIn()) {
            navigator.navigate(NotesListDestination)
        } else {
            navigator.navigate(LoginDestination)
        }
    }

    // Image
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.note),
            contentDescription = "Logo",
            modifier = Modifier.scale(scale.value)
        )
    }
}