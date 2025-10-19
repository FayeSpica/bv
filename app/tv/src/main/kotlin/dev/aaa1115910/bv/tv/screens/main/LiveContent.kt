package dev.aaa1115910.bv.tv.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.tv.material3.Text

@Composable
fun LiveContent(
    modifier: Modifier = Modifier,
    navFocusRequester: FocusRequester
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .focusRequester(navFocusRequester),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "直播内容（开发中）")
    }
}

