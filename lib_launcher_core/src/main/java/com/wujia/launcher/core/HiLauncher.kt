package com.wujia.launcher.core

import com.wujia.launcher.core.entity.AppInfo
import com.wujia.launcher.core.interfaces.InstalledAppChangeListener
import com.wujia.launcher.core.manager.AppManager
import com.wujia.launcher.core.receiver.LauncherReceiver

class HiLauncher {

    companion object {

        // 获取当前系统安装的所有可以在桌面上显示的应用
        fun getInstalledApps(): List<AppInfo> = AppManager.getInstalledApps()

        // 获取最近使用的应用列表
        fun getRecentlyUsedApps(): List<String> = AppManager.getRecentlyUsedApps()

        // 启动应用
        fun startApp(packageName: String) {
            AppManager.startApp(packageName)
        }

        // 杀死应用
        fun killApp(packageName: String) {
            AppManager.killApp(packageName)
        }

        // 获取应用信息
        fun getAppInfo(packageName: String): AppInfo? = AppManager.getAppInfo(packageName)

        // 注册应用安装监听
        fun registerAppInstallListener(listener: InstalledAppChangeListener) {
            LauncherReceiver.instance.register(listener)
        }

        // 取消注册应用安装监听
        fun unregisterAppInstallListener(listener: InstalledAppChangeListener) {
            LauncherReceiver.instance.unregister(listener)
        }

    }

}