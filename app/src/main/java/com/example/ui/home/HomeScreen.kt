package com.example.ui.home

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Timeline
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CognitiveWorld
import com.example.ui.components.BrainRadarChart
import com.example.ui.components.CategoryIcon
import com.example.ui.components.CortexCard
import com.example.ui.components.NeonButton
import com.example.ui.components.SIScoreBadge
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
import com.example.ui.theme.CortexTextPrimary
import com.example.ui.theme.CortexTextSecondary

@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  onNavigateToWorld: (String, String) -> Unit,
  onNavigateToBrainDNA: () -> Unit,
  onNavigateToAIRival: () -> Unit,
  onNavigateToPlayHub: () -> Unit,
  onNavigateToEvolution: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val user = uiState.user
  val brain = uiState.brainDNA
  val mission = uiState.dailyMission
  val rival = uiState.aiRival
  val rec = uiState.recommendation

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CortexDeepVoid)
      .statusBarsPadding()
      .padding(horizontal = 16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Header Operative Status
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "SANDLIP CORTEX",
          color = CortexCyan,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.5.sp
        )
        Text(
          text = "Operative ${user.username}",
          color = Color.White,
          fontSize = 22.sp,
          fontWeight = FontWeight.Black
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        // Streak Tag
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CortexSurfaceElevated)
            .border(1.dp, CortexBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = "Streak",
            tint = CortexRose,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${brain.currentStreak}",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Level Tag
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
              Brush.horizontalGradient(listOf(CortexPrimary, CortexCyan))
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text(
            text = "LVL ${user.level} ${user.levelTitle.uppercase()}",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // SI Score Card
    SIScoreBadge(siScore = brain.siScore, modifier = Modifier.fillMaxWidth())

    Spacer(modifier = Modifier.height(16.dp))

    // AI Game Director Recommendation Card
    if (rec != null) {
      CortexCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = CortexSurfaceElevated,
        glow = true
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = CortexCyan,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "AI GAME DIRECTOR",
                color = CortexCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
            }
            Text(
              text = "DIFF ${rec.targetDifficulty}/10",
              color = CortexEmerald,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = rec.modeTitle,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = rec.rationale,
            color = CortexTextSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          NeonButton(
            text = "START ADAPTIVE TRIAL",
            icon = Icons.Default.PlayArrow,
            onClick = {
              val world = CognitiveWorld.entries.find { it.category == rec.recommendedCategory } ?: CognitiveWorld.LOGIC_LAB
              onNavigateToWorld(world.id, "DIRECTOR")
            },
            modifier = Modifier.fillMaxWidth(),
            testTag = "btn_start_director_trial"
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Brain DNA Preview Card
    CortexCard(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onNavigateToBrainDNA() }
        .testTag("card_brain_dna_preview")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Psychology,
              contentDescription = null,
              tint = CortexPrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "BRAIN DNA PROFILE",
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
          }
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "View DNA",
            tint = CortexTextMuted,
            modifier = Modifier.size(18.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          BrainRadarChart(
            brainDNA = brain,
            modifier = Modifier
              .size(130.dp)
              .padding(4.dp)
          )

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            val strongest = brain.getStrongestCategory()
            val weakest = brain.getWeakestCategory()

            Text(
              text = "PRIMARY STRENGTH",
              color = CortexTextMuted,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              CategoryIcon(category = strongest.first, size = 14.dp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${strongest.first.displayName} (${strongest.second})",
                color = strongest.first.themeColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "OPTIMIZATION TARGET",
              color = CortexTextMuted,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              CategoryIcon(category = weakest.first, size = 14.dp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${weakest.first.displayName} (${weakest.second})",
                color = weakest.first.themeColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Daily Mission Card
    if (mission != null) {
      CortexCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (mission.isCompleted) CortexEmerald else CortexBorder
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              CategoryIcon(category = mission.category, size = 16.dp)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "DAILY MIND MISSION",
                color = mission.category.themeColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
            }
            if (mission.isCompleted) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CortexEmerald, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("CLAIMED", color = CortexEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            } else {
              Text(
                text = "${mission.currentProgress}/${mission.targetCount}",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = mission.title,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = mission.description,
            color = CortexTextSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )

          if (!mission.isCompleted) {
            Spacer(modifier = Modifier.height(12.dp))
            NeonButton(
              text = "EXECUTE MISSION",
              color = mission.category.themeColor,
              onClick = {
                val world = CognitiveWorld.entries.find { it.category == mission.category } ?: CognitiveWorld.MEMORY_VAULT
                onNavigateToWorld(world.id, "DAILY")
              },
              modifier = Modifier.fillMaxWidth(),
              testTag = "btn_execute_daily_mission"
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // AI Rival NEXUS Card
    CortexCard(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onNavigateToAIRival() }
        .testTag("card_ai_rival_nexus"),
      backgroundColor = CortexSurfaceVariant
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(CortexSurfaceElevated)
            .border(1.dp, CortexCyan, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.SmartToy,
            contentDescription = "Rival NEXUS",
            tint = CortexCyan,
            modifier = Modifier.size(24.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "RIVAL: ${rival.name}",
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "SI ${rival.siScore}",
              color = CortexCyan,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "“${rival.lastDialogue}”",
            color = CortexTextSecondary,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            maxLines = 2
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Secondary Hub Action Buttons
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      CortexCard(
        modifier = Modifier
          .weight(1f)
          .clickable { onNavigateToPlayHub() }
          .testTag("btn_quick_worlds")
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = null, tint = CortexCyan, modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.height(6.dp))
          Text("COGNITIVE WORLDS", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
      }

      CortexCard(
        modifier = Modifier
          .weight(1f)
          .clickable { onNavigateToEvolution() }
          .testTag("btn_quick_evolution")
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.Timeline, contentDescription = null, tint = CortexEmerald, modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.height(6.dp))
          Text("MIND EVOLUTION", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
