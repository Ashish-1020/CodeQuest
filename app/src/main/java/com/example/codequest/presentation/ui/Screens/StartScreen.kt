package com.example.codequest.presentation.ui.Screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class StartScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
  // ReminderScreenWithDialog()
        //SetReminderScreen()
           Startscreen{
               startActivity(Intent(this, HomeScreen::class.java))
               finish()
           }
        }
    }
}


@Composable
fun Startscreen(onNavigate: () -> Unit) {

    val bounceAnimation = rememberInfiniteTransition()

    val textColor by bounceAnimation.animateColor(
        initialValue = Color(0xFF1E88E5),
        targetValue = Color.Gray,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val logocolor by bounceAnimation.animateColor(
        initialValue = Color.White,
        targetValue = Color.Gray,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)), // Light background color
        contentAlignment = Alignment.TopCenter // Start positioning from the top
    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val offsetY = screenHeight * 0.25f

        // Circular Box
        Box(
            modifier = Modifier
                .offset(y = offsetY)
                .size(200.dp)
                .background(
                    color = textColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Code Symbol
            Text(
                text = "</>",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = logocolor
            )
        }

        // Main Content Column for centering
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = offsetY + 224.dp)
        ) {
            // Title Text
            Text(
                text = "Join Coding Challenges",
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            // Subtitle Text
            Text(
                text = "Participate in exciting coding contests and \n improve your skills",
                fontSize = 16.sp,
                color = Color.Black.copy(0.6f),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )

            Button(
                onClick = {
                   onNavigate()
                },
                modifier = Modifier.fillMaxWidth().padding(start = 4.dp,end=4.dp, top = 100.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
            ) {
                Text(
                    text = "{ StartJourney ? \"Let’s Go!\" : \"Come Back!\" }",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

    }

}




