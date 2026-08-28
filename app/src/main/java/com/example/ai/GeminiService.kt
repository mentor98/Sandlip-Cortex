package com.example.ai

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiGenerateRequest(
  val contents: List<GeminiContent>,
  val generationConfig: GeminiGenerationConfig? = null,
  val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
  val parts: List<GeminiPart>,
  val role: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
  val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
  val temperature: Float? = 0.7f,
  val topP: Float? = 0.95f,
  val topK: Int? = 40,
  val maxOutputTokens: Int? = 1024,
  val responseMimeType: String? = "application/json"
)

@JsonClass(generateAdapter = true)
data class GeminiGenerateResponse(
  val candidates: List<GeminiCandidate>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
  val content: GeminiContent? = null
)

interface GeminiApi {
  @POST("v1beta/models/gemini-3.5-flash:generateContent")
  suspend fun generateContent(
    @Query("key") apiKey: String,
    @Body request: GeminiGenerateRequest
  ): GeminiGenerateResponse
}

class GeminiService private constructor() {

  private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.NONE
  }

  private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .addInterceptor(loggingInterceptor)
    .build()

  private val retrofit = Retrofit.Builder()
    .baseUrl("https://generativelanguage.googleapis.com/")
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

  private val api = retrofit.create(GeminiApi::class.java)

  suspend fun generateText(prompt: String, systemInstruction: String? = null, asJson: Boolean = true): Result<String> =
    withContext(Dispatchers.IO) {
      val apiKey = BuildConfig.GEMINI_API_KEY
      if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
        return@withContext Result.failure(IllegalStateException("GEMINI_API_KEY is not configured"))
      }

      val request = GeminiGenerateRequest(
        contents = listOf(
          GeminiContent(parts = listOf(GeminiPart(text = prompt)))
        ),
        generationConfig = GeminiGenerationConfig(
          temperature = 0.7f,
          responseMimeType = if (asJson) "application/json" else "text/plain"
        ),
        systemInstruction = systemInstruction?.let {
          GeminiContent(parts = listOf(GeminiPart(text = it)))
        }
      )

      try {
        val response = api.generateContent(apiKey, request)
        val candidateText = response.candidates
          ?.firstOrNull()
          ?.content
          ?.parts
          ?.firstOrNull()
          ?.text

        if (!candidateText.isNullOrBlank()) {
          Result.success(candidateText)
        } else {
          Result.failure(IllegalStateException("Received empty candidate response from Gemini API"))
        }
      } catch (e: Exception) {
        Result.failure(e)
      }
    }

  companion object {
    val instance: GeminiService by lazy { GeminiService() }
  }
}
