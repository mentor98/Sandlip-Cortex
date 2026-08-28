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
import com.example.ui.theme.CortexPurpleAccent

enum class CognitiveWorld(
  val id: String,
  val worldNumber: Int,
  val title: String,
  val category: CognitiveCategory?,
  val subtitle: String,
  val description: String,
  val themeColor: Color,
  val iconName: String,
  val isSpecialMode: Boolean = false,
  val difficultyTier: Int = 1
) {
  LOGIC_LAB(
    id = "world_logic",
    worldNumber = 1,
    title = "Logic Lab",
    category = CognitiveCategory.LOGIC,
    subtitle = "Deduction & Reasoning",
    description = "Syllogistic deductions, mathematical relationships, conditional chains, and elimination puzzles.",
    themeColor = CategoryLogicColor,
    iconName = "Psychology",
    difficultyTier = 1
  ),
  MEMORY_VAULT(
    id = "world_memory",
    worldNumber = 2,
    title = "Memory Vault",
    category = CognitiveCategory.MEMORY,
    subtitle = "Recall & Retention",
    description = "Ephemeral visual flashes, complex matrix reconstruction, and precision sequence recall.",
    themeColor = CategoryMemoryColor,
    iconName = "Memory",
    difficultyTier = 1
  ),
  NEURAL_SPEED(
    id = "world_speed",
    worldNumber = 3,
    title = "Neural Speed",
    category = CognitiveCategory.SPEED,
    subtitle = "Reaction & Velocity",
    description = "Millisecond-critical symbol classifications, Stroop conflict tests, and rapid matching.",
    themeColor = CategorySpeedColor,
    iconName = "Bolt",
    difficultyTier = 2
  ),
  PATTERN_REALM(
    id = "world_pattern",
    worldNumber = 4,
    title = "Pattern Realm",
    category = CognitiveCategory.PATTERN,
    subtitle = "Matrix Inferences",
    description = "Progressive spatial rules, multi-layer geometric shifts, and inductive logic sequences.",
    themeColor = CategoryPatternColor,
    iconName = "GridOn",
    difficultyTier = 2
  ),
  HUMAN_MIND(
    id = "world_human_mind",
    worldNumber = 5,
    title = "Human Mind",
    category = CognitiveCategory.HUMAN_MIND,
    subtitle = "Context & Nuance",
    description = "Perspective simulation, conversational subtext decoding, and ambiguity analysis.",
    themeColor = CategoryHumanMindColor,
    iconName = "Groups",
    difficultyTier = 2
  ),
  CREATIVE_MIND(
    id = "world_creative",
    worldNumber = 6,
    title = "Creative Mind",
    category = CognitiveCategory.CREATIVITY,
    subtitle = "Divergent Thinking",
    description = "Lateral problem solving, multi-utility ideation, and synthesis scored by AI.",
    themeColor = CategoryCreativityColor,
    iconName = "Lightbulb",
    difficultyTier = 3
  ),
  STRATEGIST(
    id = "world_strategist",
    worldNumber = 7,
    title = "Strategist",
    category = CognitiveCategory.STRATEGY,
    subtitle = "Risk & Payoff",
    description = "Probabilistic decision making, risk-reward trade-offs, and resource distribution.",
    themeColor = CategoryStrategyColor,
    iconName = "Extension",
    difficultyTier = 3
  ),
  SPATIAL_CORE(
    id = "world_spatial",
    worldNumber = 8,
    title = "Spatial Core",
    category = CognitiveCategory.SPATIAL,
    subtitle = "3D & Mental Rotation",
    description = "Isometric projections, mental foldings, topological transformations, and perspective locks.",
    themeColor = CategorySpatialColor,
    iconName = "ViewInAr",
    difficultyTier = 3
  ),
  FOCUS_CHAMBER(
    id = "world_focus",
    worldNumber = 9,
    title = "Focus Chamber",
    category = CognitiveCategory.ATTENTION,
    subtitle = "Selective Attention",
    description = "Distraction suppression, anomaly isolation, and continuous target vigilance under noise.",
    themeColor = CategoryAttentionColor,
    iconName = "CenterFocusStrong",
    difficultyTier = 4
  ),
  ADAPTATION_TRIAL(
    id = "world_adaptation",
    worldNumber = 10,
    title = "Adaptation Trial",
    category = CognitiveCategory.ADAPTABILITY,
    subtitle = "Dynamic Rule Switching",
    description = "Live rule inversions, cognitive agility stress-tests, and instant paradigm adaptations.",
    themeColor = CategoryAdaptabilityColor,
    iconName = "Autorenew",
    difficultyTier = 4
  );

  companion object {
    fun fromId(id: String): CognitiveWorld {
      return entries.find { it.id == id } ?: LOGIC_LAB
    }
  }
}

enum class SpecialGameMode(
  val id: String,
  val title: String,
  val subtitle: String,
  val description: String
) {
  BLACK_BOX(
    id = "mode_black_box",
    title = "Black Box Mode",
    subtitle = "Zero-Instruction Discovery",
    description = "No rules are provided upfront. Test hypotheses through observation and output mapping."
  ),
  SURVIVAL(
    id = "mode_survival",
    title = "Survival Mode",
    subtitle = "3 Strikes Adaptive Gauntlet",
    description = "Endless challenges scaling exponentially in difficulty until 3 errors occur."
  ),
  SPEED_SPRINT(
    id = "mode_speed_sprint",
    title = "Speed Sprint",
    subtitle = "60-Second Reaction Velocity",
    description = "Clear as many sub-second perceptual puzzles as possible in 60 continuous seconds."
  ),
  INFINITE_CORTEX(
    id = "mode_infinite",
    title = "Infinite Cortex",
    subtitle = "Autonomous AI Direction",
    description = "Endless stream orchestrated in real-time by the AI Game Director matching your Brain DNA."
  ),
  MYSTERY_TRIAL(
    id = "mode_mystery",
    title = "Mystery Protocol",
    subtitle = "Blind Cognitive Evaluation",
    description = "Challenges with unannounced domains. Infer the cognitive faculty tested while executing."
  )
}
