package com.wujia.nslauncher.ui.pager.allapp

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wujia.nslauncher.R
import com.wujia.nslauncher.entity.NsAppInfo
import com.wujia.nslauncher.ui.view.BottomBar
import com.wujia.nslauncher.ui.view.ErrorView
import com.wujia.nslauncher.ui.view.LoadingView
import com.wujia.nslauncher.ui.view.StatusBar
import com.wujia.nslauncher.ui.view.StatusBarType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "AllAppScreen"

@Composable
fun AllAppScreen(viewModel: AllAppViewModel, onBackClick: (() -> Unit)? = null) {
    val uiState by viewModel.uiState

    if (uiState.isLoading) {
        LoadingView()
    } else if (uiState.error != null) {
        ErrorView(uiState.error) {
            viewModel.handleIntent(AllAppIntent.LoadAllApps())
        }
    } else {
        AllAppContent(
            apps = uiState.apps,
            onAppClick = { pkgName ->

            },
            onBackClick = { onBackClick?.invoke() }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(AllAppIntent.LoadAllApps())
    }
}

@Composable
private fun AllAppContent(
    apps: List<AllAppItemUiState>,
    onAppClick: (String) -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    Column(
        Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        StatusBar(
            modifier = Modifier
                .height(52.dp)
                .systemBarsPadding()
                .fillMaxWidth(),
            title = "所有软件",
            titleIcon = R.drawable.ic_all_app,
            showRightIcon = true,
            types = intArrayOf(StatusBarType.GROUP, StatusBarType.ORDER)
        )

        AppGridView(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            apps = apps,
            onAppClick = onAppClick
        )

        BottomBar(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            types = intArrayOf(StatusBarType.SELECTION, StatusBarType.RETURN, StatusBarType.START),
            onBClick = {
                onBackClick?.invoke()
            }
        )
    }
}

@Composable
private fun AppGridView(
    modifier: Modifier,
    apps: List<AllAppItemUiState>,
    onAppClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        contentPadding = PaddingValues(4.dp),
        modifier = modifier
    ) {

        itemsIndexed(
            items = apps
        ) { index, appItemUiState ->
            AppGridItem(
                app = appItemUiState.appInfo,
                onAppClick = {

                },
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
private fun AppGridItem(
    app: NsAppInfo,
    onAppClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var iconBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val context = LocalContext.current

    LaunchedEffect(app) {
        withContext(Dispatchers.IO) {
            try {
                val targetContext = context.createPackageContext(
                    app.packageName,
                    Context.CONTEXT_IGNORE_SECURITY
                )
                ResourcesCompat.getDrawable(
                    targetContext.resources,
                    app.iconResource,
                    targetContext.theme
                )?.let { drawable ->
                    iconBitmap = drawable.toBitmap().asImageBitmap()
                }
            } catch (e: Exception) {
                Log.e("AppGridItem", "Error loading icon", e)
            }
            isLoading = false
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onAppClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(110.dp)
        ) {
            when {
//                isLoading -> CircularProgressIndicator(Modifier.size(24.dp))
                iconBitmap != null -> Image(
                    bitmap = iconBitmap!!,
                    contentDescription = app.appName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                else -> Icon(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "图标加载失败"
                )
            }
        }
    }
}


