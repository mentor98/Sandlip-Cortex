package com.example.ai

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@JsonClass(generateAdapter = true)
data class AIExplanationResponse(
  val explanation: String? = null,
  val insight: String? = null
)

@JsonClass(generateAdapter = true)
data class AICreativeEvaluationResponse(
  val score: Int? = 7,
  val novelty: Int? = 7,
  val flexibility: Int? = 7,
  val feedback: String? = null,
  val isAccepted: Boolean? = true
)

@JsonClass(generateAdapter = true)
data class AIRivalDialogueResponse(
  val dialogue: String? = null
)

@JsonClass(generateAdapter = true)
data class AISessionInsightResponse(
  val summary: String? = null,
  val recommendation: String? = null
)

object AIResponseParser {

  private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  private fun cleanJson(raw: String): String {
    var cleaned = raw.trim()
    if (cleaned.startsWith("```json")) {
      cleaned = cleaned.removePrefix("```json").trim()
    } else if (cleaned.startsWith("```")) {
      cleaned = cleaned.removePrefix("```").trim()
    }
    if (cleaned.endsWith("```")) {
      cleaned = cleaned.removeSuffix("```").trim()
    }
    return cleaned
  }

  fun parseExplanation(raw: String): AIExplanationResponse? {
    return try {
      val json = cleanJson(raw)
      val adapter = moshi.adapter(AIExplanationResponse::class.java)
      adapter.fromJson(json)
    } catch (e: Exception) {
      null
    }
  }

  fun parseCreativeEvaluation(raw: String): AICreativeEvaluationResponse? {
    return try {
      val json = cleanJson(raw)
      val adapter = moshi.adapter(AICreativeEvaluationResponse::class.java)
      adapter.fromJson(json)
    } catch (e: Exception) {
      null
    }
  }

  fun parseRivalDialogue(raw: String): AIRivalDialogueResponse? {
    return try {
      val json = cleanJson(raw)
      val adapter = moshi.adapter(AIRivalDialogueResponse::class.java)
      adapter.fromJson(json)
    } catch (e: Exception) {
      null
    }
  }

  fun parseSessionInsight(raw: String): AISessionInsightResponse? {
    return try {
      val json = cleanJson(raw)
      val adapter = moshi.adapter(AISessionInsightResponse::class.java)
      adapter.fromJson(json)
    } catch (e: Exception) {
      null
    }
  }
}
