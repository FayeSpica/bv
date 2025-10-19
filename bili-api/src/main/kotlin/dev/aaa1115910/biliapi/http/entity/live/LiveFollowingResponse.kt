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
 */
@Serializable
data class LiveFollowingItem(
    val roomid: Long = 0,
    val uid: Long = 0,
    val uname: String = "",
    val face: String = "",
    val title: String = "",
    @SerialName("room_id")
    val roomId: Long = 0,
    @SerialName("short_id")
    val shortId: Int = 0,
    @SerialName("online")
    val online: Int = 0,
    @SerialName("live_status")
    val liveStatus: Int = 0, // 0:未开播 1:直播中 2:轮播中
    @SerialName("live_time")
    val liveTime: String = "",
    @SerialName("cover_from_user")
    val coverFromUser: String = "",
    val keyframe: String = "",
    @SerialName("lock_status")
    val lockStatus: Int = 0,
    @SerialName("hidden_status")
    val hiddenStatus: Int = 0,
    @SerialName("user_cover")
    val userCover: String = "",
    @SerialName("system_cover")
    val systemCover: String = "",
    @SerialName("show_cover")
    val showCover: String = "",
    @SerialName("area_name")
    val areaName: String = "",
    @SerialName("area_v2_id")
    val areaV2Id: Int = 0,
    @SerialName("area_v2_name")
    val areaV2Name: String = "",
    @SerialName("area_v2_parent_id")
    val areaV2ParentId: Int = 0,
    @SerialName("area_v2_parent_name")
    val areaV2ParentName: String = "",
    @SerialName("broadcast_type")
    val broadcastType: Int = 0,
    @SerialName("tag_name")
    val tagName: String = "",
    @SerialName("special_type")
    val specialType: Int = 0,
    val link: String = "",
    @SerialName("is_nft")
    val isNft: Int = 0,
    @SerialName("nft_dmark")
    val nftDmark: String = "",
    val watched: Int = 0,
    @SerialName("is_auto_play")
    val isAutoPlay: Int = 0
)

