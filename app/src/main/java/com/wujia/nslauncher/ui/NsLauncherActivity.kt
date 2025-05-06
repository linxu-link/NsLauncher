package com.wujia.nslauncher.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.wujia.nslauncher.arch.NsArchActivity
import com.wujia.nslauncher.ui.theme.NsLauncherTheme


class NsLauncherActivity : NsArchActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            NsLauncherTheme {
                NsLauncherNavGraph()
            }
        }
    }

}
