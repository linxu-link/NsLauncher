package com.wujia.nslauncher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wujia.nslauncher.ui.pager.HomeViewModel
import com.wujia.nslauncher.ui.pager.allapp.AllAppViewModel
import com.wujia.nslauncher.ui.pager.settings.SettingsViewModel

class NsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    getRecentlyAppListUseCase = ClassLocator.provideRecentlyAppListUseCase(),
                    startAppUseCase = ClassLocator.provideStartAppUseCase(),
                    quickAppRepository = ClassLocator.provideQuickAppRepository()
                ) as T
            }

            modelClass.isAssignableFrom(AllAppViewModel::class.java) -> {
                AllAppViewModel(
                    allAppRepository = ClassLocator.provideAllAppRepository()
                ) as T
            }

            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(
                    settingsMenuListRepository = ClassLocator.provideSettingsMenuListRepository()
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}