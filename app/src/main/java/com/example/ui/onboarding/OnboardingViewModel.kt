package com.example.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.repository.CortexRepository
import com.example.domain.model.AvatarType
import com.example.domain.model.BrainDNA
import com.example.domain.model.Challenge
import com.example.domain.model.CognitiveCategory
import com.example.game.engine.BrainDNAEngine
import com.example.game.engine.ChallengeBank
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingUiState(
  val stepIndex: Int = 0, // 0: Splash/Intro, 1: Persona/Type, 2: Username, 3: Avatar, 4: Privacy, 5: Baseline Ready
  val username: String = "Emmanuel",
  val selectedAvatar: AvatarType = AvatarType.CYBER_MIND,
  val baselineChallenges: List<Challenge> = emptyList(),
  val currentBaselineIndex: Int = 0,
  val baselineResults: MutableMap<CognitiveCategory, Boolean> = mutableMapOf(),
  val generatedBrainDNA: BrainDNA? = null,
  val isBaselineCompleted: Boolean = false,
  val isLoading: Boolean = false
)

class OnboardingViewModel(private val repository: CortexRepository) : ViewModel() {

  private val _uiState = MutableStateFlow(OnboardingUiState())
  val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

  init {
    loadBaseline()
  }

  private fun loadBaseline() {
    val challenges = ChallengeBank.getBaselineAssessment()
    _uiState.update { it.copy(baselineChallenges = challenges) }
  }

  fun nextStep() {
    _uiState.update { it.copy(stepIndex = it.stepIndex + 1) }
  }

  fun previousStep() {
    _uiState.update { it.copy(stepIndex = maxOf(0, it.stepIndex - 1)) }
  }

  fun setUsername(name: String) {
    _uiState.update { it.copy(username = name) }
  }

  fun setAvatar(avatar: AvatarType) {
    _uiState.update { it.copy(selectedAvatar = avatar) }
  }

  fun submitBaselineAnswer(isCorrect: Boolean) {
    val current = _uiState.value
    val challenge = current.baselineChallenges.getOrNull(current.currentBaselineIndex) ?: return

    val updatedMap = current.baselineResults.toMutableMap().apply {
      put(challenge.category, isCorrect)
    }

    if (current.currentBaselineIndex + 1 < current.baselineChallenges.size) {
      _uiState.update {
        it.copy(
          currentBaselineIndex = it.currentBaselineIndex + 1,
          baselineResults = updatedMap
        )
      }
    } else {
      // Completed all baseline tasks! Generate Brain DNA
      val generatedDNA = BrainDNAEngine.generateBaselineProfile(updatedMap)
      _uiState.update {
        it.copy(
          baselineResults = updatedMap,
          generatedBrainDNA = generatedDNA,
          isBaselineCompleted = true,
          stepIndex = 6 // Show First Brain DNA reveal screen
        )
      }

      viewModelScope.launch {
        repository.completeBaseline(
          username = current.username.ifBlank { "Emmanuel" },
          avatarKey = current.selectedAvatar.key,
          baselineDNA = generatedDNA
        )
      }
    }
  }

  class Factory(private val repository: CortexRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return OnboardingViewModel(repository) as T
    }
  }
}
