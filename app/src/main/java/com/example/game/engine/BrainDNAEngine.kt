package com.example.game.engine

import com.example.domain.model.BrainDNA
import com.example.domain.model.CognitiveCategory

object BrainDNAEngine {

  /**
   * Updates the Brain DNA profile based on a new challenge outcome.
   * Uses an exponential moving average / smoothed delta to prevent wild swings.
   */
  fun calculateUpdatedProfile(
    currentProfile: BrainDNA,
    category: CognitiveCategory,
    difficulty: Int,
    isCorrect: Boolean,
    responseTimeMs: Long,
    timeLimitSeconds: Int
  ): Pair<BrainDNA, Int> { // Returns (UpdatedProfile, categoryDelta)

    val currentCatScore = currentProfile.getScore(category)

    // Performance factor based on correctness, difficulty, and reaction speed
    val timeFactor = if (isCorrect) {
      val maxMs = (timeLimitSeconds * 1000L).coerceAtLeast(1000L)
      val speedRatio = (1.0f - (responseTimeMs.toFloat() / maxMs)).coerceIn(0.1f, 1.0f)
      speedRatio * 1.5f
    } else {
      -1.0f
    }

    val difficultyWeight = (difficulty.toFloat() / 10f).coerceIn(0.2f, 1.0f)

    val rawDelta = if (isCorrect) {
      // Reward based on difficulty and speed (e.g. +1 to +5)
      ((1.5f + (difficultyWeight * 2.0f)) * (0.8f + (timeFactor * 0.4f))).toInt().coerceIn(1, 6)
    } else {
      // Soft penalty (e.g. -1 to -3), less severe for high difficulty
      (-3.0f * (1.2f - (difficultyWeight * 0.5f))).toInt().coerceIn(-4, -1)
    }

    val newCatScore = (currentCatScore + rawDelta).coerceIn(10, 99)

    // Construct updated profile
    val updatedLogic = if (category == CognitiveCategory.LOGIC) newCatScore else currentProfile.logic
    val updatedMemory = if (category == CognitiveCategory.MEMORY) newCatScore else currentProfile.memory
    val updatedSpeed = if (category == CognitiveCategory.SPEED) newCatScore else currentProfile.speed
    val updatedPattern = if (category == CognitiveCategory.PATTERN) newCatScore else currentProfile.pattern
    val updatedAttention = if (category == CognitiveCategory.ATTENTION) newCatScore else currentProfile.attention
    val updatedStrategy = if (category == CognitiveCategory.STRATEGY) newCatScore else currentProfile.strategy
    val updatedAdaptability = if (category == CognitiveCategory.ADAPTABILITY) newCatScore else currentProfile.adaptability
    val updatedSpatial = if (category == CognitiveCategory.SPATIAL) newCatScore else currentProfile.spatial
    val updatedHumanMind = if (category == CognitiveCategory.HUMAN_MIND) newCatScore else currentProfile.humanMind
    val updatedCreativity = if (category == CognitiveCategory.CREATIVITY) newCatScore else currentProfile.creativity

    val updatedGamesPlayed = currentProfile.gamesPlayed + 1
    val updatedTotalCorrect = currentProfile.totalCorrect + if (isCorrect) 1 else 0
    val updatedStreak = if (isCorrect) currentProfile.currentStreak + 1 else 0
    val updatedBestStreak = maxOf(currentProfile.bestStreak, updatedStreak)
    val updatedHardest = if (isCorrect) maxOf(currentProfile.hardestSolvedDifficulty, difficulty) else currentProfile.hardestSolvedDifficulty
    val updatedFastest = if (isCorrect && responseTimeMs > 0) {
      if (currentProfile.fastestResponseMs == 0L) responseTimeMs else minOf(currentProfile.fastestResponseMs, responseTimeMs)
    } else {
      currentProfile.fastestResponseMs
    }

    // Calculate composite Sandlip Intelligence (SI) Score (0 to 1000)
    val avgDimensionScore = (
      updatedLogic + updatedMemory + updatedSpeed + updatedPattern + updatedAttention +
      updatedStrategy + updatedAdaptability + updatedSpatial + updatedHumanMind + updatedCreativity
    ) / 10.0f

    val accuracyBonus = if (updatedGamesPlayed > 0) ((updatedTotalCorrect.toFloat() / updatedGamesPlayed) * 100f) else 50f
    val streakBonus = (updatedBestStreak * 3f).coerceAtMost(50f)

    val calculatedSIScore = ((avgDimensionScore * 7.5f) + (accuracyBonus * 1.5f) + streakBonus).toInt().coerceIn(100, 999)

    val updatedProfile = currentProfile.copy(
      logic = updatedLogic,
      memory = updatedMemory,
      speed = updatedSpeed,
      pattern = updatedPattern,
      attention = updatedAttention,
      strategy = updatedStrategy,
      adaptability = updatedAdaptability,
      spatial = updatedSpatial,
      humanMind = updatedHumanMind,
      creativity = updatedCreativity,
      siScore = calculatedSIScore,
      gamesPlayed = updatedGamesPlayed,
      totalCorrect = updatedTotalCorrect,
      fastestResponseMs = updatedFastest,
      currentStreak = updatedStreak,
      bestStreak = updatedBestStreak,
      hardestSolvedDifficulty = updatedHardest,
      lastPlayedTimestamp = System.currentTimeMillis()
    )

    return Pair(updatedProfile, rawDelta)
  }

  fun generateBaselineProfile(results: Map<CognitiveCategory, Boolean>): BrainDNA {
    var logic = 50
    var memory = 50
    var speed = 50
    var pattern = 50
    var attention = 50
    var strategy = 50
    var adaptability = 50
    var spatial = 50
    var humanMind = 50
    var creativity = 50
    var correctCount = 0

    results.forEach { (category, isCorrect) ->
      val score = if (isCorrect) 75 else 45
      if (isCorrect) correctCount++
      when (category) {
        CognitiveCategory.LOGIC -> logic = score
        CognitiveCategory.MEMORY -> memory = score
        CognitiveCategory.SPEED -> speed = score
        CognitiveCategory.PATTERN -> pattern = score
        CognitiveCategory.ATTENTION -> attention = score
        CognitiveCategory.STRATEGY -> strategy = score
        CognitiveCategory.ADAPTABILITY -> adaptability = score
        CognitiveCategory.SPATIAL -> spatial = score
        CognitiveCategory.HUMAN_MIND -> humanMind = score
        CognitiveCategory.CREATIVITY -> creativity = score
      }
    }

    val sum = logic + memory + speed + pattern + attention + strategy + adaptability + spatial + humanMind + creativity
    val avg = sum / 10.0f
    val si = (avg * 8.5f + (correctCount * 12)).toInt().coerceIn(400, 850)

    return BrainDNA(
      logic = logic,
      memory = memory,
      speed = speed,
      pattern = pattern,
      attention = attention,
      strategy = strategy,
      adaptability = adaptability,
      spatial = spatial,
      humanMind = humanMind,
      creativity = creativity,
      siScore = si,
      gamesPlayed = results.size,
      totalCorrect = correctCount,
      currentStreak = correctCount,
      bestStreak = correctCount
    )
  }
}
