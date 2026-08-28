package com.example.ui.gameplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiService
import com.example.ai.PromptManager
import com.example.data.repository.CortexRepository
import com.example.domain.model.Challenge
import com.example.domain.model.ChallengeEvaluation
import com.example.domain.model.ChallengeType
import com.example.domain.model.CognitiveCategory
import com.example.domain.model.CognitiveWorld
import com.example.domain.model.SessionSummary
import com.example.game.engine.ChallengeBank
import com.example.game.engine.ScoringEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameplayUiState(
  val worldId: String = "logic_lab",
  val mode: String = "WORLD",
  val challenges: List<Challenge> = emptyList(),
  val currentIndex: Int = 0,
  val currentChallenge: Challenge? = null,
  val remainingSeconds: Int = 15,
  val isTimerActive: Boolean = false,
  val startTimeMs: Long = 0L,
  // Matrix memory state
  val isMemoryRevealPhase: Boolean = false,
  val selectedMatrixNodes: List<Int> = emptyList(),
  // Creative text state
  val creativeInputText: String = "",
  val isEvaluatingCreative: Boolean = false,
  // Evaluation result state
  val currentEvaluation: ChallengeEvaluation? = null,
  val isEvaluating: Boolean = false,
  val showFeedbackSheet: Boolean = false,
  // Session accumulator
  val sessionCorrectCount: Int = 0,
  val sessionTotalXp: Int = 0,
  val sessionSiDelta: Int = 0,
  val responseTimes: List<Long> = emptyList(),
  val isSessionFinished: Boolean = false,
  val sessionSummary: SessionSummary? = null
)

