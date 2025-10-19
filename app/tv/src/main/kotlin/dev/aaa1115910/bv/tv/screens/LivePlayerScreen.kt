package dev.aaa1115910.bv.tv.screens

import android.net.Uri
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.tv.material3.Text
import dev.aaa1115910.bv.viewmodel.LivePlayerViewModel
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun LivePlayerScreen(
    modifier: Modifier = Modifier,
    livePlayerViewModel: LivePlayerViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val logger = KotlinLogging.logger { }
    val focusRequester = remember { FocusRequester() }
    
    // 标题显示状态
    var showTitle by remember { mutableStateOf(true) }
    var titleVisibilityTrigger by remember { mutableStateOf(0) }

    // 创建 ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
            playWhenReady = true
        }
    }

    // 监听直播流 URL 变化
    LaunchedEffect(livePlayerViewModel.liveStreamUrl) {
        if (livePlayerViewModel.liveStreamUrl.isNotEmpty()) {
            logger.info { "Setting live stream URL: ${livePlayerViewModel.liveStreamUrl}" }
            val mediaItem = MediaItem.fromUri(Uri.parse(livePlayerViewModel.liveStreamUrl))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
            // 播放开始时显示标题
            showTitle = true
            titleVisibilityTrigger++
        }
    }

    // 监听播放状态
    LaunchedEffect(livePlayerViewModel.isPlaying) {
        if (livePlayerViewModel.isPlaying) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    // 标题自动隐藏逻辑
    LaunchedEffect(titleVisibilityTrigger) {
        if (titleVisibilityTrigger > 0) {
            delay(3000) // 3秒后隐藏
            showTitle = false
        }
    }

    // 请求焦点
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // 清理资源
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    BackHandler {
        (context as? androidx.activity.ComponentActivity)?.finish()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                // 任何按键都重新显示标题
                if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    showTitle = true
                    titleVisibilityTrigger++
                    true
                } else {
                    false
                }
            }
    ) {
        when {
            livePlayerViewModel.isLoading -> {
                // 加载中
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color.White)
                    Text(
                        text = "正在加载直播流...",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
            livePlayerViewModel.errorMessage.isNotEmpty() -> {
                // 错误信息
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = livePlayerViewModel.errorMessage,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "按返回键退出",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            else -> {
                // 播放器视图
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            useController = true
                            controllerAutoShow = true
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // 标题显示（带动画）
                AnimatedVisibility(
                    visible = showTitle && livePlayerViewModel.title.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = livePlayerViewModel.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(32.dp)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

