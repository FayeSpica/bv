package dev.aaa1115910.bv.tv.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.Surface
import coil.compose.AsyncImage
import dev.aaa1115910.bv.ui.theme.BVTheme
import dev.aaa1115910.bv.util.isDpadRight
import dev.aaa1115910.bv.util.isKeyDown

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    isLogin: Boolean = false,
    avatar: String = "",
    username: String = "",
    onDrawerItemChanged: (DrawerItem) -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onShowUserPanel: () -> Unit = {},
    onFocusToContent: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    var selectedItem by remember { mutableStateOf(DrawerItem.Home) }
    val centerFocusRequester = remember { FocusRequester() }
    var tabMoved by remember { mutableStateOf(true) }

    // 处理选中项变化
    androidx.compose.runtime.LaunchedEffect(selectedItem) {
        tabMoved = false
        onDrawerItemChanged(selectedItem)
        tabMoved = true
    }

    Box(
        modifier = modifier
            .width(60.dp)
            .fillMaxHeight()
            .onPreviewKeyEvent { keyEvent ->
                if (keyEvent.isDpadRight()) {
                    if (keyEvent.isKeyDown()) {
                        if (tabMoved) onFocusToContent()
                        return@onPreviewKeyEvent true
                    }
                }
                false
            }
    ) {
        // 右边分割线
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color.Gray.copy(alpha = 0.3f))
                .align(Alignment.CenterEnd)
        )
        
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 顶部导航项
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    DrawerItem.Home,
                    DrawerItem.Search,
                    DrawerItem.UGC,
                    DrawerItem.PGC,
                    DrawerItem.Live,
                ).forEach { item ->
                    DrawerItem(
                        item = item,
                        isSelected = selectedItem == item,
                        isFocused = selectedItem == item,
                        onItemClick = { selectedItem = item },
                        focusRequester = if (item == DrawerItem.Home) centerFocusRequester else null
                    )
                }
            }

            // 底部功能项
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 用户项
                DrawerItem(
                    item = DrawerItem.User,
                    isSelected = selectedItem == DrawerItem.User,
                    isFocused = selectedItem == DrawerItem.User,
                    onItemClick = {
                        if (isLogin) {
                            onShowUserPanel()
                        } else {
                            onLogin()
                        }
                    },
                    isLogin = isLogin,
                    avatar = avatar,
                    username = username
                )

                // 设置项
                DrawerItem(
                    item = DrawerItem.Settings,
                    isSelected = false,
                    isFocused = false,
                    onItemClick = onOpenSettings
                )
            }
        }
    }
}

@Composable
private fun DrawerItem(
    item: DrawerItem,
    isSelected: Boolean,
    isFocused: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    isLogin: Boolean = false,
    avatar: String = "",
    username: String = ""
) {
    Surface(
        modifier = modifier
            .width(60.dp)
            .height(48.dp)
            .let { mod ->
                if (focusRequester != null) {
                    mod.focusRequester(focusRequester)
                } else {
                    mod
                }
            }
            .onFocusChanged { },
        onClick = onItemClick,
        colors = androidx.tv.material3.ClickableSurfaceDefaults.colors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标
            if (item == DrawerItem.User && isLogin) {
                Surface(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                    color = Color.Gray
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        model = avatar,
                        contentDescription = username,
                        contentScale = ContentScale.FillBounds
                    )
                }
            } else {
                Icon(
                    imageVector = item.displayIcon,
                    contentDescription = item.displayName,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

enum class DrawerItem(
    val displayName: String,
    val displayIcon: ImageVector
) {
    User(displayName = "登录", displayIcon = Icons.Default.AccountCircle),
    Search(displayName = "搜索", displayIcon = Icons.Default.Search),
    Home(displayName = "首页", displayIcon = Icons.Default.Home),
    UGC(displayName = "UGC", displayIcon = Icons.Default.OndemandVideo),
    PGC(displayName = "PGC", displayIcon = Icons.Default.Movie),
    Live(displayName = "直播", displayIcon = Icons.Default.LiveTv),
    Settings(displayName = "设置", displayIcon = Icons.Default.Settings), ;
}

@Preview(device = "id:tv_1080p")
@Composable
private fun DrawerContentPreview() {
    BVTheme {
        Box {
            DrawerContent()
        }
    }
}
