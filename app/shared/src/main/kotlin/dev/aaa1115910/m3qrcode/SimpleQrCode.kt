package dev.aaa1115910.m3qrcode

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.Encoder

@Composable
fun SimpleQrCode(
    modifier: Modifier = Modifier,
    content: String,
    ecLevel: ErrorCorrectionLevel = ErrorCorrectionLevel.H
) {
    val matrix = remember(content, ecLevel) {
        if (content.isEmpty()) null
        else Encoder.encode(content, ecLevel, null).matrix
    }

    Canvas(
        modifier = modifier.aspectRatio(1f)
    ) {
        val qrMatrix = matrix ?: return@Canvas
        val matrixSize = qrMatrix.width
        val quietZone = 2
        val totalModules = matrixSize + quietZone * 2
        val moduleSize = size.width / totalModules

        // White background
        drawRect(color = Color.White, size = size)

        // Draw QR modules
        for (y in 0 until matrixSize) {
            for (x in 0 until matrixSize) {
                if (qrMatrix.get(x, y).toInt() == 1) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(
                            (x + quietZone) * moduleSize,
                            (y + quietZone) * moduleSize
                        ),
                        size = Size(moduleSize, moduleSize)
                    )
                }
            }
        }
    }
}
