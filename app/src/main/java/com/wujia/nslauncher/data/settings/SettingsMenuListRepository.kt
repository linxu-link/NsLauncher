package com.wujia.nslauncher.data.settings

import com.wujia.nslauncher.arch.NsArchRepository

class SettingsMenuListRepository : NsArchRepository() {

    private val menuItems = listOf(
        "壁纸",
        "主题",
        "用户",
        "设备",
        "设置",
        "关于",
    )

    fun getMenuItems(): List<String> = menuItems

}