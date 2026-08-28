package com.example.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CortexCard
import com.example.ui.theme.CortexCyan
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.CortexEmerald
import com.example.ui.theme.CortexRose
import com.example.ui.theme.CortexSurfaceElevated
import com.example.ui.theme.CortexTextMuted
import com.example.ui.theme.CortexTextSecondary

@Composable
fun SettingsScreen(
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var aiExplanations by remember { mutableStateOf(true) }
  var hapticsEnabled by remember { mutableStateOf(true) }
  var soundEnabled by remember { mutableStateOf(true) }
  var reducedMotion by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CortexDeepVoid)
      .statusBarsPadding()
      .padding(horizontal = 16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onNavigateBack,
        modifier = Modifier.testTag("btn_settings_back")
      ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CortexTextMuted)
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "SETTINGS & COGNITIVE CONTROLS",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "AI COGNITIVE DIRECTOR",
      color = CortexCyan,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    CortexCard(modifier = Modifier.fillMaxWidth(), backgroundColor = CortexSurfaceElevated) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Gemini 3.5 Flash Explanations", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Generates adaptive deep puzzle explanations and rival dialogue", color = CortexTextMuted, fontSize = 11.sp)
          }
          Switch(
            checked = aiExplanations,
            onCheckedChange = { aiExplanations = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = CortexCyan)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      text = "FEEDBACK & HAPTICS",
      color = CortexCyan,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    CortexCard(modifier = Modifier.fillMaxWidth(), backgroundColor = CortexSurfaceElevated) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Tactile Neural Haptics", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Vibration feedback on correct/incorrect lock", color = CortexTextMuted, fontSize = 11.sp)
          }
          Switch(
            checked = hapticsEnabled,
            onCheckedChange = { hapticsEnabled = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = CortexCyan)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Synthesizer Audio Pulses", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Dynamic ambient frequencies during trials", color = CortexTextMuted, fontSize = 11.sp)
          }
          Switch(
            checked = soundEnabled,
            onCheckedChange = { soundEnabled = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = CortexCyan)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      text = "LOCAL DATA & CACHE",
      color = CortexCyan,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )

    Spacer(modifier = Modifier.height(8.dp))

    CortexCard(modifier = Modifier.fillMaxWidth()) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Local Room Database Engine", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("All telemetry, session logs, and Brain DNA profiles are preserved locally on this device.", color = CortexTextSecondary, fontSize = 12.sp)
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
