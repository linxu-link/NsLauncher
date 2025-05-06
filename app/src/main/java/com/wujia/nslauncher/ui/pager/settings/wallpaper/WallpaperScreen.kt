package com.wujia.nslauncher.ui.pager.settings.wallpaper

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wujia.nslauncher.R
import java.io.IOException

// 需要添加权限：
// <uses-permission android:name="android.permission.SET_WALLPAPER" />

@Preview
@Composable
fun WallpaperScreen() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // 权限请求处理
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showDialog = true
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(enabled = true, state = rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        WallpaperItem(resId = R.drawable.bg_night, title = "静谧之夜") {
            permissionLauncher.launch(Manifest.permission.SET_WALLPAPER)
        }

        WallpaperItem(resId = R.drawable.bg_snow, title = "雪山之巅") {
            permissionLauncher.launch(Manifest.permission.SET_WALLPAPER)
        }

        WallpaperItem(resId = R.drawable.bg_luna, title = "月球之旅") {
            permissionLauncher.launch(Manifest.permission.SET_WALLPAPER)
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(1.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("选择壁纸类型") },
            text = {
                Column {
                    Button(onClick = {
                        setWallpaper(context, WallpaperManager.FLAG_SYSTEM)
                        showDialog = false
                    }) {
                        Text("设置主屏幕壁纸")
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        setWallpaper(context, WallpaperManager.FLAG_LOCK)
                        showDialog = false
                    }) {
                        Text("设置锁屏壁纸")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
fun WallpaperItem(@DrawableRes resId: Int = -1, title: String = "", onClick: (() -> Unit)? = null) {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(1.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 10.dp)
            .clickable {
                onClick?.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RectangleShape
                )
                .background(if (resId == -1) Color.Cyan else Color.Transparent)
        ) {
            if (resId != -1) {
                Image(
                    painter = painterResource(resId),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        }


        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = title,
            style = MaterialTheme.typography.titleSmall
        )

    }
}

private fun setWallpaper(
    context: Context,
    which: Int = WallpaperManager.FLAG_SYSTEM
) {
    try {
        val wallpaperManager = WallpaperManager.getInstance(context)
        // 示例使用应用内置资源
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bg_snow)

        wallpaperManager.setBitmap(
            bitmap,
            null,
            true, // 是否显示预览
            which
        )
        Toast.makeText(context, "壁纸设置成功", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        Toast.makeText(context, "设置失败: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
