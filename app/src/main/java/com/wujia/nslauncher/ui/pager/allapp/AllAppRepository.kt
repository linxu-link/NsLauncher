package com.wujia.nslauncher.ui.pager.allapp

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wujia.launcher.core.HiLauncher
import com.wujia.launcher.core.interfaces.InstalledAppChangeListener
import com.wujia.nslauncher.arch.NsArchRepository
import com.wujia.nslauncher.entity.NsAppInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Deprecated("--")
class AllAppRepository : NsArchRepository() {

//    fun fetchAllApps(): Flow<PagingData<NsAppInfo>> {
//        return Pager(PagingConfig(pageSize = 6, enablePlaceholders = false)) {
//            AllAppPagingSource()
//        }.flow
//    }

    fun fetchAllApps(): List<NsAppInfo> {
        val appList = mutableListOf<NsAppInfo>()
        HiLauncher.getInstalledApps().forEach {
            val appInfo =
                NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity)
            appList.add(appInfo)
        }
        return appList
    }

    fun fetchQuickApps(): List<NsAppInfo> {
        val appList = mutableListOf<NsAppInfo>()
        HiLauncher.getAppInfo("com.android.phone")?.let {
            appList.add(NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity))
        }
        HiLauncher.getAppInfo("com.android.settings")?.let {
            appList.add(NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity))
        }
        HiLauncher.getAppInfo("com.android.camera")?.let {
            appList.add(NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity))
        }
        HiLauncher.getAppInfo("com.android.gallery3d")?.let {
            appList.add(NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity))
        }
        HiLauncher.getAppInfo("com.android.mms")?.let {
            appList.add(NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity))
        }
        return appList
    }

    fun navigateToDetail(pkgName: String) {
        HiLauncher.startApp(pkgName)
    }

    fun registerInstallListener(listener: InstalledAppChangeListener) {
        HiLauncher.registerAppInstallListener(listener)
    }

    fun unregisterInstallListener(listener: InstalledAppChangeListener) {
        HiLauncher.unregisterAppInstallListener(listener)
    }

    class AllAppPagingSource() : PagingSource<Int, NsAppInfo>() {

        private val allApps = mutableListOf<NsAppInfo>()

        init {
            CoroutineScope(Dispatchers.Default).launch {
                allApps.clear()
                allApps.addAll(HiLauncher.getInstalledApps().map {
                    NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity)
                })
            }
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NsAppInfo> {
            return try {
                val page = params.key ?: 1
                val allApps = allApps.subList((page - 1) * params.loadSize, page * params.loadSize)
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (allApps.isNotEmpty()) page + 1 else null
                LoadResult.Page(allApps, prevKey, nextKey)
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, NsAppInfo>): Int? = null

    }

}