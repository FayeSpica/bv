package dev.aaa1115910.bv.tv.screens.main.live

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import dev.aaa1115910.bv.tv.activities.video.LivePlayerActivity
import dev.aaa1115910.bv.tv.component.videocard.LiveCard
import dev.aaa1115910.bv.ui.theme.BVTheme
import dev.aaa1115910.bv.viewmodel.live.LiveFollowingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun FollowingScreen(
    modifier: Modifier = Modifier,
    liveFollowingViewModel: LiveFollowingViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentFocusedIndex by remember { mutableIntStateOf(0) }
    val shouldLoadMore by remember {
        derivedStateOf {
            currentFocusedIndex + 20 > liveFollowingViewModel.followingLives.size &&
                    liveFollowingViewModel.hasMore &&
                    !liveFollowingViewModel.isLoading
        }
    }

    // 初始加载
    LaunchedEffect(Unit) {
        if (liveFollowingViewModel.followingLives.isEmpty()) {
            scope.launch(Dispatchers.IO) {
                liveFollowingViewModel.loadFirstPage()
            }
        }
    }

    // 自动加载更多
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            scope.launch(Dispatchers.IO) {
                liveFollowingViewModel.loadMore()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            // 未登录状态
            !liveFollowingViewModel.isLogin -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "📺",
                        fontSize = 48.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = "未登录",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                    Text(
                        text = "请先登录以查看关注的直播",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            // 加载中且列表为空
            liveFollowingViewModel.isLoading && liveFollowingViewModel.followingLives.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                    Text(
                        text = "正在加载...",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
            // 列表为空且不在加载
            liveFollowingViewModel.followingLives.isEmpty() && !liveFollowingViewModel.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "📺",
                        fontSize = 48.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = "暂无直播",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                    Text(
                        text = if (liveFollowingViewModel.errorMessage.isNotEmpty()) {
                            liveFollowingViewModel.errorMessage
                        } else {
                            "关注的主播都没有在直播哦"
                        },
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            // 显示直播列表
            else -> {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    itemsIndexed(liveFollowingViewModel.followingLives) { index, liveItem ->
                        LiveCard(
                            roomId = liveItem.roomId,
                            title = liveItem.title,
                            cover = liveItem.showCover,
                            username = liveItem.uname,
                            face = liveItem.face,
                            online = liveItem.online,
                            areaName = liveItem.areaNameV2.ifEmpty { liveItem.areaName },
                            onClick = {
                                LivePlayerActivity.actionStart(
                                    context = context,
                                    roomId = liveItem.roomId,
                                    title = liveItem.title
                                )
                            },
                            onFocus = {
                                currentFocusedIndex = index
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:tv_1080p")
@Composable
private fun AnimeFeatureButtonsPreview() {
    BVTheme {
        FollowingScreen(
            modifier = Modifier,
        )
    }
}

