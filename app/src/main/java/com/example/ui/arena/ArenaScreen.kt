package com.example.ui.arena

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
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
import com.example.domain.model.LeaderboardItem
import com.example.ui.components.CortexCard
import com.example.ui.components.NeonButton
import com.example.ui.theme.CortexAmber
import com.example.ui.theme.CortexBorder
import com.example.ui.theme.CortexCyan
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.CortexEmerald
import com.example.ui.theme.CortexPrimary
import com.example.ui.theme.CortexRose
import com.example.ui.theme.CortexSurface
import com.example.ui.theme.CortexSurfaceElevated
import com.example.ui.theme.CortexSurfaceVariant
import com.example.ui.theme.CortexTextMuted
import com.example.ui.theme.CortexTextSecondary

@Composable
fun ArenaScreen(
  viewModel: ArenaViewModel,
  onStartRivalBattle: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val rival = uiState.aiRival
  val leaderboard = uiState.leaderboard

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CortexDeepVoid)
      .statusBarsPadding()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = "COGNITIVE ARENA",
      color = CortexCyan,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.5.sp
    )

    Text(
      text = "Competitive Oasis",
      color = Color.White,
      fontSize = 24.sp,
      fontWeight = FontWeight.Black
    )

    Spacer(modifier = Modifier.height(14.dp))

    // AI Rival Challenge Hero Card
    CortexCard(
      modifier = Modifier.fillMaxWidth(),
      borderColor = CortexCyan,
      backgroundColor = CortexSurfaceElevated,
      glow = true
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(CortexPrimary.copy(alpha = 0.2f))
                .border(1.dp, CortexCyan, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.SmartToy, contentDescription = null, tint = CortexCyan, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(text = "RIVAL: ${rival.name}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
              Text(text = rival.tagLine, color = CortexCyan, fontSize = 11.sp)
            }
          }
          Text(text = "SI ${rival.siScore}", color = CortexEmerald, fontSize = 16.sp, fontWeight = FontWeight.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "“${rival.lastDialogue}”",
          color = CortexTextSecondary,
          fontSize = 12.sp,
          lineHeight = 17.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        NeonButton(
          text = "ENTER 1v1 COGNITIVE DUEL",
          icon = Icons.Default.FlashOn,
          onClick = onStartRivalBattle,
          modifier = Modifier.fillMaxWidth(),
          testTag = "btn_start_rival_duel"
        )
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Leaderboard Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "GLOBAL OASIS RANKINGS",
        color = CortexTextMuted,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )
      Text(
        text = "TOP MINDS",
        color = CortexCyan,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(leaderboard) { item ->
        LeaderboardRow(item = item)
      }
      item { Spacer(modifier = Modifier.height(20.dp)) }
    }
  }
}

@Composable
private fun LeaderboardRow(item: LeaderboardItem) {
  val isTop3 = item.rank <= 3
  val rankColor = when (item.rank) {
    1 -> CortexAmber
    2 -> Color(0xFFC0C0C0)
    3 -> Color(0xFFCD7F32)
    else -> CortexTextMuted
  }

  CortexCard(
    modifier = Modifier.fillMaxWidth(),
    backgroundColor = if (item.isCurrentUser) CortexSurfaceElevated else CortexSurface,
    borderColor = if (item.isCurrentUser) CortexCyan else CortexBorder,
    glow = item.isCurrentUser
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Rank
      Box(
        modifier = Modifier.size(28.dp),
        contentAlignment = Alignment.Center
      ) {
        if (isTop3) {
          Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = rankColor, modifier = Modifier.size(20.dp))
        } else {
          Text(text = "#${item.rank}", color = rankColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.width(10.dp))

      // User info
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.username,
          color = if (item.isCurrentUser) CortexCyan else Color.White,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = item.tier,
          color = CortexTextMuted,
          fontSize = 11.sp
        )
      }

      // SI Score
      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "SI ${item.siScore}",
          color = Color.White,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = item.changeString,
          color = if (item.changeString.startsWith("+")) CortexEmerald else CortexRose,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
