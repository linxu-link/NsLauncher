package com.wujia.nslauncher.ui.pager.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.wujia.nslauncher.R
import com.wujia.nslauncher.entity.NsAppInfo
import com.wujia.nslauncher.ui.view.StatusBar

@Composable
fun GalleryScreen() {
    Column() {
        StatusBar(
            title = "相册",
            titleIcon = R.drawable.ic_all_app,
            showRightIcon = false
        )
    }

}

@Composable
private fun ImageGridView(apps: List<NsAppInfo> = emptyList(), onAppClick: (String) -> Unit = {}) {

}