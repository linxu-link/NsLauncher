package com.wujia.nslauncher.ui.pager.allapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.wujia.nslauncher.arch.NsArchViewModel
import com.wujia.nslauncher.data.allapp.AllAppRepository
import com.wujia.nslauncher.entity.NsAppInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// 集中管理 UI 状态（密封类或数据类）
data class AllAppUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val apps: List<AllAppItemUiState> = emptyList(),
)

data class AllAppItemUiState(
    val appInfo: NsAppInfo,
    val selected: Boolean = false
)

// 用户意图（事件）
sealed class AllAppIntent {
    // 加载数据
    data class LoadAllApps(val orderType: Int = -1) : AllAppIntent()
}

private const val TAG = "AllAppViewModel"

class AllAppViewModel(
    private val allAppRepository: AllAppRepository
) : NsArchViewModel() {

    private val _uiState = mutableStateOf(AllAppUiState())
    val uiState: State<AllAppUiState> = _uiState

    override fun onCleared() {
        super.onCleared()
    }

    /**
     * 处理用户意图
     */
    fun handleIntent(intent: AllAppIntent) {
        when (intent) {
            is AllAppIntent.LoadAllApps -> fetchAllInstalledApps(intent.orderType)
        }
    }

    private var fetchJob: Job? = null

    private fun fetchAllInstalledApps(size: Int) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val apps = allAppRepository.getLatestAllApps(true)
                _uiState.value = _uiState.value.copy(
                    apps = apps.map { app ->
                        AllAppItemUiState(
                            appInfo = app,
                            selected = _uiState.value.apps
                                .find { it.appInfo.packageName == app.packageName }?.selected
                                ?: false
                        )
                    },
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


}