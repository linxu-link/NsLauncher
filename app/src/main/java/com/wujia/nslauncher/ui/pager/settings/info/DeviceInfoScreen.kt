package com.wujia.nslauncher.ui.pager.settings.info

import android.app.ActivityManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.text.format.Formatter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

@Composable
fun DeviceInfoScreen() {
    val context = LocalContext.current
    val activityManager = context.getSystemService(ActivityManager::class.java)
    val memoryInfo = remember { ActivityManager.MemoryInfo().apply { activityManager.getMemoryInfo(this) } }
    val storageInfo = remember { getStorageInfo() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 系统基本信息
        InfoCard(title = "系统信息") {
            InfoItem("Android版本", "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
            InfoItem("系统版本", "${Build.ID} (${Build.DISPLAY})")
        }

        // 硬件信息
        InfoCard(title = "硬件信息") {
            InfoItem("设备制造商", Build.MANUFACTURER.uppercase(Locale.getDefault()))
            InfoItem("设备型号", Build.MODEL)
            InfoItem("SOC制造商", Build.HARDWARE)
            InfoItem("SOC核心数", "${Runtime.getRuntime().availableProcessors()} 核")
            InfoItem("SOC型号", getCpuModel())
        }

        // 内存信息
        InfoCard(title = "内存状态") {
            val totalMem = Formatter.formatFileSize(context, memoryInfo.totalMem)
            val availMem = Formatter.formatFileSize(context, memoryInfo.availMem)
            val progress = memoryInfo.availMem.toFloat() / memoryInfo.totalMem.toFloat()

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Spacer(Modifier.height(8.dp))
            InfoItem("可用", availMem)
            InfoItem("总共", totalMem)
        }

        // 存储信息（新增部分）
        InfoCard(title = "存储状态") {
            val totalStorage = Formatter.formatFileSize(context, storageInfo.totalBytes)
            val freeStorage = Formatter.formatFileSize(context, storageInfo.freeBytes)
            val usedProgress = 1 - (storageInfo.freeBytes.toFloat() / storageInfo.totalBytes.toFloat())

            LinearProgressIndicator(
                progress = usedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Spacer(Modifier.height(8.dp))
            InfoItem("空闲空间", freeStorage)
            InfoItem("总空间", totalStorage)
        }
    }
}

// 存储信息数据类
private data class StorageInfo(
    val totalBytes: Long,
    val freeBytes: Long
)

// 获取存储信息
private fun getStorageInfo(): StorageInfo {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)

    val blockSize = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        stat.blockSizeLong
    } else {
        stat.blockSize.toLong()
    }

    val totalBlocks = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        stat.blockCountLong
    } else {
        stat.blockCount.toLong()
    }

    val availableBlocks = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        stat.availableBlocksLong
    } else {
        stat.availableBlocks.toLong()
    }

    return StorageInfo(
        totalBytes = blockSize * totalBlocks,
        freeBytes = blockSize * availableBlocks
    )
}

@Composable
private fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun InfoItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// 新增获取CPU型号的函数
private fun getCpuModel(): String {
    return try {
        val process = Runtime.getRuntime().exec("cat /proc/cpuinfo")
        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                when {
                    line?.startsWith("Hardware") == true -> {
                        return line?.substringAfter(":")?.trim() ?: "Unknown"
                    }
                    line?.startsWith("model name") == true -> {
                        return line?.substringAfter(":")?.trim() ?: "Unknown"
                    }
                }
            }
        }
        process.waitFor()
        Build.HARDWARE // 回退方案
    } catch (e: Exception) {
        e.printStackTrace()
        Build.HARDWARE // 异常时返回硬件信息
    }
}
