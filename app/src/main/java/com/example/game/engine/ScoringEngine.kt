package com.example.game.engine

import com.example.domain.model.ChallengeEvaluation
import com.example.domain.model.CognitiveCategory

object ScoringEngine {

  fun evaluateSubmission(
    category: CognitiveCategory,
    difficulty: Int,
    isCorrect: Boolean,
    responseTimeMs: Long,
    timeLimitSeconds: Int,
    currentStreak: Int,
    baseExplanation: String
  ): ChallengeEvaluation {

    val maxTimeMs = (timeLimitSeconds * 1000L).coerceAtLeast(1000L)
    val timeFraction = (1.0f - (responseTimeMs.toFloat() / maxTimeMs)).coerceIn(0.1f, 1.0f)

    val baseXP = if (isCorrect) (difficulty * 15) else 5
    val speedBonusXP = if (isCorrect) (timeFraction * 20).toInt() else 0
    val streakBonusXP = if (isCorrect) (currentStreak * 5).coerceAtMost(50) else 0

    val totalXP = baseXP + speedBonusXP + streakBonusXP

    val siDelta = if (isCorrect) {
      val baseDelta = (difficulty * 0.8f).toInt().coerceAtLeast(1)
      val speedAdd = if (timeFraction > 0.6f) 2 else 1
      baseDelta + speedAdd
    } else {
      -2
    }

    val categoryDelta = if (isCorrect) {
      (2 + (difficulty / 3)).coerceIn(2, 6)
    } else {
      -2
    }

    val insight = when {
      isCorrect && timeFraction > 0.75f -> "Rapid neural velocity: you solved this difficulty $difficulty puzzle with exceptional latency."
      isCorrect -> "Accurate pattern deduction under active cognitive constraints."
      else -> "Cognitive friction encountered. Review the rule decomposition to strengthen recall."
    }

    return ChallengeEvaluation(
      isCorrect = isCorrect,
      responseTimeMs = responseTimeMs,
      xpEarned = totalXP,
      siScoreDelta = siDelta,
      categoryDelta = categoryDelta,
      explanation = baseExplanation,
      insightMessage = insight
    )
  }
}
