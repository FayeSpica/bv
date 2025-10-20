package dev.aaa1115910.biliapi.http.entity.live

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 关注的直播列表响应
 */
@Serializable
data class LiveFollowingData(
    val totalPage: Int = 0,
    val list: List<LiveFollowingItem> = emptyList()
)

/**
 * 关注的直播间信息
 * 
 * 真实样例:
 * ```json
 * {
 *   "roomid": 33989,
 *   "uid": 63231,
 *   "uname": "泛式",
 *   "title": "体验星塔旅人公测！",
 *   "face": "https://i0.hdslb.com/bfs/face/2608aaa45309c77ac88fbfaa40e160b8c7892985.jpg",
 *   "live_status": 1,
 *   "record_num": 0,
 *   "recent_record_id": "",
 *   "is_attention": 1,
 *   "clipnum": 0,
 *   "fans_num": 0,
 *   "area_name": "",
 *   "area_value": "",
 *   "tags": "",
 *   "recent_record_id_v2": "",
 *   "record_num_v2": 0,
 *   "record_live_time": 0,
 *   "area_name_v2": "星塔旅人",
 *   "room_news": "周六杂谈回...",
 *   "switch": true,
 *   "watch_icon": "https://...",
 *   "text_small": "8.7万",
 *   "room_cover": "https://...",
 *   "parent_area_id": 3,
 *   "area_id": 1024
 * }
 * ```
 */
@Serializable
data class LiveFollowingItem(
    val roomid: Long = 0,
    val uid: Long = 0,
    val uname: String = "",
    val face: String = "",
    val title: String = "",
    @SerialName("live_status")
    val liveStatus: Int = 0, // 0:未开播 1:直播中 2:轮播中
    @SerialName("record_num")
    val recordNum: Int = 0,
    @SerialName("recent_record_id")
    val recentRecordId: String = "",
    @SerialName("is_attention")
    val isAttention: Int = 0, // 是否关注 1:已关注
    val clipnum: Int = 0,
    @SerialName("fans_num")
    val fansNum: Int = 0,
    @SerialName("area_name")
    val areaName: String = "",
    @SerialName("area_value")
    val areaValue: String = "",
    val tags: String = "",
    @SerialName("recent_record_id_v2")
    val recentRecordIdV2: String = "",
    @SerialName("record_num_v2")
    val recordNumV2: Int = 0,
    @SerialName("record_live_time")
    val recordLiveTime: Long = 0,
    @SerialName("area_name_v2")
    val areaNameV2: String = "",
    @SerialName("room_news")
    val roomNews: String = "",
    val switch: Boolean = false,
    @SerialName("watch_icon")
    val watchIcon: String = "",
    @SerialName("text_small")
    val textSmall: String = "",
    @SerialName("room_cover")
    val roomCover: String = "",
    @SerialName("parent_area_id")
    val parentAreaId: Int = 0,
    @SerialName("area_id")
    val areaId: Int = 0
) {
    /**
     * 获取直播间ID（使用 roomid 字段）
     * 
     * @return 直播间ID
     */
    val roomId: Long get() = roomid
    
    /**
     * 获取在线人数（从 text_small 解析）
     * 
     * 例如: "8.7万" -> 87000, "1234" -> 1234
     * 
     * @return 在线观看人数
     */
    val online: Int get() {
        return when {
            textSmall.contains("万") -> {
                textSmall.replace("万", "").toFloatOrNull()?.let { (it * 10000).toInt() } ?: 0
            }
            else -> textSmall.toIntOrNull() ?: 0
        }
    }
    
    /**
     * 获取显示用的封面（优先使用 room_cover，如果为空则使用主播头像）
     * 
     * @return 封面URL
     */
    val showCover: String get() = roomCover.ifEmpty { face }
}

