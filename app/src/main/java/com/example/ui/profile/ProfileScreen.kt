package com.example.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CortexCard
import com.example.ui.theme.CortexAmber
import com.example.ui.theme.CortexBorder
import com.example.ui.theme.CortexCyan
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.CortexEmerald
import com.example.ui.theme.CortexPrimary
import com.example.ui.theme.CortexSurface
import com.example.ui.theme.CortexSurfaceElevated
import com.example.ui.theme.CortexTextMuted
import com.example.ui.theme.CortexTextSecondary

@Composable
fun ProfileScreen(
  viewModel: ProfileViewModel,
  onNavigateToAchievements: () -> Unit,
  onNavigateToSettings: () -> Unit,
  onNavigateToPrivacy: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val user = uiState.user
  val brain = uiState.brainDNA

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CortexDeepVoid)
      .statusBarsPadding()
      .padding(horizontal = 16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = "OPERATIVE PROFILE",
      color = CortexCyan,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.5.sp
    )

    Text(
      text = user.username,
      color = Color.White,
      fontSize = 24.sp,
      fontWeight = FontWeight.Black
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Profile Card
    CortexCard(modifier = Modifier.fillMaxWidth(), backgroundColor = CortexSurfaceElevated, glow = true) {
      Row(
        modifier = Modifier.padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Brush.radialGradient(listOf(CortexCyan.copy(alpha = 0.4f), CortexPrimary.copy(alpha = 0.2f), Color.Transparent)))
            .border(1.5.dp, CortexCyan, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Psychology, contentDescription = null, tint = CortexCyan, modifier = Modifier.size(32.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = user.username,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Level ${user.level} • ${user.levelTitle}",
            color = CortexCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.height(6.dp))
          // XP Bar
          val xpFraction = (user.xpInCurrentLevel.toFloat() / user.xpForNextLevel.toFloat()).coerceIn(0f, 1f)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp))
              .background(CortexSurface)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth(fraction = xpFraction)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(CortexEmerald)
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${user.xpInCurrentLevel}/${user.xpForNextLevel} XP to Level ${user.level + 1}",
            color = CortexTextMuted,
            fontSize = 10.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Lifetime Stats Grid
    CortexCard(modifier = Modifier.fillMaxWidth()) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "LIFETIME COGNITIVE METRICS",
          color = CortexTextMuted,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Column {
            Text("SI SCORE", color = CortexTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("${brain.siScore}", color = CortexCyan, fontSize = 18.sp, fontWeight = FontWeight.Bold)
          }
          Column {
            Text("GAMES PLAYED", color = CortexTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("${brain.gamesPlayed}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
          }
          Column {
            Text("TOTAL ACCURACY", color = CortexTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            val acc = if (brain.gamesPlayed > 0) ((brain.totalCorrect.toFloat() / brain.gamesPlayed) * 100).toInt() else 0
            Text("$acc%", color = CortexEmerald, fontSize = 18.sp, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Column {
            Text("BEST STREAK", color = CortexTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("${brain.bestStreak}", color = CortexAmber, fontSize = 18.sp, fontWeight = FontWeight.Bold)
          }
          Column {
            Text("HARDEST SOLVED", color = CortexTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("Diff ${brain.hardestSolvedDifficulty}/10", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
          }
          Column {
            Text("FASTEST SOLVE", color = CortexTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("${if (brain.fastestResponseMs > 0) "${brain.fastestResponseMs}ms" else "--"}", color = CortexCyan, fontSize = 18.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Navigation Action Tiles
    CortexCard(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onNavigateToAchievements() }
        .testTag("btn_menu_achievements")
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = CortexAmber, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(12.dp))
          Text(text = "Achievements (${uiState.unlockedAchievementCount}/${uiState.achievements.size})", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = CortexTextMuted, modifier = Modifier.size(18.dp))
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    CortexCard(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onNavigateToSettings() }
        .testTag("btn_menu_settings")
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Settings, contentDescription = null, tint = CortexCyan, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(12.dp))
          Text(text = "Settings & AI Configuration", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = CortexTextMuted, modifier = Modifier.size(18.dp))
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    CortexCard(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onNavigateToPrivacy() }
        .testTag("btn_menu_privacy")
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Security, contentDescription = null, tint = CortexEmerald, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(12.dp))
          Text(text = "Privacy & Ethical Framework", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = CortexTextMuted, modifier = Modifier.size(18.dp))
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
