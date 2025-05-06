package com.wujia.nslauncher.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wujia.nslauncher.R

@Composable
@Preview
fun SysStatusBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 左侧头像
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "用户头像",
            modifier = Modifier
                .padding(start = 25.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .size(35.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        // 右侧状态信息
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(end = 25.dp)
        ) {
            LiveClock()

//            FourGSignal()

            WiFISignal()

            BatterySignal()
        }
    }
}




