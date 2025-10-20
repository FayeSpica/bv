package dev.aaa1115910.bv.tv.component.videocard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import dev.aaa1115910.bv.ui.theme.BVTheme
import dev.aaa1115910.bv.util.focusedBorder

@Composable
fun LiveCard(
    modifier: Modifier = Modifier,
    roomId: Long,
    title: String,
    cover: String,
    username: String,
    face: String,
    online: Int,
    areaName: String,
    onClick: () -> Unit,
    onFocus: () -> Unit = {}
) {
    var hasFocus by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (hasFocus) 1.0f else 0.95f,
        label = "live card scale"
    )

    val onlineText = when {
        online >= 10000 -> "${online / 10000}万"
        else -> "$online"
    }

    LaunchedEffect(hasFocus) {
        if (hasFocus) onFocus()
    }

    Surface(
        modifier = modifier
            .width(300.dp)
            .scale(scale)
            .onFocusChanged { hasFocus = it.isFocused }
            .focusedBorder(MaterialTheme.shapes.medium)
            .clickable { onClick() },

    ) {
        Column {
            // 封面
            Box {
                AsyncImage(
                    model = cover,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(169.dp) // 16:9 比例
                )
                
                // 直播中标签
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(Color(0xFFFF6699), shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "直播中",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
                
                // 观看人数
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.6f), shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "👁 $onlineText",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
                
                // 分区
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.6f), shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = areaName,
                        color = Color.White,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            // 信息区域
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A1A1A))
                    .padding(12.dp)
            ) {
                // 标题
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color.White,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 主播信息
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = face,
                        contentDescription = username,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = username,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LiveCardPreview() {
    BVTheme {
        LiveCard(
            roomId = 33989,
            title = "【S15】冲分之路！今天一定要上大师！！",
            cover = "https://i0.hdslb.com/bfs/live/new_room_cover/b0d9bcfbe776fc2b01ae832bda9a4d55caa41649.jpg",
            username = "泛式",
            face = "https://i0.hdslb.com/bfs/face/2608aaa45309c77ac88fbfaa40e160b8c7892985.jpg",
            online = 87000,
            areaName = "星塔旅人",
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, name = "LiveCard - Small Online")
@Composable
private fun LiveCardSmallOnlinePreview() {
    BVTheme {
        LiveCard(
            roomId = 12345,
            title = "深夜聊天｜随便聊聊",
            cover = "https://i0.hdslb.com/bfs/live/room_cover_default.jpg",
            username = "小主播",
            face = "https://i0.hdslb.com/bfs/face/member/noface.jpg",
            online = 256,
            areaName = "聊天室",
            onClick = {}
        )
    }
}
