package com.example.ui.brain

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timeline
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
import com.example.domain.model.CognitiveCategory
import com.example.ui.components.BrainDNABarRow
import com.example.ui.components.BrainRadarChart
import com.example.ui.components.CortexCard
import com.example.ui.components.NeonButton
import com.example.ui.components.SIScoreBadge
import com.example.ui.theme.CortexCyan
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.CortexEmerald
import com.example.ui.theme.CortexPrimary
import com.example.ui.theme.CortexSurface
import com.example.ui.theme.CortexSurfaceElevated
import com.example.ui.theme.CortexTextMuted
import com.example.ui.theme.CortexTextSecondary

@Composable
fun BrainDNAScreen(
  viewModel: BrainDNAViewModel,
  onNavigateToEvolution: () -> Unit,
  onTrainCategory: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
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
      text = "COGNITIVE ARCHITECTURE",
      color = CortexCyan,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.5.sp
    )

    Text(
      text = "Brain DNA",
      color = Color.White,
      fontSize = 24.sp,
      fontWeight = FontWeight.Black
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Top Persona Card
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
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CortexCyan, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("COGNITIVE PERSONA", color = CortexCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
          }
          Text("SI ${brain.siScore}", color = CortexEmerald, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = uiState.cognitivePersona,
          color = Color.White,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = uiState.cognitiveSummary,
          color = CortexTextSecondary,
          fontSize = 12.sp,
          lineHeight = 17.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Interactive Radar Chart
    CortexCard(modifier = Modifier.fillMaxWidth()) {
      Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "10-DIMENSIONAL COGNITIVE RADAR",
          color = CortexTextMuted,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        BrainRadarChart(
          brainDNA = brain,
          modifier = Modifier
            .size(260.dp)
            .padding(12.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Evolution Progression Shortcut
    CortexCard(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onNavigateToEvolution() }
        .testTag("btn_view_mind_evolution")
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Timeline, contentDescription = null, tint = CortexEmerald, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text("MIND EVOLUTION TIMELINE", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("Track historical progress & session snapshots", color = CortexTextMuted, fontSize = 11.sp)
          }
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = CortexTextMuted, modifier = Modifier.size(18.dp))
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Detailed 10 Dimension Bars
    Text(
      text = "FACULTY BREAKDOWN",
      color = CortexCyan,
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    CortexCard(modifier = Modifier.fillMaxWidth()) {
      Column(modifier = Modifier.padding(16.dp)) {
        CognitiveCategory.entries.forEach { category ->
          BrainDNABarRow(
            category = category,
            score = brain.getScore(category),
            modifier = Modifier.clickable { onTrainCategory(category.code) }
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
