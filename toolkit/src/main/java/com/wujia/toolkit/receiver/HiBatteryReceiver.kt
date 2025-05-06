package com.wujia.toolkit.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import com.wujia.toolkit.HiAppGlobal

private const val TAG = "BatteryReceiver"

object HiBatteryReceiver {

    private val listeners = mutableListOf<OnBatteryChangeListener>()

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) ?: 0
            val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging =
                status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
            Log.i(TAG, "onReceive: level = $level, scale = $scale")
            // 更新电量
            val batteryLevel = level * 100 / scale.toFloat().toInt()

            notifyLevelChange(batteryLevel, isCharging)
        }
    }

    private fun notifyLevelChange(level: Int, isCharging: Boolean) {
        synchronized(listeners) {
            listeners.forEach {
                it.onBatteryChange(level, isCharging)
            }
        }
    }

    private fun checkBatteryStatus(listener: OnBatteryChangeListener) {
        val intent = HiAppGlobal.getApplication()
            .registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) ?: 0
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging =
            status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
        Log.i(TAG, "onReceive: level = $level, scale = $scale")
        // 更新电量
        val batteryLevel = level * 100 / scale.toFloat().toInt()
        listener.onBatteryChange(batteryLevel, isCharging)
    }

    fun register(listener: OnBatteryChangeListener) {
        synchronized(listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener)
            }
            if (listeners.size == 1) {
                // 注册广播接收器
                HiAppGlobal.getApplication().registerReceiver(
                    batteryReceiver,
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                )
            } else if (listeners.size > 1) {
                checkBatteryStatus(listener)
            } else {
                // do nothing
            }
        }
    }

    fun unregister(listener: OnBatteryChangeListener) {
        synchronized(listeners) {
            listeners.remove(listener)
            if (listeners.isEmpty()) {
                HiAppGlobal.getApplication().unregisterReceiver(batteryReceiver)
            }
        }
    }

    interface OnBatteryChangeListener {
        fun onBatteryChange(level: Int, isCharging: Boolean)
    }
}