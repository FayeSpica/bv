package dev.aaa1115910.bv.tv.activities.video

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import dev.aaa1115910.bv.tv.screens.LivePlayerScreen
import dev.aaa1115910.bv.ui.theme.BVTheme
import dev.aaa1115910.bv.util.fInfo
import dev.aaa1115910.bv.viewmodel.LivePlayerViewModel
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.androidx.viewmodel.ext.android.viewModel

class LivePlayerActivity : ComponentActivity() {
    companion object {
        private val logger = KotlinLogging.logger { }
        fun actionStart(
            context: Context,
            roomId: Long,
            title: String = ""
        ) {
            context.startActivity(
                Intent(context, LivePlayerActivity::class.java).apply {
                    putExtra("roomId", roomId)
                    putExtra("title", title)
                }
            )
        }
    }

    private val livePlayerViewModel: LivePlayerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableImmersiveMode()
        getParamsFromIntent()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContent {
            BVTheme(forceDark = true) {
                LivePlayerScreen()
            }
        }
    }

    private fun enableImmersiveMode() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) enableImmersiveMode()
    }

    override fun onDestroy() {
        super.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        livePlayerViewModel.release()
    }

    override fun onPause() {
        super.onPause()
        livePlayerViewModel.pause()
    }

    override fun onResume() {
        super.onResume()
        livePlayerViewModel.resume()
    }

    private fun getParamsFromIntent() {
        if (intent.hasExtra("roomId")) {
            val roomId = intent.getLongExtra("roomId", 0)
            val title = intent.getStringExtra("title") ?: "直播间"
            logger.fInfo { "Launch live player: [roomId=$roomId, title=$title]" }
            livePlayerViewModel.apply {
                this.roomId = roomId
                this.title = title
                loadLiveStream(roomId.toInt())
            }
        } else {
            logger.fInfo { "Null launch parameter" }
            finish()
        }
    }
}

