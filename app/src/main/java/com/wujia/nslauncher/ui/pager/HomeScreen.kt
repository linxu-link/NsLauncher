package com.wujia.nslauncher.ui.pager

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.wujia.nslauncher.R
import com.wujia.nslauncher.entity.NsAppInfo
import com.wujia.nslauncher.ui.view.BottomBar
import com.wujia.nslauncher.ui.view.ErrorView
import com.wujia.nslauncher.ui.view.InteractiveImage
import com.wujia.nslauncher.ui.view.LoadingView
import com.wujia.nslauncher.ui.view.StatusBarType
import com.wujia.nslauncher.ui.view.SysStatusBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAllAppClick: (() -> Unit)? = null,
    onNewsClick: (() -> Unit)? = null,
    onShopClick: (() -> Unit)? = null,
    onGalleryClick: (() -> Unit)? = null,
    onBtClick: (() -> Unit)? = null,
    onSettingsClick: (() -> Unit)? = null,
    onStandbyClick: (() -> Unit)? = null,
) {

    val uiState by viewModel.uiState

    if (uiState.isLoading) {
        LoadingView()
    } else if (uiState.error != null) {
        ErrorView(uiState.error) {
            viewModel.handleIntent(HomeIntent.LoadRecentlyApps())
        }
    } else {
        HomeContent(
            recentlyApps = uiState.recentlyAppItems,
            onAppSelected = { pkg ->
                viewModel.handleIntent(HomeIntent.SelectedApp(pkg))
            },
            onAppClick = { pkg ->
                viewModel.handleIntent(HomeIntent.ClickApp(pkg))
            },
            onAllAppClick = { onAllAppClick?.invoke() },
            onSettingsClick = { onSettingsClick?.invoke() }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(HomeIntent.LoadRecentlyApps())
    }
}

@Composable
private fun HomeContent(
    recentlyApps: List<HomeItemUiState>,
    onAppSelected: ((String) -> Unit)? = null,
    onAppClick: (String) -> Unit,
    onAllAppClick: (() -> Unit)? = null,
    onNewsClick: (() -> Unit)? = null,
    onShopClick: (() -> Unit)? = null,
    onGalleryClick: (() -> Unit)? = null,
    onBtClick: (() -> Unit)? = null,
    onSettingsClick: (() -> Unit)? = null,
    onStandbyClick: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        SysStatusBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(52.dp)
        )

        HorizontalAppList(
            modifier = Modifier.weight(1f),
            appList = recentlyApps,
            iconSize = 160.dp,
            onAppSelectedApp = { pkg ->
                onAppSelected?.invoke(pkg)
            },
            onAppClick = { pkg ->
                onAppClick.invoke(pkg)
            },
            onAllAppClick = {
                onAllAppClick?.invoke()
            },
            showAddIcon = true
        )

        QuickAppList(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(80.dp),
            onNewsClick = onNewsClick,
            onShopClick = onShopClick,
            onGalleryClick = onGalleryClick,
            onBtClick = onBtClick,
            onSettingsClick = onSettingsClick,
            onStandbyClick = onStandbyClick,
        )

        BottomBar(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            types = intArrayOf(StatusBarType.SELECTION, StatusBarType.START)
        )
    }
}

@Composable
private fun HorizontalAppList(
    modifier: Modifier,
    iconSize: Dp,
    appList: List<HomeItemUiState>,
    onAppSelectedApp: ((String) -> Unit)? = null,
    onAppClick: (String) -> Unit,
    onAllAppClick: (() -> Unit)? = null,
    showAddIcon: Boolean = false
) {
    val context = LocalContext.current
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        itemsIndexed(items = appList) { index, itemUiState ->
            AppItem(
                state = itemUiState,
                modifier = Modifier.size(iconSize),
                onSelected = {
                    onAppSelectedApp?.invoke(it.packageName)
                },
                onClick = {
                    onAppClick.invoke(it.packageName)
                })

        }

        item {
            Spacer(modifier = Modifier.width(30.dp))
        }

        if (showAddIcon) {
            item {
                AddButton(
                    modifier = Modifier
                        .size(100.dp)
                        .offset(y = 10.dp)
                ) {
                    onAllAppClick?.invoke()
                }
            }
        }
        item {
            Spacer(modifier = Modifier.width(40.dp))
        }
    }
}

@Composable
private fun AppItem(
    state: HomeItemUiState,
    modifier: Modifier = Modifier,
    onSelected: ((NsAppInfo) -> Unit)? = null,
    onClick: ((NsAppInfo) -> Unit)? = null
) {
    var appIcon by remember { mutableStateOf<ImageBitmap?>(null) }
    var loading by remember { mutableStateOf(true) }

    val currentContext = LocalContext.current

    LaunchedEffect(state) {
        loading = true
        appIcon = withContext(Dispatchers.IO) {
            try {
                val targetContext = currentContext.createPackageContext(
                    state.appInfo.packageName,
                    Context.CONTEXT_IGNORE_SECURITY
                )
                ResourcesCompat.getDrawable(
                    targetContext.resources,
                    state.appInfo.iconResource,
                    null
                )?.toBitmap()?.asImageBitmap()
            } catch (e: Exception) {
                null
            }
        }
        loading = false
    }

    appIcon?.let { icon ->
        InteractiveImage(
            modifier,
            icon,
            state.appInfo.appName,
            showBounds = state.selected,
            onSelected = { onSelected?.invoke(state.appInfo) },
            onClick = { onClick?.invoke(state.appInfo) }
        )
    }
}

@Composable
private fun AddButton(modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.White)
            .clickable(onClick = onClick)
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .size(45.dp),
            painter = painterResource(R.drawable.ic_all_app),
            contentDescription = "添加应用",
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun QuickAppList(
    modifier: Modifier = Modifier,
    onNewsClick: (() -> Unit)? = null,
    onShopClick: (() -> Unit)? = null,
    onGalleryClick: (() -> Unit)? = null,
    onBtClick: (() -> Unit)? = null,
    onSettingsClick: (() -> Unit)? = null,
    onStandbyClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        QuickAppImage(
            resId = R.drawable.news,
            onClick = {
                onNewsClick?.invoke()
            }
        )

        QuickAppImage(
            resId = R.drawable.shop,
            onClick = {
                onShopClick?.invoke()
            }
        )

        QuickAppImage(R.drawable.pic) {
            onGalleryClick?.invoke()
        }

        QuickAppImage(R.drawable.bt) {
            onBtClick?.invoke()
        }

        QuickAppImage(R.drawable.ic_settings) {
            onSettingsClick?.invoke()
        }

        QuickAppImage(R.drawable.lock) {
            onStandbyClick?.invoke()
        }
    }
}

@Composable
private fun QuickAppImage(@DrawableRes resId: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = resId),
        contentDescription = null, // 添加内容描述
        modifier = Modifier
            .size(40.dp)
            .clickable(onClick = onClick)
    )
}
