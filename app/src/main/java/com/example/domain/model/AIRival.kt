package com.example.domain.model

data class AIRival(
  val id: String = "rival_nexus",
  val name: String = "NEXUS",
  val tagLine: String = "Your Adaptive Cognitive Rival",
  val avatarKey: String = "cyber_mind",
  val level: Int = 5,
  val siScore: Int = 740,
  val logic: Int = 78,
  val memory: Int = 72,
  val speed: Int = 84,
  val pattern: Int = 88,
  val attention: Int = 80,
  val strategy: Int = 76,
  val adaptability: Int = 85,
  val wins: Int = 0,
  val losses: Int = 0,
  val confidenceScore: Int = 75,
  val currentMood: String = "Analytical",
  val lastDialogue: String = "I have observed your pattern recognition latency. Let us see how you handle sudden rule inversions."
) {
  fun getCategoryStrength(): CognitiveCategory {
    val scores = listOf(
      CognitiveCategory.LOGIC to logic,
      CognitiveCategory.MEMORY to memory,
      CognitiveCategory.SPEED to speed,
      CognitiveCategory.PATTERN to pattern,
      CognitiveCategory.ATTENTION to attention,
      CognitiveCategory.STRATEGY to strategy,
      CognitiveCategory.ADAPTABILITY to adaptability
    )
    return scores.maxByOrNull { it.second }?.first ?: CognitiveCategory.PATTERN
  }
}

data class RivalMatchRound(
  val roundNumber: Int,
  val category: CognitiveCategory,
  val challenge: Challenge,
  val userCorrect: Boolean? = null,
  val userTimeMs: Long = 0L,
  val rivalCorrect: Boolean? = null,
  val rivalTimeMs: Long = 0L,
  val rivalDialogue: String = ""
)

data class RivalMatchResult(
  val userWins: Int,
  val rivalWins: Int,
  val userWonMatch: Boolean,
  val xpEarned: Int,
  val siDelta: Int,
  val rivalConfidenceDelta: Int,
  val victoryDialogue: String
)
