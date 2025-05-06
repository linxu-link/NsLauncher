package com.wujia.launcher.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.wujia.launcher.core.interfaces.InstalledAppChangeListener
import com.wujia.toolkit.HiAppGlobal

private const val TAG = "LauncherReceiver"

internal class LauncherReceiver private constructor() {

    private val listeners = mutableListOf<InstalledAppChangeListener>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onInstalledAppChanged()
        }
    }

    init {
        // 注册广播
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_PACKAGE_ADDED)
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        filter.addDataScheme("package")
        HiAppGlobal.getApplication().registerReceiver(receiver, filter)
    }

    companion object {

        internal val instance by lazy {
            synchronized(LauncherReceiver::class.java) {
                LauncherReceiver()
            }
        }

    }

    // 注册广播
    internal fun register(listener: InstalledAppChangeListener) {
        synchronized(listeners) {
            if (listeners.contains(listener)) {
                return
            }
            listeners.add(listener)
        }
    }

    internal fun unregister(listener: InstalledAppChangeListener) {
        synchronized(listeners) {
            listeners.remove(listener)
        }
    }

    private fun onInstalledAppChanged() {
        synchronized(listeners) {
            listeners.forEach {
                it.onInstalledAppChanged()
            }
        }
    }

}