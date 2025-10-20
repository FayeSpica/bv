package dev.aaa1115910.biliapi.repositories

import dev.aaa1115910.biliapi.http.BiliLiveHttpApi
import dev.aaa1115910.biliapi.http.entity.live.LiveFollowingData
import org.koin.core.annotation.Single

@Single
class LiveRepository(
    private val authRepository: AuthRepository
) {
    /**
     * 获取关注的正在直播的主播列表
     * @param page 页码，从1开始
     * @param pageSize 每页数量，默认20
     * @return 关注的直播列表数据
     */
    suspend fun getLiveFollowing(
        page: Int = 1,
        pageSize: Int = 20
    ): LiveFollowingData? {
        val response = BiliLiveHttpApi.getLiveFollowing(
            page = page,
            pageSize = pageSize,
            sessData = authRepository.sessionData ?: ""
        )
        return if (response.code == 0) {
            response.data
        } else {
            null
        }
    }
}

