package com.example.simpelapp

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopwatchScreen()
        }
    }
}

@Composable
fun StopwatchScreen() {
    var timeElapsed by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    val history = remember { mutableStateListOf<String>() }
    val lapTimes = remember { mutableStateListOf<String>() }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(10)
            timeElapsed = SystemClock.elapsedRealtime() - startTime
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2196F3), Color(0xFF64B5F6))
                )
            )
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Stopwatch",
            fontSize = 30.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .shadow(10.dp)
                .padding(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Text(
                text = formatTime(timeElapsed),
                fontSize = 50.sp,
                color = Color(0xFF2196F3),
                modifier = Modifier.padding(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (isRunning) {
                        isRunning = false
                        history.add(formatTime(timeElapsed))
                    } else {
                        startTime = SystemClock.elapsedRealtime() - timeElapsed
                        isRunning = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color.Red else Color.Green
                ),
                modifier = Modifier
                    .weight(1f)
                    .shadow(5.dp)
            ) {
                Text(
                    text = if (isRunning) "Pause" else "Start",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            Button(
                onClick = {
                    isRunning = false
                    timeElapsed = 0L
                    startTime = 0L
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier
                    .weight(1f)
                    .shadow(5.dp)
            ) {
                Text(
                    text = "Reset",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (isRunning) {
                    lapTimes.add(formatTime(timeElapsed))
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
            modifier = Modifier.shadow(5.dp)
        ) {
            Text(text = "Lap", fontSize = 18.sp, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (lapTimes.isNotEmpty()) {
            Text(text = "Lap Times:", fontSize = 20.sp, color = Color.White)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                items(lapTimes) { lap ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .shadow(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Text(
                                text = lap,
                                fontSize = 18.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Button(
                            onClick = { lapTimes.remove(lap) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier.padding(start = 5.dp)
                        ) {
                            Text(text = "X", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

fun formatTime(timeMillis: Long): String {
    val minutes = (timeMillis / 60000) % 60
    val seconds = (timeMillis / 1000) % 60
    val millis = (timeMillis % 1000) / 10
    return String.format("%02d:%02d:%02d", minutes, seconds, millis)
}

@Preview(showBackground = true)
@Composable
fun StopwatchPreview() {
    StopwatchScreen()
}