class GameplayViewModel(
  private val repository: CortexRepository,
  private val worldId: String,
  private val mode: String
) : ViewModel() {

  private val _uiState = MutableStateFlow(GameplayUiState(worldId = worldId, mode = mode))
  val uiState: StateFlow<GameplayUiState> = _uiState.asStateFlow()

  private var timerJob: Job? = null

  init {
    loadChallenges()
  }

  private fun loadChallenges() {
    val world = CognitiveWorld.entries.find { it.id == worldId }
    val challenges = if (world != null) {
      ChallengeBank.getChallengesForWorld(world, count = 5)
    } else {
      val category = CognitiveCategory.fromCode(worldId)
      ChallengeBank.getChallengesForCategory(category, count = 5)
    }

    _uiState.update {
      it.copy(
        challenges = challenges,
        currentIndex = 0,
        currentChallenge = challenges.firstOrNull()
      )
    }

    startChallenge(challenges.firstOrNull())
  }

  private fun startChallenge(challenge: Challenge?) {
    if (challenge == null) return

    timerJob?.cancel()
    val isMatrix = challenge.type == ChallengeType.MATRIX_MEMORY

    _uiState.update {
      it.copy(
        currentChallenge = challenge,
        remainingSeconds = challenge.timeLimitSeconds,
        isTimerActive = true,
        startTimeMs = System.currentTimeMillis(),
        isMemoryRevealPhase = isMatrix,
        selectedMatrixNodes = emptyList(),
        creativeInputText = "",
        showFeedbackSheet = false,
        currentEvaluation = null
      )
    }

    if (isMatrix) {
      // Show pattern for 2.5 seconds, then enter recall mode
      viewModelScope.launch {
        delay(2500)
        _uiState.update { it.copy(isMemoryRevealPhase = false) }
        startTimer(challenge.timeLimitSeconds)
      }
    } else {
      startTimer(challenge.timeLimitSeconds)
    }
  }

  private fun startTimer(totalSeconds: Int) {
    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      for (sec in totalSeconds downTo 0) {
        _uiState.update { it.copy(remainingSeconds = sec) }
        if (sec == 0) {
          // Time expired -> evaluate as incorrect timeout
          handleTimeout()
          break
        }
        delay(1000)
      }
    }
  }

  fun onSelectOption(optionIndex: Int) {
    val current = _uiState.value
    val challenge = current.currentChallenge ?: return
    if (current.showFeedbackSheet) return

    timerJob?.cancel()
    val responseTime = System.currentTimeMillis() - current.startTimeMs
    val isCorrect = optionIndex == challenge.correctOptionIndex

    processEvaluation(
      isCorrect = isCorrect,
      responseTimeMs = responseTime,
      selectedOptionIndex = optionIndex,
      userText = null
    )
  }

  fun onToggleMatrixNode(nodeIndex: Int) {
    val current = _uiState.value
    if (current.isMemoryRevealPhase || current.showFeedbackSheet) return

    val currentSelected = current.selectedMatrixNodes.toMutableList()
    if (currentSelected.contains(nodeIndex)) {
      currentSelected.remove(nodeIndex)
    } else {
      currentSelected.add(nodeIndex)
    }

    _uiState.update { it.copy(selectedMatrixNodes = currentSelected) }
  }

  fun submitMatrixMemory() {
    val current = _uiState.value
    val challenge = current.currentChallenge ?: return
    if (current.showFeedbackSheet) return

    timerJob?.cancel()
    val responseTime = System.currentTimeMillis() - current.startTimeMs

    val targets = challenge.targetSequence.toSet()
    val selected = current.selectedMatrixNodes.toSet()
    val isCorrect = targets == selected

    processEvaluation(
      isCorrect = isCorrect,
      responseTimeMs = responseTime,
      selectedOptionIndex = 0,
      userText = "Nodes: ${selected.joinToString()}"
    )
  }

  fun setCreativeText(text: String) {
    _uiState.update { it.copy(creativeInputText = text) }
  }

  fun submitCreativeText() {
    val current = _uiState.value
    val challenge = current.currentChallenge ?: return
    if (current.creativeInputText.isBlank() || current.showFeedbackSheet) return

    timerJob?.cancel()
    val responseTime = System.currentTimeMillis() - current.startTimeMs
    _uiState.update { it.copy(isEvaluatingCreative = true) }

    viewModelScope.launch {
      val prompt = PromptManager.creativeEvaluationPrompt(challenge.prompt, current.creativeInputText)
      val result = GeminiService.instance.generateText(prompt, PromptManager.SYSTEM_COGNITIVE_DIRECTOR)

      val parsed = result.getOrNull()?.let { com.example.ai.AIResponseParser.parseCreativeEvaluation(it) }
      val isCorrect = (parsed?.score ?: 7) >= 6
      val customFeedback = parsed?.feedback ?: challenge.baseExplanation

      _uiState.update { it.copy(isEvaluatingCreative = false) }

      processEvaluation(
        isCorrect = isCorrect,
        responseTimeMs = responseTime,
        selectedOptionIndex = 0,
        userText = current.creativeInputText,
        overrideExplanation = customFeedback
      )
    }
  }

  private fun handleTimeout() {
    val current = _uiState.value
    val challenge = current.currentChallenge ?: return

    processEvaluation(
      isCorrect = false,
      responseTimeMs = challenge.timeLimitSeconds * 1000L,
      selectedOptionIndex = -1,
      userText = null,
      overrideExplanation = "Time expired before cognitive lock was achieved."
    )
  }

  private fun processEvaluation(
    isCorrect: Boolean,
    responseTimeMs: Long,
    selectedOptionIndex: Int,
    userText: String?,
    overrideExplanation: String? = null
  ) {
    val current = _uiState.value
    val challenge = current.currentChallenge ?: return

    val evaluation = ScoringEngine.evaluateSubmission(
      category = challenge.category,
      difficulty = challenge.difficulty,
      isCorrect = isCorrect,
      responseTimeMs = responseTimeMs,
      timeLimitSeconds = challenge.timeLimitSeconds,
      currentStreak = current.sessionCorrectCount,
      baseExplanation = overrideExplanation ?: challenge.baseExplanation
    )

    _uiState.update {
      it.copy(
        currentEvaluation = evaluation,
        showFeedbackSheet = true,
        sessionCorrectCount = it.sessionCorrectCount + (if (isCorrect) 1 else 0),
        sessionTotalXp = it.sessionTotalXp + evaluation.xpEarned,
        sessionSiDelta = it.sessionSiDelta + evaluation.siScoreDelta,
        responseTimes = it.responseTimes + responseTimeMs
      )
    }

    viewModelScope.launch {
      // Record outcome in local Room DB
      repository.recordChallengeOutcome(
        challengeId = challenge.id,
        category = challenge.category,
        difficulty = challenge.difficulty,
        isCorrect = isCorrect,
        responseTimeMs = responseTimeMs,
        timeLimitSeconds = challenge.timeLimitSeconds,
        selectedOptionIndex = selectedOptionIndex,
        textAnswer = userText,
        evaluation = evaluation,
        mode = current.mode
      )

      // Increment daily mission if applicable
      repository.incrementDailyMissionProgress(challenge.category)

      // Fetch AI explanation asynchronously if connected
      fetchGeminiExplanation(challenge, isCorrect, selectedOptionIndex)
    }
  }

  private suspend fun fetchGeminiExplanation(challenge: Challenge, isCorrect: Boolean, selectedOptionIndex: Int) {
    val selectedText = challenge.options.getOrNull(selectedOptionIndex) ?: "None"
    val correctText = challenge.options.getOrNull(challenge.correctOptionIndex) ?: "N/A"

    val prompt = PromptManager.explanationPrompt(
      category = challenge.category,
      question = challenge.prompt,
      options = challenge.options,
      correctAnswer = correctText,
      userAnswer = selectedText,
      wasCorrect = isCorrect
    )

    val response = GeminiService.instance.generateText(prompt, PromptManager.SYSTEM_COGNITIVE_DIRECTOR)
    val parsed = response.getOrNull()?.let { com.example.ai.AIResponseParser.parseExplanation(it) }

    if (parsed?.explanation != null) {
      _uiState.update {
        val currentEval = it.currentEvaluation ?: return@update it
        it.copy(
          currentEvaluation = currentEval.copy(
            explanation = parsed.explanation,
            insightMessage = parsed.insight ?: currentEval.insightMessage
          )
        )
      }
    }
  }

  fun nextChallenge() {
    val current = _uiState.value
    if (current.currentIndex + 1 < current.challenges.size) {
      val nextIdx = current.currentIndex + 1
      val nextChallenge = current.challenges[nextIdx]
      _uiState.update {
        it.copy(
          currentIndex = nextIdx,
          currentChallenge = nextChallenge,
          showFeedbackSheet = false
        )
      }
      startChallenge(nextChallenge)
    } else {
      // Session Completed!
      finishSession()
    }
  }

  private fun finishSession() {
    val current = _uiState.value
    val count = current.challenges.size
    val avgSpeed = if (current.responseTimes.isNotEmpty()) current.responseTimes.average().toLong() else 2500L
    val accuracy = if (count > 0) ((current.sessionCorrectCount.toFloat() / count) * 100).toInt() else 0

    val summary = SessionSummary(
      challengesCompleted = count,
      correctCount = current.sessionCorrectCount,
      averageResponseTimeMs = avgSpeed,
      totalXpEarned = current.sessionTotalXp,
      siScoreDelta = current.sessionSiDelta,
      categoryDeltas = emptyMap(),
      aiInsight = "Session concluded. Cognitive faculties adapted with $accuracy% accuracy."
    )

    _uiState.update {
      it.copy(
        isSessionFinished = true,
        sessionSummary = summary,
        showFeedbackSheet = false
      )
    }

    viewModelScope.launch {
      // Generate rich AI summary
      val world = CognitiveWorld.entries.find { it.id == worldId }
      val cat = world?.category ?: CognitiveCategory.LOGIC
      val aiSummary = com.example.game.engine.AIGameDirector.getSessionAIInsight(
        accuracy = accuracy,
        averageSpeedMs = avgSpeed,
        strongest = cat,
        weakest = cat,
        siDelta = current.sessionSiDelta
      )

      _uiState.update {
        it.copy(sessionSummary = summary.copy(aiInsight = aiSummary))
      }
    }
  }

  class Factory(
    private val repository: CortexRepository,
    private val worldId: String,
    private val mode: String
  ) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return GameplayViewModel(repository, worldId, mode) as T
    }
  }
}
