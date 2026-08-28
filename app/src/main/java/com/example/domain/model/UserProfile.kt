package com.example.domain.model

data class DailyMission(
  val id: String,
  val dateString: String,
  val title: String,
  val category: CognitiveCategory,
  val targetCount: Int = 3,
  val currentProgress: Int = 0,
  val description: String,
  val xpReward: Int = 100,
  val brainPointsReward: Int = 5,
  val arenaRatingReward: Int = 15,
  val isCompleted: Boolean = false,
  val completedAt: Long? = null
)

data class UserProfile(
  val id: String = "local_user_1",
  val username: String = "Emmanuel",
  val email: String = "emmanuel@sandlip.io",
  val avatarKey: String = "cyber_mind",
  val level: Int = 1,
  val xp: Int = 0,
  val siScore: Int = 500,
  val isGuest: Boolean = false,
  val createdTimestamp: Long = System.currentTimeMillis()
) {
  val levelTitle: String
    get() = when (level) {
      1 -> "Initiate"
      2 -> "Observer"
      3 -> "Thinker"
      4 -> "Solver"
      5 -> "Strategist"
      6 -> "Analyst"
      7 -> "Architect"
      8 -> "Mastermind"
      9 -> "Cortex"
      10 -> "Sandlip Mind"
      else -> "Transcendence ${level - 10}"
    }

  val xpForNextLevel: Int
    get() = level * 200

  val xpInCurrentLevel: Int
    get() = xp % (level * 200)
}

enum class AvatarType(
  val key: String,
  val title: String,
  val subtitle: String,
  val iconName: String
) {
  HUMAN("human", "Human", "Organic biological cognition", "Person"),
  ANDROID("android", "Android", "Synthetic logical processor", "SmartToy"),
  CYBER_MIND("cyber_mind", "Cyber Mind", "Interconnected neural construct", "Psychology"),
  NEURAL_ENTITY("neural_entity", "Neural Entity", "Quantum synapse matrix", "Grain"),
  EXPLORER("explorer", "Explorer", "Cognitive frontier discoverer", "Explore"),
  ARCHITECT("architect", "Architect", "Systematic mind builder", "DesignServices")
}

data class LeaderboardItem(
  val rank: Int,
  val username: String,
  val avatarKey: String,
  val siScore: Int,
  val tier: String = "Cortex Elite",
  val isCurrentUser: Boolean = false,
  val changeString: String = "+12"
)

data class SessionSummary(
  val challengesCompleted: Int,
  val correctCount: Int,
  val averageResponseTimeMs: Long,
  val totalXpEarned: Int,
  val siScoreDelta: Int,
  val categoryDeltas: Map<CognitiveCategory, Int>,
  val aiInsight: String,
  val timestamp: Long = System.currentTimeMillis()
)
