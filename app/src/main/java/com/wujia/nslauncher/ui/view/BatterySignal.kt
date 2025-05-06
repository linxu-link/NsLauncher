package com.wujia.nslauncher.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wujia.nslauncher.R
import com.wujia.toolkit.receiver.HiBatteryReceiver

private const val TAG = "BatterySignal"

@Composable
@Preview
fun BatterySignal() {
    var batteryLevel by remember { mutableStateOf("100%") }
    var isInCharging by remember { mutableStateOf(false) } // 新增充电状态
    Row {

        val batteryIcon = if (isInCharging) {
            R.drawable.icon_battery_charging_full_24 // 充电状态图标
        } else {
            when (batteryLevel.dropLast(1).toInt()) {
                in 0..5 -> R.drawable.icon_battery_0_bar_24
                in 6..10 -> R.drawable.icon_battery_1_bar_24
                in 20..29 -> R.drawable.icon_battery_2_bar_24
                in 30..49 -> R.drawable.icon_battery_3_bar_24
                in 50..69 -> R.drawable.icon_battery_4_bar_24
                in 70..89 -> R.drawable.icon_battery_5_bar_24
                in 90..99 -> R.drawable.icon_battery_6_bar_24
                else -> R.drawable.icon_battery_full_bar_24
            }
        }

        Icon(
            painter = painterResource(batteryIcon),
            contentDescription = "电量",
            tint = if (isInCharging) Color.Green else Color.Black,
            modifier = Modifier
                .size(height = 26.dp, width = 34.dp)
                .rotate(90f)
                .align(Alignment.CenterVertically)
        )

        Text(
            text = buildAnnotatedString {
                val number = batteryLevel.dropLast(1)
                append(number)
                withStyle(SpanStyle(fontSize = 15.sp)) {
                    append("%")
                }
            },
            modifier = Modifier.align(Alignment.CenterVertically),
            color = Color.Black,
            fontSize = 18.sp
        )
    }

    // 动态电量监听
    DisposableEffect(Unit) {
        val onBatteryChangeListener = object : HiBatteryReceiver.OnBatteryChangeListener {
            override fun onBatteryChange(level: Int, isCharging: Boolean) {
                batteryLevel = "$level%"
                isInCharging = isCharging
            }
        }

        // 注册广播接收器
        HiBatteryReceiver.register(onBatteryChangeListener)

        // 组件销毁时注销
        onDispose {
            HiBatteryReceiver.unregister(onBatteryChangeListener)
        }
    }

}
