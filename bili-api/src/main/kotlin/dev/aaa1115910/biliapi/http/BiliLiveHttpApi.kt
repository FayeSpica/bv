package dev.aaa1115910.biliapi.http

import dev.aaa1115910.biliapi.http.entity.BiliResponse
import dev.aaa1115910.biliapi.http.entity.live.DanmuInfoData
import dev.aaa1115910.biliapi.http.entity.live.HistoryDanmaku
import dev.aaa1115910.biliapi.http.entity.live.LiveFollowingData
import dev.aaa1115910.biliapi.http.entity.live.LiveStreamUrlData
import dev.aaa1115910.biliapi.http.entity.live.RoomPlayInfoData
import dev.aaa1115910.biliapi.http.plugins.BiliUserAgent
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object BiliLiveHttpApi {
    private var endPoint: String = ""
    private lateinit var client: HttpClient
    private val logger = KotlinLogging.logger { }

    init {
        createClient()
    }

    private fun createClient() {
        client = HttpClient(OkHttp) {
            BiliUserAgent()
            install(ContentNegotiation) {
                json(Json {
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
            install(ContentEncoding) {
                deflate(1.0F)
                gzip(0.9F)
            }
            defaultRequest {
                url {
                    host = "api.live.bilibili.com"
                    protocol = URLProtocol.HTTPS
                }
            }
        }
    }

    /**
     * 获取直播间[roomId]的弹幕连接地址等信息，例如 token
     */
    suspend fun getLiveDanmuInfo(roomId: Int): BiliResponse<DanmuInfoData> =
        client.get("/xlive/web-room/v1/index/getDanmuInfo") {
            parameter("id", roomId)
        }.body()

    /**
     * 获取直播间[roomId]的信息
     */
    suspend fun getLiveRoomPlayInfo(roomId: Int): BiliResponse<RoomPlayInfoData> =
        client.get("/xlive/web-room/v1/index/getRoomPlayInfo") {
            parameter("room_id", roomId)
        }.body()

    /**
     * 获取直播间[roomId]的历史弹幕
     */
    suspend fun getLiveDanmuHistory(roomId: Int): BiliResponse<HistoryDanmaku> =
        client.get("/xlive/web-room/v1/dM/gethistory") {
            parameter("roomid", roomId)
        }.body()

    /**
     * 获取直播间[roomId]的播放流地址
     * @param qn 清晰度 10000:原画 400:蓝光 250:超清 150:高清 80:流畅
     * @param platform 平台 web/h5
     */
    suspend fun getLiveStreamUrl(
        roomId: Int,
        qn: Int = 10000,
        platform: String = "web"
    ): BiliResponse<LiveStreamUrlData> =
        client.get("/xlive/web-room/v2/index/getRoomPlayInfo") {
            parameter("room_id", roomId)
            parameter("protocol", "0,1")
            parameter("format", "0,1,2")
            parameter("codec", "0,1")
            parameter("qn", qn)
            parameter("platform", platform)
            parameter("ptype", 8)
        }.body()

    /**
     * 获取关注的正在直播的主播列表
     * @param page 页码，从1开始
     * @param pageSize 每页数量，默认9
     * @param sessData 登录凭证（Cookie中的SESSDATA）
     * @return 关注的直播列表
     * 
     * 注意：此API需要登录，未登录会返回 code=-101
     * 
     * @see [B站API文档](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/live/user.md)
     */
    suspend fun getLiveFollowing(
        page: Int = 1,
        pageSize: Int = 9,
        sessData: String = ""
    ): BiliResponse<LiveFollowingData> =
        client.get("/xlive/web-ucenter/user/following") {
            parameter("page", page)
            parameter("page_size", pageSize)
            parameter("ignoreRecord", 1)
            parameter("hit_ab", true)
            if (sessData.isNotEmpty()) {
                header("Cookie", "SESSDATA=$sessData;")
            }
        }.body()

}