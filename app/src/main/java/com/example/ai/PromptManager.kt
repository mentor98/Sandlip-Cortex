package com.example.ai

import com.example.domain.model.CognitiveCategory

object PromptManager {

  const val SYSTEM_COGNITIVE_DIRECTOR = """
You are the AI Cognitive Director for Sandlip Cortex, a futuristic cognitive gaming system.
You analyze gameplay behavior, generate novel cognitive challenges, provide concise educational explanations for puzzles, evaluate creative lateral thinking, and speak as NEXUS (an adaptive AI rival).
Always output strictly structured, valid JSON without Markdown blocks when requested.
Do not make medical, neurological, or clinical IQ claims. All scores are game metrics.
"""

  fun explanationPrompt(
    category: CognitiveCategory,
    question: String,
    options: List<String>,
    correctAnswer: String,
    userAnswer: String,
    wasCorrect: Boolean
  ): String {
    return """
Generate a concise, brilliant, 1-2 sentence cognitive explanation for this challenge.
Category: ${category.displayName}
Question: $question
Options: ${options.joinToString(", ")}
Correct Answer: $correctAnswer
Player selected: $userAnswer (Correct: $wasCorrect)

Return JSON in this format:
{
  "explanation": "concise explanation of why the correct answer is right and the underlying pattern",
  "insight": "brief cognitive takeaway or strategy tip"
}
"""
  }

  fun creativeEvaluationPrompt(
    prompt: String,
    userAnswer: String
  ): String {
    return """
Evaluate this player's response to a divergent creative problem challenge.
Problem: $prompt
Player Response: $userAnswer

Score the response between 1 and 10 based on:
1. Novelty (originality of ideas)
2. Flexibility (distinct conceptual categories)
3. Relevance (practical applicability)
4. Quantity of distinct ideas

Return JSON in this format:
{
  "score": 8,
  "novelty": 8,
  "flexibility": 7,
  "feedback": "Encouraging, analytical feedback highlighting the most creative idea.",
  "isAccepted": true
}
"""
  }

  fun rivalBanterPrompt(
    rivalName: String,
    playerSi: Int,
    rivalSi: Int,
    category: CognitiveCategory,
    playerWonLastRound: Boolean,
    roundNumber: Int
  ): String {
    return """
Generate a quick 1-sentence analytical and competitive remark from $rivalName, the AI Rival in Sandlip Cortex.
Context:
- Rival SI Score: $rivalSi, Player SI Score: $playerSi
- Round $roundNumber, Faculty tested: ${category.displayName}
- Did Player win previous round? $playerWonLastRound
Tone: Respectful, cybernetic, competitive, non-toxic, sharp.

Return JSON:
{
  "dialogue": "..."
}
"""
  }

  fun sessionInsightPrompt(
    accuracy: Int,
    averageSpeedMs: Long,
    strongestCategory: String,
    weakestCategory: String,
    siScoreDelta: Int
  ): String {
    return """
Generate an AI Game Director post-session analytical summary.
Metrics:
- Accuracy: $accuracy%
- Avg Response Time: ${averageSpeedMs}ms
- Top Faculty: $strongestCategory
- Weakest Faculty: $weakestCategory
- SI Delta: +$siScoreDelta

Return JSON:
{
  "summary": "1-2 sentence objective gameplay observation",
  "recommendation": "Targeted suggestion for next training session"
}
"""
  }
}
