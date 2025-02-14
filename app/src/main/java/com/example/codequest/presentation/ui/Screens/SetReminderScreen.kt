package com.example.codequest.presentation.ui.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codequest.R
import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.*

import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.example.codequest.data.Contest
import com.example.codequest.presentation.ui.ContestViewModel
import com.example.codequest.room.data.ContestReminder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@Composable
fun SetReminderScreen(id: Int,contestName:String,contestPlatform:String,startTime:String,contestViewModel: ContestViewModel){
    val context = LocalContext.current

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val utcDate = dateFormat.parse(startTime)!!
    val localDateFormat = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale("en", "IN"))
    localDateFormat.timeZone = TimeZone.getDefault()
    val formattedLocalTime = localDateFormat.format(utcDate)
    val localTimeMillis = utcDate.time
    var savetxt:String="Save"
    var name by remember { mutableStateOf(contestName) }
    var platform by remember { mutableStateOf(contestPlatform) }
    var time by remember { mutableStateOf(startTime) }


    var ReminderTime: Long = remember { 29 }
    Column(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) { // Title
            Row {

                Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Set Reminder",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                )
            }}

            Row {

            Column {
                Text(
                    text = savetxt,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color =  Color(0xFF1E88E5),
                    modifier = Modifier.clickable{
                        val reminderOffset =(ReminderTime * 60 * 1000)
                        val selectedTimeInMillis =  localTimeMillis-reminderOffset// 1 minute later
                        // Format the selected time to display in the desired format
                        val dateFormat = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale("en", "IN"))
                        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata") // IST timezone
                        val formattedTime = dateFormat.format(Date(selectedTimeInMillis))
                        val contest = ContestReminder(
                            id=id,
                            contestName = name,
                            contestPlatform = platform,
                            contestTime = time
                        )

                        // Call the insertContest function to save the contest in the database
                        contestViewModel.insertContest(contest)

                        // Show Toast with formatted time
                        Toast.makeText(context, "Alarm set for: $formattedTime", Toast.LENGTH_LONG).show()
                       scheduleAlarm(context, selectedTimeInMillis)



                    }.padding(end = 8.dp)
                )



            }}



        }

        Divider()
        Spacer(modifier = Modifier.height(8.dp))






    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
        ,//elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Blue.copy(0.05f)
        )

    ) {
        Row(modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            val platformIcon = when (contestName) {
                "leetcode" -> R.drawable.leetcode
                "geeksforgeeks" -> R.drawable.gfg
                "codeforces" -> R.drawable.codeforces
                "codechef"->R.drawable.codechef
                "atcoder"->R.drawable.atcoder
                else -> null
            }

            platformIcon?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription =contestName,
                    modifier = Modifier.size(24.dp).padding(end = 4.dp) // Adjust icon size and padding
                )
            }
            Text(
                modifier = Modifier.padding( 8.dp),
                text = "$contestName",
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                color = Color.Black
            )
        }

        Row(modifier = Modifier.padding(8.dp)) {

            Text(
                modifier = Modifier.padding( 8.dp),
                text = "$contestPlatform",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black.copy(0.7f)
            )
        }

        Row(  modifier = Modifier.padding( 8.dp),) {
            Icon(Icons.Default.DateRange,
                contentDescription = "vghvh",
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = "${formattedLocalTime}",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black.copy(0.7f)
            )
        }
    }




        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
       ReminderTimeDropdown(ReminderTime){ newTime ->
           ReminderTime = newTime
       }

        }












    }
}

fun formatDateTime(startMillis: Long): String {
    val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy 'from' h:mm a", Locale.ENGLISH)
    return dateFormat.format(Date(startMillis))
}

@Composable
fun ReminderTimeDropdown(reminderTime: Long, onReminderTimeChange: (Long) -> Unit){

    var isPlaying by remember {
        mutableStateOf(true)
    }

    var speed by remember {
        mutableStateOf(1f)
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.anim)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("30 Minutes") }

    val options = listOf(
        "5 minutes before" to 5L,
        "10 minutes before" to 10L,
        "30 minutes before" to 30L,
        "1 hour before" to 60L
    )

    val soundOptions = listOf("Sound 1", "Sound 2", "Sound 3")
    var selectedSound by remember { mutableStateOf(soundOptions[0]) }
    val context = LocalContext.current

    // Variable to hold and stop the MediaPlayer
    var currentMediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val boxWidth = maxWidth

        Column {
            Text(
                text = "Reminder Time ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .background(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedOption,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Arrow",
                        tint = Color.Black
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(boxWidth).background(Color.White)
            ) {
                options.forEach { (option, time) ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOption = option
                            onReminderTimeChange(time)
                            expanded = false
                        },
                        text = { Text(text = option, fontSize = 14.sp, color = Color(0xFF1E88E5)) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))



        // Lottie Animation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)

        ) {
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier
                    .size(400.dp)
                    .alpha(0.6f)
            )
        }

        // Sound Selection and Playback
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(top=120.dp)
        ) {
            Text(
                text = "Select a Reminder Sound 🎵",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            soundOptions.forEach { sound ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedSound = sound
                            playOrStopSound(context, sound, currentMediaPlayer) { newPlayer ->
                                currentMediaPlayer = newPlayer
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedSound == sound),
                        onClick = {
                            selectedSound = sound
                            playOrStopSound(context, sound, currentMediaPlayer) { newPlayer ->
                                currentMediaPlayer = newPlayer
                            }
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF1E88E5),
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(
                        text = sound,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

// Function to play or stop the sound
fun playOrStopSound(
    context: Context,
    soundName: String,
    currentMediaPlayer: MediaPlayer?,
    updateMediaPlayer: (MediaPlayer?) -> Unit
) {
    // Stop the current sound if it is playing
    currentMediaPlayer?.stop()
    currentMediaPlayer?.release()

    // Play the new sound
    val soundRes = when (soundName) {
        "Sound 1" -> R.raw.alarm
        "Sound 2" -> R.raw.alarm_sound
        "Sound 3" -> R.raw.hindi_taunt
        else -> R.raw.alarm
    }

    val mediaPlayer = MediaPlayer.create(context, soundRes)
    mediaPlayer.start()
    updateMediaPlayer(mediaPlayer)

    mediaPlayer.setOnCompletionListener {
        it.release()
        updateMediaPlayer(null)
    }
}

