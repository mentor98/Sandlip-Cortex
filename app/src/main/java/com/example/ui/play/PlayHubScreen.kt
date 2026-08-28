package com.example.ui.play

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CognitiveWorld
import com.example.domain.model.SpecialGameMode
import com.example.ui.components.CategoryIcon
import com.example.ui.components.CortexCard
import com.example.ui.theme.CortexAmber
import com.example.ui.theme.CortexBorder
import com.example.ui.theme.CortexCyan
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.CortexEmerald
import com.example.ui.theme.CortexPrimary
import com.example.ui.theme.CortexRose
import com.example.ui.theme.CortexSurface
import com.example.ui.theme.CortexSurfaceElevated
import com.example.ui.theme.CortexTextMuted
import com.example.ui.theme.CortexTextSecondary

@Composable
fun PlayHubScreen(
  onSelectWorld: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("COGNITIVE WORLDS", "SPECIAL MODES")

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CortexDeepVoid)
      .statusBarsPadding()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = "COGNITIVE MATRIX",
      color = CortexCyan,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.5.sp
    )

    Text(
      text = "Select Simulation",
      color = Color.White,
      fontSize = 24.sp,
      fontWeight = FontWeight.Black
    )

    Spacer(modifier = Modifier.height(12.dp))

    ScrollableTabRow(
      selectedTabIndex = selectedTab,
      containerColor = CortexSurface,
      contentColor = CortexCyan,
      edgePadding = 0.dp,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = CortexCyan,
          height = 2.dp
        )
      },
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .border(1.dp, CortexBorder, RoundedCornerShape(12.dp))
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              color = if (selectedTab == index) Color.White else CortexTextMuted,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (selectedTab == 0) {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(CognitiveWorld.entries) { world ->
          WorldItemCard(world = world, onClick = { onSelectWorld(world.id, "WORLD") })
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
      }
    } else {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(SpecialGameMode.entries) { mode ->
          SpecialModeCard(mode = mode, onClick = { onSelectWorld(mode.id, "SPECIAL") })
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
      }
    }
  }
}

@Composable
private fun WorldItemCard(
  world: CognitiveWorld,
  onClick: () -> Unit
) {
  val catColor = world.category?.themeColor ?: CortexCyan

  CortexCard(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("world_card_${world.id}"),
    borderColor = catColor.copy(alpha = 0.3f),
    backgroundColor = CortexSurfaceElevated
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(catColor.copy(alpha = 0.15f))
          .border(1.dp, catColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
      ) {
        if (world.category != null) {
          CategoryIcon(category = world.category, size = 26.dp)
        } else {
          Icon(Icons.Default.HelpOutline, contentDescription = null, tint = catColor, modifier = Modifier.size(26.dp))
        }
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = world.title.uppercase(),
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "TIER ${world.difficultyTier}",
            color = catColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = world.subtitle,
          color = catColor,
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = world.description,
          color = CortexTextSecondary,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = "Start World",
        tint = CortexTextMuted,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}

@Composable
private fun SpecialModeCard(
  mode: SpecialGameMode,
  onClick: () -> Unit
) {
  val (color, icon) = when (mode) {
    SpecialGameMode.BLACK_BOX -> Pair(CortexPrimary, Icons.Default.Lock)
    SpecialGameMode.SURVIVAL -> Pair(CortexRose, Icons.Default.Warning)
    SpecialGameMode.SPEED_SPRINT -> Pair(CortexAmber, Icons.Default.Bolt)
    SpecialGameMode.INFINITE_CORTEX -> Pair(CortexCyan, Icons.Default.AllInclusive)
    SpecialGameMode.MYSTERY_TRIAL -> Pair(CortexEmerald, Icons.Default.HelpOutline)
  }

  CortexCard(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("special_mode_${mode.id}"),
    borderColor = color.copy(alpha = 0.4f),
    backgroundColor = CortexSurfaceElevated
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(color.copy(alpha = 0.15f))
          .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = mode.title, tint = color, modifier = Modifier.size(24.dp))
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = mode.title.uppercase(),
          color = Color.White,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = mode.subtitle,
          color = color,
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = mode.description,
          color = CortexTextSecondary,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = "Start Special Mode",
        tint = CortexTextMuted,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
