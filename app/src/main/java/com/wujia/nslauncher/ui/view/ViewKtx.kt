package com.wujia.nslauncher.ui.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedSprite(
    targetState: SpriteState,
    width: Int,
    height: Int,
    bitmap: Bitmap
) {
    Box(
        modifier = Modifier
            .size(60.dp, 60.dp)
            .background(androidx.compose.ui.graphics.Color.Blue)
    ) {
        Image(
            bitmap = bitmap.extractSubregion(targetState, width, height),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Bitmap 扩展函数
fun Bitmap.extractSubregion(state: SpriteState, width: Int, height: Int): ImageBitmap {
    return Bitmap.createBitmap(
        this,
        0, 0, 90, 90
    ).asImageBitmap()
}

data class SpriteState(val row: Int, val column: Int)


@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(error: String?, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("加载失败", color = Color.Red)
            Text(error ?: "未知错误")
            Button(onClick = { onClick.invoke() }) {
                Text("重试")
            }
        }
    }
}

@Composable
fun DotIndicator(
    count: Int,
    selected: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(count) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (index < selected) Color.Black else Color.Gray,
                        shape = CircleShape
                    )
            )
        }
    }
}