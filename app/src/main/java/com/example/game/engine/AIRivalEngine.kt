package com.example.game.engine

import com.example.ai.GeminiService
import com.example.ai.PromptManager
import com.example.domain.model.AIRival
import com.example.domain.model.Challenge
import com.example.domain.model.CognitiveCategory
import kotlin.random.Random

object AIRivalEngine {

  /**
   * Simulates NEXUS's response to a challenge based on its current profile and category strength.
   */
  fun simulateRivalTurn(
    rival: AIRival,
    challenge: Challenge
  ): Pair<Boolean, Long> {
    val rivalCategoryScore = when (challenge.category) {
      CognitiveCategory.LOGIC -> rival.logic
      CognitiveCategory.MEMORY -> rival.memory
      CognitiveCategory.SPEED -> rival.speed
      CognitiveCategory.PATTERN -> rival.pattern
      CognitiveCategory.ATTENTION -> rival.attention
      CognitiveCategory.STRATEGY -> rival.strategy
      CognitiveCategory.ADAPTABILITY -> rival.adaptability
      else -> 75
    }

    // Probability of rival being correct scales with rival's category skill vs challenge difficulty
    val difficultyBurden = challenge.difficulty * 6
    val accuracyChance = (rivalCategoryScore - difficultyBurden + 30).coerceIn(40, 95)
    val isCorrect = Random.nextInt(100) < accuracyChance

    // Reaction time based on difficulty and speed faculty
    val baseMs = (challenge.timeLimitSeconds * 1000L * 0.45f).toLong()
    val variance = Random.nextLong(-800L, 1200L)
    val speedModifier = (100 - rival.speed) * 15L
    val responseTimeMs = (baseMs + variance + speedModifier).coerceIn(1200L, (challenge.timeLimitSeconds * 1000L) - 500L)

    return Pair(isCorrect, responseTimeMs)
  }

  suspend fun getRivalDialogue(
    rival: AIRival,
    playerSi: Int,
    category: CognitiveCategory,
    playerWonLast: Boolean,
    roundNumber: Int
  ): String {
    val prompt = PromptManager.rivalBanterPrompt(
      rivalName = rival.name,
      playerSi = playerSi,
      rivalSi = rival.siScore,
      category = category,
      playerWonLastRound = playerWonLast,
      roundNumber = roundNumber
    )

    val response = GeminiService.instance.generateText(prompt, PromptManager.SYSTEM_COGNITIVE_DIRECTOR)
    return response.getOrNull()?.let { raw ->
      com.example.ai.AIResponseParser.parseRivalDialogue(raw)?.dialogue
    } ?: getDefaultRivalDialogue(playerWonLast, category, roundNumber)
  }

  private fun getDefaultRivalDialogue(
    playerWonLast: Boolean,
    category: CognitiveCategory,
    roundNumber: Int
  ): String {
    return if (playerWonLast) {
      listOf(
        "Impressive latency on that ${category.displayName} puzzle. I am adjusting my prediction model.",
        "You found an optimal heuristic. Let us see how you handle this next trial.",
        "Sharp deduction. My synthetic synapses are recalibrating for Round $roundNumber."
      ).random()
    } else {
      listOf(
        "I detected cognitive friction in your response. ${category.displayName} remains my domain.",
        "My execution was 340ms faster on this sequence. Focus your attention.",
        "Pattern matched before your deadline expired. Next round will be more demanding."
      ).random()
    }
  }
}
