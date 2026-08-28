package com.example.ui.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun PrivacyScreen(
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
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
        modifier = Modifier.testTag("btn_privacy_back")
      ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CortexTextMuted)
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "PRIVACY & ETHICAL FRAMEWORK",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    CortexCard(modifier = Modifier.fillMaxWidth(), backgroundColor = CortexSurfaceElevated, borderColor = CortexRose) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("ENTERTAINMENT & GAME METRIC NOTICE", color = CortexRose, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Sandlip Cortex, Sandlip Intelligence (SI) score, and Brain DNA profiles are gameplay scoring mechanisms designed for cognitive entertainment, puzzle solving, and personal engagement. They do not constitute clinical psychological assessments, IQ measurements, or medical diagnoses.",
          color = CortexTextSecondary,
          fontSize = 12.sp,
          lineHeight = 17.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    CortexCard(modifier = Modifier.fillMaxWidth(), backgroundColor = CortexSurfaceElevated, borderColor = CortexEmerald) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("ON-DEVICE DATA OWNERSHIP", color = CortexEmerald, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Your telemetry data is stored locally in an encrypted Room SQLite database on your device. You retain full control over your cognitive profile history.",
          color = CortexTextSecondary,
          fontSize = 12.sp,
          lineHeight = 17.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
