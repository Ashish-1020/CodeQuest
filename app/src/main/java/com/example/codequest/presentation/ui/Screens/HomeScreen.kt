package com.example.codequest.presentation.ui.Screens



import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.codequest.broadcastReceiver.AlarmReceiver

import com.example.codequest.R

import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.codequest.data.Contest
import com.example.codequest.presentation.ui.ContestViewModel
import com.example.codequest.room.data.ContestReminder
import dagger.hilt.android.AndroidEntryPoint

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contestViewModel: ContestViewModel by viewModels()
        setContent {
            val context = LocalContext.current
            val isConnected = remember { mutableStateOf(checkInternetConnection(context)) }
            val darkTheme = isSystemInDarkTheme() // Detect system mode

            MaterialTheme(
                colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
            ) {
                if (isConnected.value) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "contest_screen") {
                        composable("contest_screen") {
                            ContestScreen(navController, contestViewModel)
                        }
                        composable(
                            "set_reminder_screen/{id}/{contestName}/{contestPlatform}/{startTime}",
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id")
                            val contestName = backStackEntry.arguments?.getString("contestName")
                            val contestPlatform = backStackEntry.arguments?.getString("contestPlatform")
                            val startTime = backStackEntry.arguments?.getString("startTime")
                            if (id != null && contestName != null && contestPlatform != null && startTime != null) {
                                SetReminderScreen(
                                    id = id,
                                    contestName = contestName,
                                    contestPlatform = contestPlatform,
                                    startTime = startTime,
                                    contestViewModel
                                )
                            }
                        }
                    }
                } else {
                    NoInternetScreen()
                }
            }
        }
    }
}
@SuppressLint("MissingPermission")
fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
@Composable
fun NoInternetScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_no_internet))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(200.dp)) {
            LottieAnimation(composition = composition, progress = progress)
        }
        Text(
            text = "Please Connect Your Device To Internet!!",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ContestScreen(navController: NavController, contestViewModel: ContestViewModel) {
    val options = listOf("LeetCode", "Codeforces", "CodeChef", "AtCoder", "GeeksforGeeks")
    val selectedOptions = remember { mutableStateListOf<String>() }
    val contests by contestViewModel.contests.observeAsState(emptyList())
    var expanded by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    var filteredContests by remember { mutableStateOf(contests) }
    val storedContests: List<ContestReminder> by contestViewModel.contestsStored.observeAsState(initial = emptyList())

    fun updateFilteredContests() {
        val filteredByPlatform = if (selectedOptions.isEmpty()) {
            contests.filter { contest -> options.map { it.lowercase() }
                .contains(contest.host.substringBefore(".").lowercase()) }
        } else {
            contests.filter { contest -> selectedOptions.map { it.lowercase() }
                .contains(contest.host.substringBefore(".").lowercase()) }
        }

        filteredContests = when (selectedTabIndex) {
            1 -> filteredByPlatform.filter { getContestStatus(it) == "Upcoming" }
            2 -> filteredByPlatform.filter { getContestStatus(it) == "Active" }
            3 -> filteredByPlatform.filter { getContestStatus(it) == "Completed" }
            else -> filteredByPlatform
        }
    }

    LaunchedEffect(contests, selectedOptions, selectedTabIndex) {
        updateFilteredContests()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Contests",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box(modifier = Modifier.wrapContentSize()) {
                Row {

                    Column {
                        Icon(
                            painter = painterResource(R.drawable.filter),
                            contentDescription = "Filter",
                            modifier = Modifier.clickable { expanded = true }.size(28.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        if (selectedOptions.contains(option)) {
                                            selectedOptions.remove(option)
                                        } else {
                                            selectedOptions.add(option)
                                        }
                                        updateFilteredContests()
                                    },
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = selectedOptions.contains(option),
                                                onCheckedChange = null,
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = MaterialTheme.colorScheme.primary,
                                                    uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(option, color = MaterialTheme.colorScheme.onSurface)
                                        }
                                    }
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        updateFilteredContests()
                                        expanded = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text(text = "Apply", color = MaterialTheme.colorScheme.onPrimary)
                                }
                                Button(
                                    onClick = {
                                        selectedOptions.clear()
                                        updateFilteredContests()
                                        expanded = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                ) {
                                    Text(text = "Cancel", color = MaterialTheme.colorScheme.onSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }

        RoundedBorderTabs(selectedTabIndex) { newIndex ->
            selectedTabIndex = newIndex
            updateFilteredContests()
        }



        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(filteredContests) { contest ->
                val contestStatus = getContestStatus(contest)

                ContestItem(
                    title = contest.event,
                    contestId = contest.id,
                    status = contestStatus,
                    timeRemaining = getTimeRemaining(contest),
                    url = contest.href,
                    platform = contest.host.substringBefore("."),
                    onSetReminderClick = {
                        navController.navigate("set_reminder_screen/${contest.id}/${contest.host.substringBefore(".")}/${contest.event}/${contest.start}")
                    },
                    storedContests = storedContests
                )
            }
        }
    }
}



@Composable
fun RoundedBorderTabs(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("All", "Upcoming", "Active", "Past")

    Row(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth(),
      //  horizontalArrangement = Arrangement.
    ) {
        tabs.forEachIndexed { index, title ->
            Surface(
                shape = RoundedCornerShape(40),
                color = if (selectedTabIndex == index) Color(0xFF1E88E5).copy(0.7f) else Color.LightGray.copy(0.6f),
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { onTabSelected(index) }
            ) {
                Text(
                    text = title,
                    color = if (selectedTabIndex == index) Color.White else Color.Black,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

fun getContestStatus(contest: Contest): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    val startMillis = dateFormat.parse(contest.start)?.time ?: 0L
    val endMillis = dateFormat.parse(contest.end)?.time ?: 0L
    val currentMillis = System.currentTimeMillis()
    val remainingTime = startMillis - currentMillis

    return when {
        remainingTime > 0 -> "Upcoming"
        currentMillis in startMillis..endMillis -> "Active"
        else -> "Completed"
    }
}

fun getTimeRemaining(contest: Contest): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    val startMillis = dateFormat.parse(contest.start)?.time ?: 0L
    val endMillis = dateFormat.parse(contest.end)?.time ?: 0L
    val currentMillis = System.currentTimeMillis()
    val remainingTime = startMillis - currentMillis
    val hours = TimeUnit.MILLISECONDS.toHours(remainingTime)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime) % 60

    return when {
        remainingTime < 0 -> "Completed on ${contest.end.substringBefore("T")}"
        remainingTime > 0 -> if (hours > 24) "Time left: ${hours / 24} days ${hours % 24} hrs $minutes mins" else "Time left: $hours hrs $minutes mins"
        else -> "Finished after $hours hrs $minutes mins"
    }
}
@Composable
fun ContestItem(
    title: String,
    contestId: Int,
    status: String,
    timeRemaining: String,
    url: String,
    platform: String,
    onSetReminderClick: () -> Unit,
    storedContests: List<ContestReminder>
) {
    val context = LocalContext.current
    val isContestStored = storedContests.any { it.id == contestId }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (status) {
                "Active" -> Color.Green
                "Upcoming" -> Color(0xFF1E88E5)
                "Completed" -> Color.Gray
                else -> Color.Black
            }
        )
    ) {
        Card(
            modifier = Modifier
                .padding(start = 4.dp)
                .fillMaxWidth()
                .clickable { },
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = when (status) {
                                    "Active" -> Color.Green.copy(alpha = 0.1f)
                                    "Upcoming" -> Color(0xFF1E88E5).copy(alpha = 0.1f)
                                    "Completed" -> Color.Gray.copy(alpha = 0.1f)
                                    else -> Color.Black.copy(alpha = 0.2f)
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = status,
                            color = when (status) {
                                "Active" -> Color.Green
                                "Upcoming" -> Color(0xFF1E88E5)
                                "Completed" -> Color.Gray
                                else -> Color.Black
                            },
                            fontSize = 14.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val platformIcon = when (platform) {
                            "leetcode" -> R.drawable.leetcode
                            "geeksforgeeks" -> R.drawable.gfg
                            "codeforces" -> R.drawable.codeforces
                            "codechef" -> R.drawable.codechef
                            "atcoder" -> R.drawable.atcoder
                            else -> null
                        }

                        platformIcon?.let {
                            Image(
                                painter = painterResource(id = it),
                                contentDescription = platform,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 4.dp)
                            )
                        }
                        Text(text = platform, color = MaterialTheme.colorScheme.onBackground)
                    }
                }

                Text(
                    text = title,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = timeRemaining,  color = MaterialTheme.colorScheme.onBackground , fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "View Details",
                        color = Color(0xFF1E88E5),
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )

                    if (status == "Upcoming") {
                        if (isContestStored) {
                            Text(
                                text = "Alarm is Set",
                                color = Color.Green,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }else{
                        Text(
                            text = "Set Reminder",
                            color = Color(0xFF1E88E5),
                            modifier = Modifier.clickable { onSetReminderClick() }
                        )
                    }}
                }
            }
        }
    }
}


@SuppressLint("ScheduleExactAlarm")
fun scheduleAlarm(context: Context, triggerTimeInMillis: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent)
}



