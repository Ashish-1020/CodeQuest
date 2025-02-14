package com.example.codequest.presentation.ui.Screens

import android.content.Intent
import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.rotate

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Splashscreen {
                // Navigate to StartScreen
                startActivity(Intent(this, StartScreen::class.java))
                finish() // Finish SplashScreen so it's not on the back stack
            }
        }
    }
}

@Composable
fun Splashscreen(onNavigate: () -> Unit) {
    // Wait for 3 seconds and navigate
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000) // 3-second delay
        onNavigate() // Trigger navigation
    }
    val bounceAnimation = rememberInfiniteTransition()

    // Bounce animation for the logo
  /*  val translationY by bounceAnimation.animateFloat(
        initialValue = -30f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                -30f at 0 with LinearEasing // Start above the screen
                0f at 800 with LinearEasing // Fall down
              -30f at 1600 with LinearEasing // Bounce back slightly

            }
        )
    )*/

    // Text color animation from white to gray
    val textColor by bounceAnimation.animateColor(
        initialValue = Color.White,
        targetValue = Color.Gray,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Rotation animation for circular progress
    val rotation by bounceAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E88E5)), // Blue background
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Code logo with bounce animation
            Text(
                text = "</>",
                fontSize = 78.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
               // modifier = Modifier.offset(y = translationY.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // App title with animated text color
            Text(
                text = "CodeQuest",
                fontSize = 36.sp,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            // App subtitle with animated text color
            Text(
                text = "Where Coding Meets Competition",
                fontSize = 14.sp,
                color = textColor
            )


            Spacer(modifier = Modifier.height(32.dp))

            // Circular progress indicator with rotation
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier
                    .size(48.dp)
                    .rotate(rotation)
            )
        }

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(bottom = 30.dp), // Blue background
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(text="Version:1.0.0", color = Color.White)
    }
}

@Composable
@Preview
fun PreviewSplashScreen() {
    SplashScreen()
}