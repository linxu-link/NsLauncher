package com.wujia.nslauncher.data.recentapp.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RecentAppResource(
    private val recentApi: IRecentApp,//依赖于接口能够使 API 实现在应用中可交换。除了提供可扩缩性并可让您更轻松地替换依赖项之外，这还有利于进行测试，可以在测试时注入虚构的数据源实现。
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getRecentApps() = withContext(dispatcher) {
        recentApi.getRecentlyUsedApps()
    }

}