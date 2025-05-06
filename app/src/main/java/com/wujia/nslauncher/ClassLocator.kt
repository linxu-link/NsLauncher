package com.wujia.nslauncher

import android.content.Context
import androidx.room.Room
import com.wujia.nslauncher.data.allapp.AllAppRepository
import com.wujia.nslauncher.data.allapp.remote.AllAppResource
import com.wujia.nslauncher.data.quickapp.QuickAppRepository
import com.wujia.nslauncher.data.recentapp.RecentAppRepository
import com.wujia.nslauncher.data.recentapp.local.IRecentApp
import com.wujia.nslauncher.data.recentapp.local.RecentAppImpl
import com.wujia.nslauncher.data.recentapp.local.RecentAppResource
import com.wujia.nslauncher.data.recentapp.local.dao.RecentAppDao
import com.wujia.nslauncher.data.recentapp.local.dao.RecentAppDataBase
import com.wujia.nslauncher.data.settings.SettingsMenuListRepository
import com.wujia.nslauncher.domain.GetRecentlyAppListUseCase
import com.wujia.nslauncher.domain.StartAppUseCase
import com.wujia.toolkit.HiAppGlobal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.annotations.TestOnly

/**
 * 服务定位器
 * https://en.wikipedia.org/wiki/Service_locator_pattern
 */
object ClassLocator {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Volatile
    private var _allAppRepository: AllAppRepository? = null

    @Volatile
    private var _recentAppRepository: RecentAppRepository? = null

    fun provideAllAppRepository(allAppResource: AllAppResource = provideAllAppResource()): AllAppRepository =
        _allAppRepository ?: synchronized(this) {
            _allAppRepository ?: AllAppRepository(
                allResources = allAppResource,
                externalScope = scope
            ).also { _allAppRepository = it }
        }

    fun provideRecentAppRepository(recentAppResource: RecentAppResource = provideRecentAppResource()): RecentAppRepository =
        _recentAppRepository ?: synchronized(this) {
            _recentAppRepository ?: RecentAppRepository(
                recentAppResource = recentAppResource,
                externalScope = scope
            ).also { _recentAppRepository = it }
        }

    @Volatile
    private var _recentlyAppListUseCase: GetRecentlyAppListUseCase? = null

    fun provideRecentlyAppListUseCase(
        allAppRepository: AllAppRepository = provideAllAppRepository(),
        recentAppRepository: RecentAppRepository = provideRecentAppRepository()
    ): GetRecentlyAppListUseCase {
        return _recentlyAppListUseCase ?: synchronized(this) {
            _recentlyAppListUseCase ?: GetRecentlyAppListUseCase(
                Dispatchers.Default, allAppRepository, recentAppRepository
            ).also {
                _recentlyAppListUseCase = it
            }
        }
    }

    @Volatile
    private var _allAppResource: AllAppResource? = null

    fun provideAllAppResource(): AllAppResource {
        return _allAppResource ?: synchronized(this) {
            _allAppResource ?: AllAppResource(Dispatchers.Default)
        }
    }


    @Volatile
    private var _recentAppResource: RecentAppResource? = null

    fun provideRecentAppResource(recentApp: IRecentApp = provideRecentApi()): RecentAppResource {
        return _recentAppResource ?: synchronized(this) {
            _recentAppResource ?: RecentAppResource(
                recentApi = recentApp,
                dispatcher = Dispatchers.IO
            )
        }
    }

    @Volatile
    private var _recentApi: IRecentApp? = null

    fun provideRecentApi(recentAppDao: RecentAppDao = provideRecentDao()): IRecentApp {
        return _recentApi ?: synchronized(this) {
            _recentApi ?: RecentAppImpl(recentAppDao)
        }
    }

    @Volatile
    private var _recentAppDataBase: RecentAppDataBase? = null

    fun provideDataBase(context: Context): RecentAppDataBase {
        return _recentAppDataBase ?: synchronized(this) {
            _recentAppDataBase ?: Room.databaseBuilder(
                context.applicationContext,
                RecentAppDataBase::class.java,
                "recent.db"
            ).build()
        }
    }

    @Volatile
    private var _recentDao: RecentAppDao? = null

    fun provideRecentDao(dataBase: RecentAppDataBase = provideDataBase(provideApplication())): RecentAppDao {
        return _recentDao ?: synchronized(this) {
            _recentDao ?: dataBase.RecentAppDao()
        }
    }

    fun provideApplication(): Context {
        return HiAppGlobal.getApplication()
    }

    @Volatile
    private var _quickApp: QuickAppRepository? = null

    fun provideQuickAppRepository(): QuickAppRepository {
        return _quickApp ?: synchronized(this) {
            _quickApp ?: QuickAppRepository(scope)
        }
    }

    @Volatile
    private var _settingsMenuListRepository: SettingsMenuListRepository? = null

    fun provideSettingsMenuListRepository(): SettingsMenuListRepository {
        return _settingsMenuListRepository ?: synchronized(this) {
            _settingsMenuListRepository ?: SettingsMenuListRepository()
        }
    }

    @Volatile
    private var _startAppUseCase: StartAppUseCase? = null

    fun provideStartAppUseCase(): StartAppUseCase {
        return _startAppUseCase ?: synchronized(this) {
            _startAppUseCase ?: StartAppUseCase()
        }
    }

    // 用于测试时替换依赖
    @Synchronized
    @TestOnly
    fun setAllAppRepository(repository: AllAppRepository) {
        _allAppRepository = repository
    }

    @Synchronized
    @TestOnly
    fun setRecentAppRepository(repository: RecentAppRepository) {
        _recentAppRepository = repository
    }

    // 用于测试后重置
    @Synchronized
    @TestOnly
    fun reset() {
        _allAppRepository = null
        _recentAppRepository = null
    }
}