package com.wujia.nslauncher.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wujia.nslauncher.R
import com.wujia.toolkit.receiver.HiConnectivityReceiver
import com.wujia.toolkit.receiver.HiWifiReceiver

@Preview
@Composable
fun FourGSignal() {
    var fourGSignalLevel by remember { mutableIntStateOf(0) }

    val wifiIcon = when (fourGSignalLevel) {
        1, 2 -> R.drawable.icon_signal_cellular_alt_1_bar_24
        3 -> R.drawable.icon_signal_cellular_alt_2_bar_24
        4 -> R.drawable.icon_signal_cellular_alt_3_bar_24
        else -> R.drawable.icon_signal_cellular_no_sim_24
    }

    Row {
        Icon(
            painter = painterResource(wifiIcon),
            contentDescription = "WiFi信号",
            tint = Color.Black,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterVertically)
        )
    }

    // 动态wifi信号强度监听
    DisposableEffect(Unit) {
        val signalChangedListener = object : HiConnectivityReceiver.OnSignalChangedListener {

            override fun onSignalChanged(signalLevel: Int) {
                fourGSignalLevel = signalLevel
            }
        }

        HiConnectivityReceiver.register(signalChangedListener)

        // 组件销毁时注销
        onDispose {
            HiConnectivityReceiver.unregister(signalChangedListener)
        }
    }

}