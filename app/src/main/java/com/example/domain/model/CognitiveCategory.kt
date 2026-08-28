package com.example.domain.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CategoryAdaptabilityColor
import com.example.ui.theme.CategoryAttentionColor
import com.example.ui.theme.CategoryCreativityColor
import com.example.ui.theme.CategoryHumanMindColor
import com.example.ui.theme.CategoryLogicColor
import com.example.ui.theme.CategoryMemoryColor
import com.example.ui.theme.CategoryPatternColor
import com.example.ui.theme.CategorySpatialColor
import com.example.ui.theme.CategorySpeedColor
import com.example.ui.theme.CategoryStrategyColor

enum class CognitiveCategory(
  val displayName: String,
  val code: String,
  val description: String,
  val iconName: String,
  val themeColor: Color
) {
  LOGIC(
    displayName = "Logic",
    code = "LOGIC",
    description = "Deductive reasoning, syllogisms, conditional logic, and sequence inference.",
    iconName = "Psychology",
    themeColor = CategoryLogicColor
  ),
  MEMORY(
    displayName = "Memory",
    code = "MEMORY",
    description = "Short-term visual retention, sequence reconstruction, and missing-element recall.",
    iconName = "Memory",
    themeColor = CategoryMemoryColor
  ),
  SPEED(
    displayName = "Neural Speed",
    code = "SPEED",
    description = "Rapid processing velocity, cognitive reaction times, and Stroop conflict resolution.",
    iconName = "Bolt",
    themeColor = CategorySpeedColor
  ),
  PATTERN(
    displayName = "Pattern Realm",
    code = "PATTERN",
    description = "Matrix transformations, non-verbal symbolic progressions, and rule inference.",
    iconName = "GridOn",
    themeColor = CategoryPatternColor
  ),
  ATTENTION(
    displayName = "Focus Chamber",
    code = "ATTENTION",
    description = "Selective attention, distraction filtering, and visual anomaly detection.",
    iconName = "CenterFocusStrong",
    themeColor = CategoryAttentionColor
  ),
  STRATEGY(
    displayName = "Strategist",
    code = "STRATEGY",
    description = "Risk vs. reward optimization, probabilistic decisions, and multi-step planning.",
    iconName = "Extension",
    themeColor = CategoryStrategyColor
  ),
  ADAPTABILITY(
    displayName = "Adaptability",
    code = "ADAPTABILITY",
    description = "Cognitive flexibility, spontaneous rule shifts, and mental paradigm changes.",
    iconName = "Autorenew",
    themeColor = CategoryAdaptabilityColor
  ),
  SPATIAL(
    displayName = "Spatial Core",
    code = "SPATIAL",
    description = "Mental rotation, 2D/3D topological projection, and spatial orientation.",
    iconName = "ViewInAr",
    themeColor = CategorySpatialColor
  ),
  HUMAN_MIND(
    displayName = "Human Mind",
    code = "HUMAN_MIND",
    description = "Social nuance reasoning, contextual subtext, and perspective decoding.",
    iconName = "Groups",
    themeColor = CategoryHumanMindColor
  ),
  CREATIVITY(
    displayName = "Creative Mind",
    code = "CREATIVITY",
    description = "Divergent thinking, lateral synthesis, and alternative utility ideation.",
    iconName = "Lightbulb",
    themeColor = CategoryCreativityColor
  );

  companion object {
    fun fromCode(code: String): CognitiveCategory {
      return entries.find { it.code.equals(code, ignoreCase = true) } ?: LOGIC
    }
  }
}
