package com.example.domain.model

enum class ChallengeType {
  MULTIPLE_CHOICE,
  MATRIX_MEMORY,
  SEQUENCE_RECALL,
  STROOP_SPEED,
  PATTERN_MATRIX,
  RULE_SWITCH,
  RISK_PAYOFF,
  SPATIAL_ROTATION,
  ANOMALY_FOCUS,
  SOCIAL_NUANCE,
  CREATIVE_TEXT
}

data class Challenge(
  val id: String,
  val category: CognitiveCategory,
  val type: ChallengeType,
  val difficulty: Int, // 1 to 10
  val title: String,
  val prompt: String,
  val contextVisual: String? = null,
  val visualData: List<String> = emptyList(), // For grids, sequences, matrix cells, colors
  val visualGridSize: Int = 3, // For matrix memory / pattern grids (e.g. 3x3, 4x4)
  val targetSequence: List<Int> = emptyList(), // For memory sequence reproduction
  val ruleCondition: String? = null, // For rule-switch challenges
  val options: List<String> = emptyList(),
  val correctOptionIndex: Int = 0,
  val correctTextAnswer: String? = null,
  val timeLimitSeconds: Int = 15,
  val baseExplanation: String = ""
)

data class ChallengeResult(
  val id: Long = 0,
  val challengeId: String,
  val category: CognitiveCategory,
  val difficulty: Int,
  val isCorrect: Boolean,
  val responseTimeMs: Long,
  val scoreEarned: Int,
  val xpEarned: Int,
  val userSelectedOptionIndex: Int = -1,
  val userTextAnswer: String? = null,
  val explanation: String = "",
  val timestamp: Long = System.currentTimeMillis(),
  val mode: String = "STANDARD" // "WORLD", "DAILY", "ARENA", "RIVAL", "WEAKNESS", "SURVIVAL"
)

data class ChallengeEvaluation(
  val isCorrect: Boolean,
  val responseTimeMs: Long,
  val xpEarned: Int,
  val siScoreDelta: Int,
  val categoryDelta: Int,
  val explanation: String,
  val insightMessage: String? = null
)
