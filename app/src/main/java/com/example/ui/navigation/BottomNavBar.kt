package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CortexBorder
import com.example.ui.theme.CortexCyan
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.CortexPrimary
import com.example.ui.theme.CortexSurface
import com.example.ui.theme.CortexSurfaceElevated
import com.example.ui.theme.CortexTextMuted
import com.example.ui.theme.CortexTextSecondary

@Composable
fun CortexBottomNavBar(
  currentRoute: String?,
  onNavigate: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(CortexDeepVoid)
      .navigationBarsPadding()
      .padding(horizontal = 12.dp, vertical = 6.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(CortexSurface)
        .border(
          width = 1.dp,
          brush = Brush.horizontalGradient(
            listOf(CortexBorder, CortexPrimary.copy(alpha = 0.3f), CortexBorder)
          ),
          shape = RoundedCornerShape(20.dp)
        )
        .padding(horizontal = 6.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      BottomNavDestination.entries.forEach { item ->
        val isSelected = currentRoute == item.route

        val icon = when (item) {
          BottomNavDestination.HOME -> Icons.Default.Home
          BottomNavDestination.PLAY -> Icons.Default.SportsEsports
          BottomNavDestination.ARENA -> Icons.Default.EmojiEvents
          BottomNavDestination.BRAIN -> Icons.Default.Psychology
          BottomNavDestination.PROFILE -> Icons.Default.Person
        }

        val interactionSource = remember { MutableInteractionSource() }

        Box(
          modifier = Modifier
            .weight(1f)
            .height(50.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) CortexSurfaceElevated else Color.Transparent)
            .border(
              width = if (isSelected) 1.dp else 0.dp,
              color = if (isSelected) CortexCyan.copy(alpha = 0.5f) else Color.Transparent,
              shape = RoundedCornerShape(14.dp)
            )
            .clickable { if (!isSelected) onNavigate(item.route) }
            .testTag("nav_${item.label.lowercase()}"),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = item.label,
              tint = if (isSelected) CortexCyan else CortexTextMuted,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = item.label,
              color = if (isSelected) Color.White else CortexTextMuted,
              fontSize = 9.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              letterSpacing = 0.5.sp
            )
          }
        }
      }
    }
  }
}
