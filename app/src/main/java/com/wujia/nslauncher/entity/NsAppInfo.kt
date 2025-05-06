package com.wujia.nslauncher.entity

data class NsAppInfo(
    val appName: String,
    val packageName: String,
    val iconResource: Int,
    val launcherActivity: String?
) {
    override fun toString(): String {
        return "NsAppInfo(appName='$appName', packageName='$packageName', launcherActivity='$launcherActivity')"
    }
}