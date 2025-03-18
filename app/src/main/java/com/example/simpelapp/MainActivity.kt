package com.example.stopwatchapp

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(10)
            timeElapsed = SystemClock.elapsedRealtime() - startTime
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Stopwatch",
            fontSize = 32.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))


        Text(
            text = formatTime(timeElapsed),
            fontSize = 50.sp,
            color = Color(0xFF2196F3)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (isRunning) {
                    isRunning = false
                    history.add(formatTime(timeElapsed)) // Simpan ke riwayat
                } else {
                    startTime = SystemClock.elapsedRealtime() - timeElapsed
                    isRunning = true
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunning) Color.Red else Color.Green
            ),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(
                text = if (isRunning) "Pause" else "Start",
                fontSize = 20.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))


        Button(
            onClick = {
                isRunning = false
                timeElapsed = 0L
                startTime = 0L
                history.clear()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(
                text = "Reset",
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))


        if (history.isNotEmpty()) {
            Text(text = "Riwayat Waktu:", fontSize = 20.sp, color = Color.Black)

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                items(history) { time ->
                    Text(text = time, fontSize = 18.sp, color = Color.DarkGray)
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
