package dev.aaa1115910.biliapi.http

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BiliLiveFollowingApiTest {
    @Test
    fun `get live following list`() {
        Assertions.assertDoesNotThrow {
            runBlocking {
                val response = BiliLiveHttpApi.getLiveFollowing(
                    page = 1,
                    pageSize = 9
                )
                println("Response code: ${response.code}")
                println("Response message: ${response.message}")
                
                when (response.code) {
                    0 -> {
                        // 登录状态
                        response.data?.let { data ->
                            println("Total pages: ${data.totalPage}")
                            println("Live count: ${data.list.size}")
                            data.list.forEach { item ->
                                println("=".repeat(50))
                                println("房间号: ${item.roomId}")
                                println("主播: ${item.uname}")
                                println("标题: ${item.title}")
                                println("状态: ${when(item.liveStatus) {
                                    0 -> "未开播"
                                    1 -> "直播中"
                                    2 -> "轮播中"
                                    else -> "未知"
                                }}")
                                println("分区: ${item.areaV2ParentName} > ${item.areaV2Name}")
                                println("在线人数: ${item.online}")
                            }
                        }
                    }
                    -101 -> {
                        // 未登录
                        println("未登录状态，需要登录才能获取关注的直播列表")
                    }
                    else -> {
                        println("其他错误: ${response.message}")
                    }
                }
            }
        }
    }
}

