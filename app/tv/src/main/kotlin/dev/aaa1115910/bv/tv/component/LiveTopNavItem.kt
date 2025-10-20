package dev.aaa1115910.bv.tv.component

import android.content.Context
import dev.aaa1115910.bv.BVApp

enum class LiveTopNavItem : TopNavItem {
    Following;

    override fun getDisplayName(context: Context): String {
        return when (this) {
            Following -> "我的关注"
        }
    }
}

