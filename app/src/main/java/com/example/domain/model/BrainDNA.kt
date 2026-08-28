package com.example.domain.model

data class BrainDNA(
  val logic: Int = 50,
  val memory: Int = 50,
  val speed: Int = 50,
  val pattern: Int = 50,
  val attention: Int = 50,
  val strategy: Int = 50,
  val adaptability: Int = 50,
  val spatial: Int = 50,
  val humanMind: Int = 50,
  val creativity: Int = 50,
  val siScore: Int = 500,
  val gamesPlayed: Int = 0,
  val totalCorrect: Int = 0,
  val fastestResponseMs: Long = 0L,
  val currentStreak: Int = 0,
  val bestStreak: Int = 0,
  val hardestSolvedDifficulty: Int = 1,
  val lastPlayedTimestamp: Long = System.currentTimeMillis()
) {
  val accuracyPercentage: Int
    get() = if (gamesPlayed > 0) ((totalCorrect.toFloat() / gamesPlayed) * 100).toInt() else 0

  fun getScore(category: CognitiveCategory): Int {
    return when (category) {
      CognitiveCategory.LOGIC -> logic
      CognitiveCategory.MEMORY -> memory
      CognitiveCategory.SPEED -> speed
      CognitiveCategory.PATTERN -> pattern
      CognitiveCategory.ATTENTION -> attention
      CognitiveCategory.STRATEGY -> strategy
      CognitiveCategory.ADAPTABILITY -> adaptability
      CognitiveCategory.SPATIAL -> spatial
      CognitiveCategory.HUMAN_MIND -> humanMind
      CognitiveCategory.CREATIVITY -> creativity
    }
  }

  fun getStrongestCategory(): Pair<CognitiveCategory, Int> {
    val list = CognitiveCategory.entries.map { it to getScore(it) }
    return list.maxByOrNull { it.second } ?: (CognitiveCategory.LOGIC to logic)
  }

  fun getWeakestCategory(): Pair<CognitiveCategory, Int> {
    val list = CognitiveCategory.entries.map { it to getScore(it) }
    return list.minByOrNull { it.second } ?: (CognitiveCategory.MEMORY to memory)
  }
}

data class BrainSnapshot(
  val id: Long = 0,
  val dayLabel: String,
  val timestamp: Long,
  val siScore: Int,
  val logic: Int,
  val memory: Int,
  val speed: Int,
  val pattern: Int,
  val attention: Int,
  val strategy: Int,
  val adaptability: Int,
  val spatial: Int,
  val humanMind: Int,
  val creativity: Int
)
