package com.example.game.engine

import com.example.domain.model.ChallengeResult

object AdaptiveDifficultyEngine {

  /**
   * Adapts next challenge difficulty (1 to 10) dynamically based on recent history.
   */
  fun calculateNextDifficulty(
    currentDifficulty: Int,
    recentResults: List<ChallengeResult>,
    categoryStreak: Int
  ): Int {
    if (recentResults.isEmpty()) return currentDifficulty.coerceIn(1, 10)

    val last3 = recentResults.take(3)
    val correctCount = last3.count { it.isCorrect }
    val avgSpeedMs = if (last3.isNotEmpty()) last3.map { it.responseTimeMs }.average() else 3000.0

    var targetDifficulty = currentDifficulty

    // Success streaks and fast reactions trigger difficulty elevations
    if (correctCount == 3 && avgSpeedMs < 3500) {
      targetDifficulty += 2
    } else if (correctCount >= 2 && categoryStreak >= 2) {
      targetDifficulty += 1
    } else if (correctCount == 0) {
      // 3 consecutive failures: decrease smoothly without overly punishing
      targetDifficulty -= 1
    } else if (last3.firstOrNull()?.isCorrect == false && avgSpeedMs > 8000) {
      // Slow and wrong: slight ease
      targetDifficulty -= 1
    }

    return targetDifficulty.coerceIn(1, 10)
  }
}
