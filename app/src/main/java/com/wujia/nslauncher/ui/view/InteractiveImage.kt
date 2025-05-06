package com.wujia.nslauncher.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InteractiveImage(
    modifier: Modifier,
    icon: ImageBitmap,
    iconName: String,
    showBounds: Boolean = false,
    onSelected: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier,
    ) {
        // 图片名称文字（仅在第一次点击后显示）
        if (showBounds) {
            Text(
                text = iconName,
                color = Color.Cyan,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(135.dp)
                .align(Alignment.BottomCenter)
                .clickable {
                    if (!showBounds) {
                        onSelected?.invoke()
                    } else {
                        onClick?.invoke()
                    }
                }
                .then(
                    if (showBounds) {
                        modifier.border(2.dp, Color.Cyan)
                    } else {
                        modifier
                    }
                )
        ) {
            // 图片内容

            Image(
                bitmap = icon,
                contentDescription = iconName,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }

    }
}
