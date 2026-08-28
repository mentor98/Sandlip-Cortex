package com.example.domain.model

data class Achievement(
  val id: String,
  val title: String,
  val description: String,
  val category: String,
  val xpReward: Int,
  val iconName: String,
  val isUnlocked: Boolean = false,
  val currentProgress: Int = 0,
  val maxProgress: Int = 1,
  val unlockedTimestamp: Long? = null
)

val BuiltInAchievements = listOf(
  Achievement(
    id = "first_thought",
    title = "FIRST THOUGHT",
    description = "Complete your first cognitive challenge in the Cortex.",
    category = "GENERAL",
    xpReward = 50,
    iconName = "Lightbulb",
    maxProgress = 1
  ),
  Achievement(
    id = "logic_architect",
    title = "LOGIC ARCHITECT",
    description = "Solve 15 deductive logic and mathematical challenges.",
    category = "LOGIC",
    xpReward = 150,
    iconName = "Psychology",
    maxProgress = 15
  ),
  Achievement(
    id = "memory_matrix",
    title = "MEMORY ARCHITECT",
    description = "Complete 15 visual flash matrix recall challenges with 100% accuracy.",
    category = "MEMORY",
    xpReward = 150,
    iconName = "Memory",
    maxProgress = 15
  ),
  Achievement(
    id = "speed_demon",
    title = "SPEED DEMON",
    description = "Solve a difficulty 7+ challenge with a response time under 1.5 seconds.",
    category = "SPEED",
    xpReward = 200,
    iconName = "Bolt",
    maxProgress = 1
  ),
  Achievement(
    id = "pattern_master",
    title = "PATTERN MASTER",
    description = "Successfully solve 25 symbolic matrix pattern challenges.",
    category = "PATTERN",
    xpReward = 250,
    iconName = "GridOn",
    maxProgress = 25
  ),
  Achievement(
    id = "adaptive_mind",
    title = "ADAPTIVE MIND",
    description = "Adapt instantly to 10 mid-game rule inversion triggers.",
    category = "ADAPTABILITY",
    xpReward = 200,
    iconName = "Autorenew",
    maxProgress = 10
  ),
  Achievement(
    id = "nexus_breaker",
    title = "NEXUS BREAKER",
    description = "Defeat the AI Rival NEXUS in a 5-round competitive cognitive battle.",
    category = "RIVAL",
    xpReward = 300,
    iconName = "SmartToy",
    maxProgress = 1
  ),
  Achievement(
    id = "oasis_champion",
    title = "OASIS CHAMPION",
    description = "Attain an SI Game Score of 800+ across all 10 Brain DNA dimensions.",
    category = "RANKING",
    xpReward = 500,
    iconName = "EmojiEvents",
    maxProgress = 1
  ),
  Achievement(
    id = "streak_titan",
    title = "STREAK TITAN",
    description = "Maintain a 10-challenge continuous perfection streak.",
    category = "STREAK",
    xpReward = 250,
    iconName = "LocalFireDepartment",
    maxProgress = 10
  ),
  Achievement(
    id = "divergent_thinker",
    title = "DIVERGENT THINKER",
    description = "Submit 5 high-novelty creative synthesis solutions.",
    category = "CREATIVITY",
    xpReward = 200,
    iconName = "AutoAwesome",
    maxProgress = 5
  )
)
