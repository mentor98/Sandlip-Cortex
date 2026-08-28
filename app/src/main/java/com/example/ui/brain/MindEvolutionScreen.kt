package com.example.ui.brain

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Timeline
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
import com.example.domain.model.BrainSnapshot
import com.example.ui.components.CortexCard
import com.example.ui.theme.CortexCyan
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.CortexEmerald
import com.example.ui.theme.CortexSurfaceElevated
import com.example.ui.theme.CortexTextMuted
import com.example.ui.theme.CortexTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MindEvolutionScreen(
  viewModel: BrainDNAViewModel,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val snapshots = uiState.snapshots

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
        modifier = Modifier.testTag("btn_evolution_back")
      ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CortexTextMuted)
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "MIND EVOLUTION TIMELINE",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (snapshots.isEmpty()) {
      CortexCard(modifier = Modifier.fillMaxWidth()) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.Timeline, contentDescription = null, tint = CortexCyan, modifier = Modifier.size(36.dp))
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "INITIALIZING HISTORICAL EVOLUTION",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Complete cognitive challenges and daily drills to record Mind Snapshots over time.",
            color = CortexTextSecondary,
            fontSize = 12.sp
          )
        }
      }
    } else {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(snapshots.reversed()) { snapshot ->
          SnapshotItemCard(snapshot = snapshot)
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
      }
    }
  }
}

@Composable
private fun SnapshotItemCard(snapshot: BrainSnapshot) {
  val dateStr = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(snapshot.timestamp))

  CortexCard(
    modifier = Modifier.fillMaxWidth(),
    backgroundColor = CortexSurfaceElevated
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = snapshot.dayLabel.uppercase(),
          color = CortexCyan,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Text(
          text = "SI ${snapshot.siScore}",
          color = CortexEmerald,
          fontSize = 16.sp,
          fontWeight = FontWeight.Black
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = dateStr,
        color = CortexTextMuted,
        fontSize = 11.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Compact stat badges
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text("LOGIC", color = CortexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          Text("${snapshot.logic}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Column {
          Text("MEMORY", color = CortexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          Text("${snapshot.memory}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Column {
          Text("SPEED", color = CortexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          Text("${snapshot.speed}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Column {
          Text("PATTERN", color = CortexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          Text("${snapshot.pattern}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Column {
          Text("FOCUS", color = CortexTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          Text("${snapshot.attention}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
