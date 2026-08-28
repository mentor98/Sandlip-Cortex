package com.example.game.engine

import com.example.ai.GeminiService
import com.example.ai.PromptManager
import com.example.domain.model.BrainDNA
import com.example.domain.model.Challenge
import com.example.domain.model.CognitiveCategory

data class DirectorRecommendation(
  val recommendedCategory: CognitiveCategory,
  val targetDifficulty: Int,
  val rationale: String,
  val modeTitle: String
)

object AIGameDirector {

  fun generateRecommendation(brainDNA: BrainDNA): DirectorRecommendation {
    val weakest = brainDNA.getWeakestCategory()
    val strongest = brainDNA.getStrongestCategory()

    // 60% prioritize weakest category for balanced cognitive evolution, 40% strongest for flow state
    val targetCategory = if (brainDNA.gamesPlayed % 3 != 0) weakest.first else strongest.first
    val currentScore = brainDNA.getScore(targetCategory)
    val targetDifficulty = ((currentScore / 10) + 1).coerceIn(2, 9)

    val rationale = if (targetCategory == weakest.first) {
      "${weakest.first.displayName} is currently at $currentScore. Targeted training will calibrate neural retention and optimize your composite SI Score."
    } else {
      "You are excelling in ${strongest.first.displayName} ($currentScore). Advancing to difficulty $targetDifficulty to stress-test your upper limit."
    }

    return DirectorRecommendation(
      recommendedCategory = targetCategory,
      targetDifficulty = targetDifficulty,
      rationale = rationale,
      modeTitle = if (targetCategory == weakest.first) "Targeted Weakness Drill" else "Peak Strength Calibration"
    )
  }

  suspend fun getSessionAIInsight(
    accuracy: Int,
    averageSpeedMs: Long,
    strongest: CognitiveCategory,
    weakest: CognitiveCategory,
    siDelta: Int
  ): String {
    val prompt = PromptManager.sessionInsightPrompt(
      accuracy = accuracy,
      averageSpeedMs = averageSpeedMs,
      strongestCategory = strongest.displayName,
      weakestCategory = weakest.displayName,
      siScoreDelta = siDelta
    )

    val result = GeminiService.instance.generateText(prompt, PromptManager.SYSTEM_COGNITIVE_DIRECTOR)
    return result.getOrNull()?.let { raw ->
      com.example.ai.AIResponseParser.parseSessionInsight(raw)?.summary
    } ?: "Gameplay analysis: High consistency observed in ${strongest.displayName}. Continue training ${weakest.displayName} to elevate global cognitive synergy."
  }
}
