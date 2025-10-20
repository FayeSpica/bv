package dev.aaa1115910.bv.viewmodel.live

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.aaa1115910.biliapi.http.entity.live.LiveFollowingItem
import dev.aaa1115910.biliapi.repositories.LiveRepository
import dev.aaa1115910.bv.util.fInfo
import dev.aaa1115910.bv.util.fWarn
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LiveFollowingViewModel(
    private val liveRepository: LiveRepository
) : ViewModel() {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    var followingLives = mutableStateListOf<LiveFollowingItem>()
    var isLoading by mutableStateOf(false)
    var isLogin by mutableStateOf(true)
    var errorMessage by mutableStateOf("")
    var currentPage by mutableStateOf(1)
    var totalPage by mutableStateOf(1)
    var hasMore by mutableStateOf(true)

    fun loadFirstPage() {
        viewModelScope.launch(Dispatchers.IO) {
            currentPage = 1
            followingLives.clear()
            loadMore()
        }
    }

    fun loadMore() {
        if (isLoading || !hasMore) return
        
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            errorMessage = ""
            
            runCatching {
                logger.fInfo { "Loading live following page $currentPage" }
                
                val data = liveRepository.getLiveFollowing(
                    page = currentPage,
                    pageSize = 20
                )
                
                if (data != null) {
                    // 成功获取（已登录状态）
                    totalPage = data.totalPage
                    logger.fInfo { "Loaded ${data.list.size} live items, total pages: $totalPage" }
                    
                    // 只添加正在直播的
                    val liveItems = data.list.filter { it.liveStatus == 1 }
                    followingLives.addAll(liveItems)
                    
                    currentPage++
                    hasMore = currentPage <= totalPage
                    isLogin = true
                } else {
                    // 未登录或其他错误
                    errorMessage = "未登录，请先登录"
                    isLogin = false
                    hasMore = false
                    logger.fInfo { "User not logged in or failed to load" }
                }
            }.onFailure { e ->
                logger.fWarn { "Failed to load live following: ${e.stackTraceToString()}" }
                errorMessage = "加载失败: ${e.message}"
                hasMore = false
            }
            
            isLoading = false
        }
    }

    fun refresh() {
        hasMore = true
        loadFirstPage()
    }
}

