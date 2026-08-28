package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.BrainDNA
import com.example.domain.model.CognitiveCategory
import com.example.ui.theme.CortexBorder
import com.example.ui.theme.CortexBorderGlow
import com.example.ui.theme.CortexCyan
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.CortexEmerald
import com.example.ui.theme.CortexPrimary
import com.example.ui.theme.CortexSurface
import com.example.ui.theme.CortexSurfaceElevated
import com.example.ui.theme.CortexTextMuted
import com.example.ui.theme.CortexTextPrimary
import com.example.ui.theme.CortexTextSecondary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CortexCard(
  modifier: Modifier = Modifier,
  borderColor: Color = CortexBorder,
  backgroundColor: Color = CortexSurface,
  shape: RoundedCornerShape = RoundedCornerShape(16.dp),
  glow: Boolean = false,
  content: @Composable () -> Unit
) {
  Box(
    modifier = modifier
      .clip(shape)
      .background(backgroundColor)
      .border(
        width = if (glow) 1.5.dp else 1.dp,
        brush = Brush.linearGradient(
          colors = if (glow) listOf(CortexCyan.copy(alpha = 0.8f), CortexPrimary.copy(alpha = 0.4f), CortexBorder)
          else listOf(borderColor, borderColor.copy(alpha = 0.4f))
        ),
        shape = shape
      )
  ) {
    content()
  }
}

@Composable
fun NeonButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  color: Color = CortexPrimary,
  textColor: Color = Color.White,
  enabled: Boolean = true,
  testTag: String = "neon_button"
) {
  val interactionSource = remember { MutableInteractionSource() }

  Box(
    modifier = modifier
      .testTag(testTag)
      .height(52.dp)
      .clip(RoundedCornerShape(14.dp))
      .background(
        brush = if (enabled) Brush.horizontalGradient(
          colors = listOf(color, color.copy(alpha = 0.85f), CortexCyan.copy(alpha = 0.6f))
        ) else Brush.horizontalGradient(listOf(Color.DarkGray, Color.Gray))
      )
      .border(
        width = 1.dp,
        color = if (enabled) color.copy(alpha = 0.9f) else Color.Transparent,
        shape = RoundedCornerShape(14.dp)
      )
      .clickable(
        enabled = enabled,
        onClick = onClick
      ),
    contentAlignment = Alignment.Center
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 20.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      if (icon != null) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = textColor,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
      }
      Text(
        text = text,
        color = textColor,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        letterSpacing = 0.5.sp
      )
    }
  }
}

@Composable
fun BrainRadarChart(
  brainDNA: BrainDNA,
  modifier: Modifier = Modifier,
  accentColor: Color = CortexCyan
) {
  val categories = CognitiveCategory.entries
  val count = categories.size
  val animatedValues = categories.map { cat ->
    animateFloatAsState(
      targetValue = (brainDNA.getScore(cat).toFloat() / 100f).coerceIn(0.1f, 1f),
      animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
      label = "radar_${cat.code}"
    )
  }

  Canvas(modifier = modifier) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val maxRadius = (minOf(size.width, size.height) / 2f) * 0.78f

    // Draw concentric polygon grid webs (25%, 50%, 75%, 100%)
    val steps = 4
    for (s in 1..steps) {
      val r = maxRadius * (s.toFloat() / steps)
      val gridPath = Path()
      for (i in 0 until count) {
        val angle = (2 * PI / count) * i - (PI / 2)
        val x = (center.x + r * cos(angle)).toFloat()
        val y = (center.y + r * sin(angle)).toFloat()
        if (i == 0) gridPath.moveTo(x, y) else gridPath.lineTo(x, y)
      }
      gridPath.close()
      drawPath(
        path = gridPath,
        color = CortexBorder.copy(alpha = 0.5f),
        style = Stroke(width = 1.dp.toPx())
      )
    }

    // Draw axis lines from center to outer points
    for (i in 0 until count) {
      val angle = (2 * PI / count) * i - (PI / 2)
      val x = (center.x + maxRadius * cos(angle)).toFloat()
      val y = (center.y + maxRadius * sin(angle)).toFloat()
      drawLine(
        color = CortexBorder.copy(alpha = 0.4f),
        start = center,
        end = Offset(x, y),
        strokeWidth = 1.dp.toPx()
      )
    }

    // Draw active Brain DNA polygon
    val dataPath = Path()
    val points = mutableListOf<Offset>()
    for (i in 0 until count) {
      val frac = animatedValues[i].value
      val r = maxRadius * frac
      val angle = (2 * PI / count) * i - (PI / 2)
      val x = (center.x + r * cos(angle)).toFloat()
      val y = (center.y + r * sin(angle)).toFloat()
      val point = Offset(x, y)
      points.add(point)
      if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
    }
    dataPath.close()

    // Fill with glowing semi-transparent gradient
    drawPath(
      path = dataPath,
      brush = Brush.radialGradient(
        colors = listOf(accentColor.copy(alpha = 0.45f), CortexPrimary.copy(alpha = 0.2f), Color.Transparent),
        center = center,
        radius = maxRadius
      )
    )

    // Stroke outline
    drawPath(
      path = dataPath,
      color = accentColor,
      style = Stroke(width = 2.5.dp.toPx())
    )

    // Draw vertex glowing dots
    for (p in points) {
      drawCircle(
        color = CortexDeepVoid,
        radius = 5.dp.toPx(),
        center = p
      )
      drawCircle(
        color = CortexCyan,
        radius = 3.5.dp.toPx(),
        center = p
      )
    }
  }
}

