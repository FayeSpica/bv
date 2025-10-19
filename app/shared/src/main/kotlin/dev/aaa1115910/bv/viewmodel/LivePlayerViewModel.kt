package dev.aaa1115910.bv.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.aaa1115910.biliapi.http.BiliLiveHttpApi
import dev.aaa1115910.bv.util.fInfo
import dev.aaa1115910.bv.util.fWarn
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LivePlayerViewModel : ViewModel() {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    var roomId: Long by mutableStateOf(0L)
    var title: String by mutableStateOf("")
    var liveStreamUrl: String by mutableStateOf("")
    var liveStatus: Int by mutableStateOf(0) // 0:未开播 1:正在直播 2:轮播
    var isLoading: Boolean by mutableStateOf(true)
    var errorMessage: String by mutableStateOf("")
    var isPlaying: Boolean by mutableStateOf(false)

    fun loadLiveStream(roomId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            errorMessage = ""
            runCatching {
                logger.fInfo { "Loading live stream for room $roomId" }
                
                // 获取直播间信息
                val roomInfo = BiliLiveHttpApi.getLiveRoomPlayInfo(roomId)
                logger.fInfo { "Room info: liveStatus=${roomInfo.data?.liveStatus}" }
                
                roomInfo.data?.let { data ->
                    liveStatus = data.liveStatus
                    
                    if (liveStatus != 1) {
                        errorMessage = when (liveStatus) {
                            0 -> "主播未开播"
                            2 -> "直播间轮播中"
                            else -> "直播间状态异常"
                        }
                        isLoading = false
                        return@launch
                    }
                }
                
                // 获取直播流地址
                val streamResponse = BiliLiveHttpApi.getLiveStreamUrl(roomId)
                logger.fInfo { "Stream response received" }
                
                streamResponse.data?.playUrlInfo?.playurl?.let { playUrl ->
                    // 优先选择 http_hls 协议的流
                    val hlsStream = playUrl.stream
                        .find { it.protocolName == "http_hls" }
                        ?.format?.firstOrNull()
                        ?.codec?.firstOrNull()
                    
                    // 如果没有 HLS，尝试 http_stream (FLV)
                    val flvStream = if (hlsStream == null) {
                        playUrl.stream
                            .find { it.protocolName == "http_stream" }
                            ?.format?.firstOrNull()
                            ?.codec?.firstOrNull()
                    } else null
                    
                    val selectedStream = hlsStream ?: flvStream
                    
                    selectedStream?.let { codec ->
                        if (codec.urlInfo.isNotEmpty()) {
                            val url = codec.urlInfo.first().host + codec.baseUrl + codec.urlInfo.first().extra
                            liveStreamUrl = url
                            logger.fInfo { "Live stream URL obtained: $url" }
                            isLoading = false
                            isPlaying = true
                        } else {
                            errorMessage = "无法获取直播流地址"
                            isLoading = false
                        }
                    } ?: run {
                        errorMessage = "无法解析直播流信息"
                        isLoading = false
                    }
                } ?: run {
                    errorMessage = "直播流信息为空"
                    isLoading = false
                }
            }.onFailure { e ->
                logger.fWarn { "Failed to load live stream: ${e.stackTraceToString()}" }
                errorMessage = "加载直播流失败: ${e.message}"
                isLoading = false
            }
        }
    }

    fun pause() {
        isPlaying = false
    }

    fun resume() {
        if (liveStreamUrl.isNotEmpty() && liveStatus == 1) {
            isPlaying = true
        }
    }

    fun release() {
        // 释放资源
        isPlaying = false
    }
}

