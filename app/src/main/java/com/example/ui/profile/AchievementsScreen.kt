package com.example.ui.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Achievement
import com.example.ui.components.CortexCard
import com.example.ui.theme.CortexAmber
import com.example.ui.theme.CortexBorder
import com.example.ui.theme.CortexCyan
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.CortexEmerald
import com.example.ui.theme.CortexSurface
import com.example.ui.theme.CortexSurfaceElevated
import com.example.ui.theme.CortexTextMuted
import com.example.ui.theme.CortexTextSecondary

@Composable
fun AchievementsScreen(
  viewModel: ProfileViewModel,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val achievements = uiState.achievements

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CortexDeepVoid)
      .statusBarsPadding()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onNavigateBack,
        modifier = Modifier.testTag("btn_achievements_back")
      ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CortexTextMuted)
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "COGNITIVE ACHIEVEMENTS",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(achievements) { achievement ->
        AchievementItemCard(achievement = achievement)
      }
      item { Spacer(modifier = Modifier.height(20.dp)) }
    }
  }
}

@Composable
private fun AchievementItemCard(achievement: Achievement) {
  CortexCard(
    modifier = Modifier.fillMaxWidth(),
    backgroundColor = if (achievement.isUnlocked) CortexSurfaceElevated else CortexSurface,
    borderColor = if (achievement.isUnlocked) CortexAmber.copy(alpha = 0.5f) else CortexBorder,
    glow = achievement.isUnlocked
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(if (achievement.isUnlocked) CortexAmber.copy(alpha = 0.2f) else CortexSurface),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (achievement.isUnlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
          contentDescription = null,
          tint = if (achievement.isUnlocked) CortexAmber else CortexTextMuted,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = achievement.title,
            color = if (achievement.isUnlocked) Color.White else CortexTextMuted,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "+${achievement.xpReward} XP",
            color = CortexCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = achievement.description,
          color = CortexTextSecondary,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )
      }
    }
  }
}
