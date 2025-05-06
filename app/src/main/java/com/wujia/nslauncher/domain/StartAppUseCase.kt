package com.wujia.nslauncher.domain

import com.wujia.launcher.core.HiLauncher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class StartAppUseCase(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(pkg: String) {
        HiLauncher.startApp(pkg)
    }
}