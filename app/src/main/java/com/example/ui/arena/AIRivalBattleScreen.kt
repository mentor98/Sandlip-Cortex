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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CategoryIcon
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
fun AIRivalBattleScreen(
  viewModel: ArenaViewModel,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val challenge = uiState.battleRounds.getOrNull(uiState.currentRoundIndex)

  if (uiState.isBattleFinished) {
    BattleFinishedView(
      playerWon = uiState.playerWonBattle,
      playerScore = uiState.playerRoundScore,
      rivalScore = uiState.rivalRoundScore,
      onFinish = onNavigateBack
    )
    return
  }

  if (challenge == null) {
    // Show Battle Preparation screen
    Column(
      modifier = modifier
        .fillMaxSize()
        .background(CortexDeepVoid)
        .statusBarsPadding()
        .navigationBarsPadding()
        .padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
      ) {
        IconButton(onClick = onNavigateBack) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CortexTextMuted)
        }
      }

      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
          modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(CortexPrimary.copy(alpha = 0.2f))
            .border(2.dp, CortexCyan, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.FlashOn, contentDescription = null, tint = CortexCyan, modifier = Modifier.size(46.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = "NEURAL DUEL: YOU vs NEXUS",
          color = Color.White,
          fontSize = 20.sp,
          fontWeight = FontWeight.Black,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "5 synchronized cognitive rounds. Speed and accuracy determine match victor.",
          color = CortexTextSecondary,
          fontSize = 13.sp,
          textAlign = TextAlign.Center
        )
      }

      NeonButton(
        text = "COMMENCE NEURAL BATTLE",
        icon = Icons.Default.FlashOn,
        onClick = { viewModel.startRivalBattle() },
        modifier = Modifier.fillMaxWidth(),
        testTag = "btn_commence_battle"
      )
    }
    return
  }

  // Active Battle View
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CortexDeepVoid)
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    // Battle Match Scoreboard Header
    CortexCard(modifier = Modifier.fillMaxWidth(), backgroundColor = CortexSurfaceElevated, glow = true) {
      Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Player
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "YOU", color = CortexCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          Text(text = "${uiState.playerRoundScore}", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black)
        }

        // Versus & Round
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "ROUND ${uiState.currentRoundIndex + 1}/5", color = CortexTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          Text(text = "VS", color = CortexRose, fontSize = 16.sp, fontWeight = FontWeight.Black)
          Text(text = "${uiState.remainingSeconds}s", color = CortexAmber, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }

        // Rival
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "NEXUS", color = CortexRose, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          Text(text = "${uiState.rivalRoundScore}", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black)
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Rival Live Dialogue Box
    CortexCard(modifier = Modifier.fillMaxWidth(), backgroundColor = CortexSurfaceVariant) {
      Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.SmartToy, contentDescription = null, tint = CortexCyan, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "“${uiState.rivalDialogue}”",
          color = CortexTextSecondary,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Challenge Card
    CortexCard(modifier = Modifier.fillMaxWidth(), borderColor = challenge.category.themeColor) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            CategoryIcon(category = challenge.category, size = 16.dp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = challenge.category.displayName.uppercase(),
              color = challenge.category.themeColor,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Text(text = "DIFF ${challenge.difficulty}", color = CortexTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = challenge.prompt,
          color = Color.White,
          fontSize = 15.sp,
          lineHeight = 21.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Options
    challenge.options.forEachIndexed { index, option ->
      CortexCard(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
          .clickable {
            viewModel.submitPlayerBattleAnswer(index, 2000L)
          }
          .testTag("battle_opt_$index"),
        backgroundColor = CortexSurfaceElevated
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${'A' + index}.",
            color = CortexCyan,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = option,
            color = Color.White,
            fontSize = 14.sp
          )
        }
      }
    }
  }
}

@Composable
private fun BattleFinishedView(
  playerWon: Boolean,
  playerScore: Int,
  rivalScore: Int,
  onFinish: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(CortexDeepVoid)
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(
        imageVector = if (playerWon) Icons.Default.EmojiEvents else Icons.Default.SmartToy,
        contentDescription = null,
        tint = if (playerWon) CortexAmber else CortexRose,
        modifier = Modifier.size(64.dp)
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = if (playerWon) "SYNAPTIC VICTORY" else "NEXUS OUTPERFORMED",
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = if (playerWon) "You demonstrated superior cognitive deduction velocity." else "NEXUS identified patterns with higher latency efficiency.",
        color = CortexTextSecondary,
        fontSize = 13.sp,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(24.dp))

      CortexCard(modifier = Modifier.fillMaxWidth(), glow = playerWon) {
        Row(
          modifier = Modifier.padding(20.dp),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("YOU", color = CortexCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("$playerScore", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
          }
          Text("-", color = CortexTextMuted, fontSize = 24.sp)
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("NEXUS", color = CortexRose, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("$rivalScore", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
          }
        }
      }
    }

    NeonButton(
      text = "RETURN TO ARENA",
      onClick = onFinish,
      modifier = Modifier.fillMaxWidth(),
      testTag = "btn_finish_battle"
    )
  }
}
