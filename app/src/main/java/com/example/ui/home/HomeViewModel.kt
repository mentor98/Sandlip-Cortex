package com.example.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.repository.CortexRepository
import com.example.domain.model.AIRival
import com.example.domain.model.BrainDNA
import com.example.domain.model.DailyMission
import com.example.domain.model.UserProfile
import com.example.game.engine.AIGameDirector
import com.example.game.engine.DirectorRecommendation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
  val user: UserProfile = UserProfile(),
  val brainDNA: BrainDNA = BrainDNA(),
  val dailyMission: DailyMission? = null,
  val aiRival: AIRival = AIRival(),
  val recommendation: DirectorRecommendation? = null,
  val isLoading: Boolean = false
)

class HomeViewModel(private val repository: CortexRepository) : ViewModel() {

  private val _uiState = MutableStateFlow(HomeUiState())
  val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      repository.initializeDefaultsIfNeeded()
      loadHomeData()
    }
  }

  fun loadHomeData() {
    viewModelScope.launch {
      val mission = repository.getDailyMission()
      combine(
        repository.userProfileFlow,
        repository.brainProfileFlow,
        repository.aiRivalFlow
      ) { user, brain, rival ->
        val safeUser = user ?: UserProfile()
        val safeBrain = brain ?: BrainDNA()
        val safeRival = rival ?: AIRival()
        val rec = AIGameDirector.generateRecommendation(safeBrain)

        HomeUiState(
          user = safeUser,
          brainDNA = safeBrain,
          dailyMission = mission,
          aiRival = safeRival,
          recommendation = rec,
          isLoading = false
        )
      }.collect { state ->
        _uiState.value = state
      }
    }
  }

  class Factory(private val repository: CortexRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return HomeViewModel(repository) as T
    }
  }
}
