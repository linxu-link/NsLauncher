package com.wujia.nslauncher.ui

import androidx.navigation.NavController

/**
 * Destinations used in the [NsLauncherActivity]
 */
object NsDestinations {
    const val ROUTE_HOME = NsScreens.SCREEN_HOME
    const val ROUTE_SETTINGS = NsScreens.SCREEN_SETTINGS
    const val ROUTE_ALL_APP = NsScreens.SCREEN_ALL_APP
    const val ROUTE_GALLERY = NsScreens.SCREEN_GALLERY
    const val ROUTE_STANDBY = NsScreens.SCREEN_STANDBY
}

/**
 * Screens used in [NsDestinations]
 */
private object NsScreens {
    const val SCREEN_HOME = "home"
    const val SCREEN_SETTINGS = "settings"
    const val SCREEN_ALL_APP = "allApp"
    const val SCREEN_GALLERY = "gallery"
    const val SCREEN_STANDBY = "standby"
}

fun NavController.naviSettingsScreen() {
    this.navigate(NsDestinations.ROUTE_SETTINGS)
}

fun NavController.naviAllAppScreen() {
    this.navigate(NsDestinations.ROUTE_ALL_APP)
}