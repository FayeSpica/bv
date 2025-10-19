package dev.aaa1115910.biliapi.http

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BiliLiveStreamUrlApiTest {
    @Test
    fun `get live stream url`() {
        Assertions.assertDoesNotThrow {
            runBlocking {
                val response = BiliLiveHttpApi.getLiveStreamUrl(roomId = 22739471)
                println("Response code: ${response.code}")
                println("Live status: ${response.data?.liveStatus}")
                response.data?.playUrlInfo?.playurl?.let { playUrl ->
                    println("Stream count: ${playUrl.stream.size}")
                    playUrl.stream.forEach { stream ->
                        println("Protocol: ${stream.protocolName}")
                        stream.format.forEach { format ->
                            println("  Format: ${format.formatName}")
                            format.codec.forEach { codec ->
                                println("    Codec: ${codec.codecName}")
                                if (codec.urlInfo.isNotEmpty()) {
                                    val url = codec.urlInfo.first().host + codec.baseUrl + codec.urlInfo.first().extra
                                    println("    URL: ${url.take(100)}...")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

