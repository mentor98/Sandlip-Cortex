package com.example.ui.arena

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.repository.CortexRepository
import com.example.domain.model.AIRival
import com.example.domain.model.BrainDNA
import com.example.domain.model.Challenge
import com.example.domain.model.CognitiveCategory
import com.example.domain.model.LeaderboardItem
import com.example.domain.model.UserProfile
import com.example.game.engine.AIRivalEngine
import com.example.game.engine.ChallengeBank
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ArenaUiState(
  val user: UserProfile = UserProfile(),
  val brainDNA: BrainDNA = BrainDNA(),
  val aiRival: AIRival = AIRival(),
  val leaderboard: List<LeaderboardItem> = emptyList(),
  val selectedLeaderboardFilter: String = "GLOBAL",
  // Battle state
  val isBattleActive: Boolean = false,
  val battleRounds: List<Challenge> = emptyList(),
  val currentRoundIndex: Int = 0,
  val playerRoundScore: Int = 0,
  val rivalRoundScore: Int = 0,
  val rivalLastResponseTimeMs: Long = 0L,
  val rivalDialogue: String = "Commencing neural synchronization...",
  val isBattleFinished: Boolean = false,
  val playerWonBattle: Boolean = false,
  val remainingSeconds: Int = 12
)

class ArenaViewModel(private val repository: CortexRepository) : ViewModel() {

  private val _uiState = MutableStateFlow(ArenaUiState())
  val uiState: StateFlow<ArenaUiState> = _uiState.asStateFlow()

  private var timerJob: Job? = null

  init {
    loadArenaData()
  }

  private fun loadArenaData() {
    viewModelScope.launch {
      combine(
        repository.userProfileFlow,
        repository.brainProfileFlow,
        repository.aiRivalFlow,
        repository.getLeaderboardFlow("GLOBAL")
      ) { user, brain, rival, leaderboard ->
        _uiState.update {
          it.copy(
            user = user ?: UserProfile(),
            brainDNA = brain ?: BrainDNA(),
            aiRival = rival ?: AIRival(),
            leaderboard = leaderboard
          )
        }
      }.collect {}
    }
  }

  fun startRivalBattle() {
    val challenges = ChallengeBank.getAllChallenges().shuffled().take(5)
    _uiState.update {
      it.copy(
        isBattleActive = true,
        battleRounds = challenges,
        currentRoundIndex = 0,
        playerRoundScore = 0,
        rivalRoundScore = 0,
        isBattleFinished = false,
        rivalDialogue = "Neural match initiated. Let us measure your cognitive velocity."
      )
    }
    startBattleRound(challenges.firstOrNull())
  }

  private fun startBattleRound(challenge: Challenge?) {
    if (challenge == null) return

    timerJob?.cancel()
    _uiState.update { it.copy(remainingSeconds = challenge.timeLimitSeconds) }

    timerJob = viewModelScope.launch {
      for (sec in challenge.timeLimitSeconds downTo 0) {
        _uiState.update { it.copy(remainingSeconds = sec) }
        if (sec == 0) {
          submitPlayerBattleAnswer(-1, challenge.timeLimitSeconds * 1000L)
          break
        }
        delay(1000)
      }
    }
  }

  fun submitPlayerBattleAnswer(selectedOptionIndex: Int, responseTimeMs: Long) {
    timerJob?.cancel()
    val current = _uiState.value
    val challenge = current.battleRounds.getOrNull(current.currentRoundIndex) ?: return

    val playerCorrect = selectedOptionIndex == challenge.correctOptionIndex
    val (rivalCorrect, rivalMs) = AIRivalEngine.simulateRivalTurn(current.aiRival, challenge)

    val playerPoint = if (playerCorrect && (!rivalCorrect || responseTimeMs <= rivalMs)) 1 else 0
    val rivalPoint = if (rivalCorrect && (!playerCorrect || rivalMs < responseTimeMs)) 1 else 0

    val newPlayerScore = current.playerRoundScore + playerPoint
    val newRivalScore = current.rivalRoundScore + rivalPoint

    _uiState.update {
      it.copy(
        playerRoundScore = newPlayerScore,
        rivalRoundScore = newRivalScore,
        rivalLastResponseTimeMs = rivalMs
      )
    }

    viewModelScope.launch {
      val dialogue = AIRivalEngine.getRivalDialogue(
        rival = current.aiRival,
        playerSi = current.brainDNA.siScore,
        category = challenge.category,
        playerWonLast = playerPoint > rivalPoint,
        roundNumber = current.currentRoundIndex + 1
      )

      _uiState.update { it.copy(rivalDialogue = dialogue) }

      delay(1800)

      if (current.currentRoundIndex + 1 < current.battleRounds.size) {
        val nextIdx = current.currentRoundIndex + 1
        _uiState.update { it.copy(currentRoundIndex = nextIdx) }
        startBattleRound(current.battleRounds[nextIdx])
      } else {
        // Battle finished!
        val playerWon = newPlayerScore >= newRivalScore
        _uiState.update {
          it.copy(
            isBattleFinished = true,
            playerWonBattle = playerWon,
            isBattleActive = false
          )
        }

        repository.updateRivalOutcome(
          userWon = playerWon,
          xpEarned = if (playerWon) 250 else 50,
          siDelta = if (playerWon) 15 else -5,
          confidenceDelta = if (playerWon) -5 else +5
        )
      }
    }
  }

  fun exitBattle() {
    timerJob?.cancel()
    _uiState.update {
      it.copy(
        isBattleActive = false,
        isBattleFinished = false
      )
    }
  }

  class Factory(private val repository: CortexRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return ArenaViewModel(repository) as T
    }
  }
}
