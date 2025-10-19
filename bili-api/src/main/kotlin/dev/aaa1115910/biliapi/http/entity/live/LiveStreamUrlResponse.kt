package dev.aaa1115910.biliapi.http.entity.live

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveStreamUrlData(
    @SerialName("room_id")
    val roomId: Int = 0,
    @SerialName("short_id")
    val shortId: Int = 0,
    val uid: Long = 0,
    @SerialName("is_hidden")
    val isHidden: Boolean = false,
    @SerialName("is_locked")
    val isLocked: Boolean = false,
    @SerialName("is_portrait")
    val isPortrait: Boolean = false,
    @SerialName("live_status")
    val liveStatus: Int = 0,
    @SerialName("hidden_till")
    val hiddenTill: Int = 0,
    @SerialName("lock_till")
    val lockTill: Int = 0,
    val encrypted: Boolean = false,
    @SerialName("pwd_verified")
    val pwdVerified: Boolean = false,
    @SerialName("live_time")
    val liveTime: Long = 0,
    @SerialName("room_shield")
    val roomShield: Int = 0,
    @SerialName("all_special_types")
    val allSpecialTypes: List<Int> = emptyList(),
    @SerialName("playurl_info")
    val playUrlInfo: PlayUrlInfo? = null
)

@Serializable
data class PlayUrlInfo(
    @SerialName("conf_json")
    val confJson: String = "",
    val playurl: PlayUrl? = null
)

@Serializable
data class PlayUrl(
    val cid: Long = 0,
    @SerialName("g_qn_desc")
    val gQnDesc: List<QnDesc> = emptyList(),
    val stream: List<Stream> = emptyList(),
    @SerialName("p2p_data")
    val p2pData: P2PData? = null,
    @SerialName("dolby_qn")
    val dolbyQn: String? = null
)

@Serializable
data class QnDesc(
    val qn: Int = 0,
    val desc: String = "",
    @SerialName("hdr_desc")
    val hdrDesc: String = "",
    @SerialName("attr_desc")
    val attrDesc: String? = null
)

@Serializable
data class Stream(
    @SerialName("protocol_name")
    val protocolName: String = "",
    val format: List<Format> = emptyList()
)

@Serializable
data class Format(
    @SerialName("format_name")
    val formatName: String = "",
    val codec: List<Codec> = emptyList()
)

@Serializable
data class Codec(
    @SerialName("codec_name")
    val codecName: String = "",
    @SerialName("current_qn")
    val currentQn: Int = 0,
    @SerialName("accept_qn")
    val acceptQn: List<Int> = emptyList(),
    @SerialName("base_url")
    val baseUrl: String = "",
    @SerialName("url_info")
    val urlInfo: List<UrlInfo> = emptyList(),
    @SerialName("hdr_qn")
    val hdrQn: String? = null,
    @SerialName("dolby_type")
    val dolbyType: Int = 0,
    @SerialName("attr_name")
    val attrName: String = ""
)

@Serializable
data class UrlInfo(
    val host: String = "",
    val extra: String = "",
    @SerialName("stream_ttl")
    val streamTtl: Int = 0
)

@Serializable
data class P2PData(
    @SerialName("p2p")
    val p2p: Boolean = false,
    @SerialName("p2p_type")
    val p2pType: Int = 0,
    @SerialName("m_p2p")
    val mP2p: Boolean = false,
    @SerialName("m_servers")
    val mServers: String? = null
)

