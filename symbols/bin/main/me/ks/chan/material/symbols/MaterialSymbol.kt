package me.ks.chan.material.symbols

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.Float
import kotlin.Int
import kotlin.Lazy
import kotlin.String
import kotlin.Unit

public object MaterialSymbols

public fun materialSymbol(
  name: String,
  size: Int,
  pathBuilder: PathBuilder.() -> Unit,
): Lazy<ImageVector> = lazy {
  materialSymbol(name = name, size = size.dp, pathBuilder = pathBuilder)
}

private val ViewportSize: Float = 960.0F

private fun materialSymbol(
  name: String,
  size: Dp,
  pathBuilder: PathBuilder.() -> Unit,
): ImageVector = ImageVector.Builder(
      name = name,
      defaultWidth = size,
      defaultHeight = size,
      viewportWidth = ViewportSize,
      viewportHeight = ViewportSize,
  ).path(
      fill = SolidColor(Color.Black),
      pathBuilder = pathBuilder,
      strokeLineJoin = StrokeJoin.Bevel,
      strokeLineMiter = 1F,
      strokeLineWidth = 1F,
  ).build()
