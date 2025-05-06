package com.wujia.nslauncher.ui.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun LiveClock(
    formatPattern: String = "HH:mm",
    zoneId: ZoneId = ZoneId.systemDefault()
) {
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000 - System.currentTimeMillis() % 1000) // 对齐秒
            currentTime = LocalDateTime.now(zoneId)
        }
    }

    Text(
        text = DateTimeFormatter.ofPattern(formatPattern).format(currentTime),
        fontSize = 18.sp,
        style = MaterialTheme.typography.titleMedium
    )
}