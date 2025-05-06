package com.wujia.nslauncher.ui.pager

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.wujia.nslauncher.arch.NsArchViewModel
import com.wujia.nslauncher.data.quickapp.QuickAppRepository
import com.wujia.nslauncher.domain.GetRecentlyAppListUseCase
import com.wujia.nslauncher.domain.StartAppUseCase
import com.wujia.nslauncher.entity.NsAppInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// 集中管理 UI 状态（密封类或数据类）
data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val recentlyAppItems: List<HomeItemUiState> = emptyList(),
    val quickApps: List<NsAppInfo> = emptyList(),
)

data class HomeItemUiState(
    val appInfo: NsAppInfo,
    val selected: Boolean = false
)

// 用户意图（事件）
sealed class HomeIntent {
    // 加载最近使用的app列表
    data class LoadRecentlyApps(val size: Int = 6) : HomeIntent()

    // 加载快捷app
    data object LoadQuickApp : HomeIntent()

    // 选中APP、
    data class SelectedApp(val pkgName: String) : HomeIntent()

    // 点击app
    data class ClickApp(val pkgName: String) : HomeIntent()
}

class HomeViewModel(
    private val getRecentlyAppListUseCase: GetRecentlyAppListUseCase,
    private val startAppUseCase: StartAppUseCase,
    private val quickAppRepository: QuickAppRepository,
) : NsArchViewModel() {

    private val _uiState = mutableStateOf(HomeUiState())
    val uiState: State<HomeUiState> = _uiState

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadRecentlyApps -> fetchRecentlyApps(intent.size)
            is HomeIntent.LoadQuickApp -> fetchQuickApps()
            is HomeIntent.ClickApp -> handleAppClick(intent.pkgName)
            is HomeIntent.SelectedApp -> handleAppSelected(intent.pkgName)
        }
    }

    private var fetchJob: Job? = null

    private fun fetchRecentlyApps(size: Int) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val apps = getRecentlyAppListUseCase(size)
                _uiState.value = _uiState.value.copy(
                    recentlyAppItems = apps.map { app ->
                        HomeItemUiState(
                            appInfo = app,
                            selected = _uiState.value.recentlyAppItems
                                .find { it.appInfo.packageName == app.packageName }?.selected
                                ?: false
                        )
                    },
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.toString() ?: "Failed to load recent apps",
                    isLoading = false
                )
            }
        }
    }

    private fun fetchQuickApps() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val quickApps = quickAppRepository.getQuickApps()
                _uiState.value = _uiState.value.copy(
                    quickApps = quickApps,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load quick apps",
                    isLoading = false
                )
            }
        }
    }

    private fun handleAppSelected(pkgName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                recentlyAppItems = _uiState.value.recentlyAppItems.map { item ->
                    item.copy(selected = item.appInfo.packageName == pkgName)
                }
            )
        }
    }

    private fun handleAppClick(pkgName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                recentlyAppItems = _uiState.value.recentlyAppItems.map { item ->
                    item.copy(selected = false)
                }
            )
            startAppUseCase(pkgName)
        }
    }
}