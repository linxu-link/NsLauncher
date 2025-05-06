package com.wujia.nslauncher.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wujia.launcher.core.HiLauncher
import com.wujia.nslauncher.NsViewModelFactory
import com.wujia.nslauncher.ui.pager.allapp.AllAppScreen
import com.wujia.nslauncher.ui.pager.HomeScreen
import com.wujia.nslauncher.ui.pager.settings.SettingsScreen
import com.wujia.nslauncher.ui.pager.standby.StandbyScreen

private const val TAG = "NsLauncherNavGraph"

@Composable
fun NsLauncherNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NsDestinations.ROUTE_HOME,
) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination
    Log.d(TAG, "[NsLauncherNavGraph] current route:${currentRoute}")
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(route = NsDestinations.ROUTE_HOME) {
            HomeScreen(
                viewModel = viewModel(factory = NsViewModelFactory()),
                onAllAppClick = {
                    navController.naviAllAppScreen()
                },
                onSettingsClick = {
                    navController.naviSettingsScreen()
                }
            )
        }

        composable(route = NsDestinations.ROUTE_ALL_APP) {
            AllAppScreen(
                viewModel = viewModel(factory = NsViewModelFactory()),
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = NsDestinations.ROUTE_SETTINGS) {
            SettingsScreen(
                viewModel = viewModel(factory = NsViewModelFactory()),
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = NsDestinations.ROUTE_STANDBY) {
            StandbyScreen(navController)
        }

    }
}

