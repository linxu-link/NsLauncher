package com.wujia.nslauncher.ui.pager.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wujia.nslauncher.R
import com.wujia.nslauncher.ui.pager.settings.about.AboutScreen
import com.wujia.nslauncher.ui.pager.settings.account.AccountScreen
import com.wujia.nslauncher.ui.pager.settings.info.DeviceInfoScreen
import com.wujia.nslauncher.ui.pager.settings.settings.SettingPanel
import com.wujia.nslauncher.ui.pager.settings.theme.ThemeScreen
import com.wujia.nslauncher.ui.pager.settings.wallpaper.WallpaperScreen
import com.wujia.nslauncher.ui.view.BottomBar
import com.wujia.nslauncher.ui.view.ErrorView
import com.wujia.nslauncher.ui.view.LoadingView
import com.wujia.nslauncher.ui.view.StatusBar
import com.wujia.nslauncher.ui.view.StatusBarType

private const val TAG = "SettingsScreen"

annotation class SettingsPager() {
    companion object {
        const val WALLPAPER = 0
        const val THEME = 1
        const val ACCOUNT = 2
        const val DEVICES = 3
        const val SETTINGS = 4
        const val ABOUT = 5
    }
}

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onBack: () -> Unit) {
    val uiState by viewModel.uiState

    if (uiState.isLoading) {
        LoadingView()
    } else if (uiState.error != null) {
        ErrorView(uiState.error) {
            viewModel.sendIntent(SettingsIntent.LoadMenuList)
        }
    } else {
        SettingsContent(
            uiState = uiState,
            onMenuItemClick = { index->
                viewModel.sendIntent(SettingsIntent.ClickMenuItem(index))
            }
        )
    }

    // 初始加载
    LaunchedEffect(Unit) {
        viewModel.sendIntent(SettingsIntent.LoadMenuList)
    }
}


@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onMenuItemClick: ((Int) -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatusBar(
            modifier = Modifier
                .height(52.dp)
                .systemBarsPadding()
                .fillMaxWidth(),
            title = "设置",
            titleIcon = R.drawable.ic_settings,
            showRightIcon = false,
        )

        Row(
            modifier = Modifier.weight(1f)
        ) {
            MenuItemList(uiState.menuItems, onClick = { index ->
                onMenuItemClick?.invoke(index)
            })

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 16.dp)
                    .width(1.dp)
            )

            MenuPanel(uiState.selectedItemIndex)
        }

        BottomBar(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            types = intArrayOf(StatusBarType.RETURN, StatusBarType.START)
        )
    }
}


@Composable
private fun MenuItemList(menuItems: List<String>, onClick: (index: Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(menuItems) { index, item ->
            MenuItem(title = item, onClick = {
                onClick(index)
            })
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(1.dp)
            )
        }
    }
}

@Composable
private fun MenuItem(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null
        )
    }
}

@Composable
private fun MenuPanel(currentPagerIndex: Int) {
    when (currentPagerIndex) {
        SettingsPager.WALLPAPER -> {
            WallpaperScreen()
        }

        SettingsPager.THEME -> {
            ThemeScreen()
        }

        SettingsPager.DEVICES -> {
            DeviceInfoScreen()
        }

        SettingsPager.ACCOUNT -> {
            AccountScreen()
        }

        SettingsPager.SETTINGS -> {
            SettingPanel()
        }

        SettingsPager.ABOUT -> {
            AboutScreen()
        }
    }
}




