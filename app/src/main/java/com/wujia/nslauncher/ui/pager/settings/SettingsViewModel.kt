package com.wujia.nslauncher.ui.pager.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.wujia.nslauncher.arch.NsArchViewModel
import com.wujia.nslauncher.data.settings.SettingsMenuListRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

data class SettingsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    @SettingsPager val selectedItemIndex: Int = 0,
    val menuItems: List<String> = emptyList(),
)

sealed class SettingsIntent {
    data object LoadMenuList : SettingsIntent()
    data class ClickMenuItem(@SettingsPager val index: Int = 0) : SettingsIntent()
    data class OpenApp(val packageName: String) : SettingsIntent()
}

class SettingsViewModel(
    private val settingsMenuListRepository: SettingsMenuListRepository
) : NsArchViewModel() {

    private val _uiState = mutableStateOf(SettingsUiState())
    val uiState: State<SettingsUiState> = _uiState

    override fun onCleared() {
        super.onCleared()
    }

    /**
     * 处理用户意图
     */
    fun sendIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ClickMenuItem -> clickMenuItem(intent.index)
            is SettingsIntent.LoadMenuList -> fetchMenuList()
            is SettingsIntent.OpenApp -> openApp(intent.packageName)
        }
    }

    private var fetchJob: Job? = null

    private fun fetchMenuList() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val menus = settingsMenuListRepository.getMenuItems()
                _uiState.value = _uiState.value.copy(
                    menuItems = menus,
                    selectedItemIndex = 0,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.toString() ?: "Failed to load all apps",
                    isLoading = false
                )
            }
        }
    }

    private fun clickMenuItem(index: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                selectedItemIndex = index,
                isLoading = false
            )
        }
    }

    private fun openApp(packageName: String) {
        viewModelScope.launch {

        }
    }


}