@Composable
fun BrainDNABarRow(
  category: CognitiveCategory,
  score: Int,
  modifier: Modifier = Modifier
) {
  val animatedScore by animateFloatAsState(
    targetValue = (score / 100f).coerceIn(0f, 1f),
    animationSpec = tween(700),
    label = "score_bar_${category.code}"
  )

  Column(modifier = modifier.fillMaxWidth().padding(vertical = 5.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        CategoryIcon(category = category, size = 16.dp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = category.displayName.uppercase(),
          color = CortexTextSecondary,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 0.5.sp
        )
      }
      Text(
        text = "$score",
        color = category.themeColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(4.dp))

    // High-tech segmented progress bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(CortexSurfaceElevated)
        .border(0.5.dp, CortexBorder, RoundedCornerShape(4.dp))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(fraction = animatedScore)
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp))
          .background(
            Brush.horizontalGradient(
              colors = listOf(category.themeColor.copy(alpha = 0.6f), category.themeColor)
            )
          )
      )
    }
  }
}

@Composable
fun CategoryIcon(
  category: CognitiveCategory,
  modifier: Modifier = Modifier,
  size: Dp = 20.dp
) {
  val icon = when (category) {
    CognitiveCategory.LOGIC -> Icons.Default.Psychology
    CognitiveCategory.MEMORY -> Icons.Default.Memory
    CognitiveCategory.SPEED -> Icons.Default.Bolt
    CognitiveCategory.PATTERN -> Icons.Default.GridOn
    CognitiveCategory.ATTENTION -> Icons.Default.CenterFocusStrong
    CognitiveCategory.STRATEGY -> Icons.Default.Extension
    CognitiveCategory.ADAPTABILITY -> Icons.Default.Autorenew
    CognitiveCategory.SPATIAL -> Icons.Default.ViewInAr
    CognitiveCategory.HUMAN_MIND -> Icons.Default.Groups
    CognitiveCategory.CREATIVITY -> Icons.Default.Lightbulb
  }

  Icon(
    imageVector = icon,
    contentDescription = category.displayName,
    tint = category.themeColor,
    modifier = modifier.size(size)
  )
}

@Composable
fun SIScoreBadge(
  siScore: Int,
  modifier: Modifier = Modifier
) {
  CortexCard(
    modifier = modifier,
    glow = true,
    backgroundColor = CortexSurfaceElevated
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        Text(
          text = "SANDLIP INTELLIGENCE",
          color = CortexTextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Text(
          text = "SI $siScore",
          color = CortexCyan,
          fontSize = 26.sp,
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.5.sp
        )
      }
      Icon(
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = null,
        tint = CortexCyan,
        modifier = Modifier.size(28.dp)
      )
    }
  }
}
