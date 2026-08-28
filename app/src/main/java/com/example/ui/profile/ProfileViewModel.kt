package com.example.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.repository.CortexRepository
import com.example.domain.model.Achievement
import com.example.domain.model.AvatarType
import com.example.domain.model.BrainDNA
import com.example.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
  val user: UserProfile = UserProfile(),
  val brainDNA: BrainDNA = BrainDNA(),
  val achievements: List<Achievement> = emptyList(),
  val unlockedAchievementCount: Int = 0
)

class ProfileViewModel(private val repository: CortexRepository) : ViewModel() {

  private val _uiState = MutableStateFlow(ProfileUiState())
  val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

  init {
    loadProfile()
  }

  private fun loadProfile() {
    viewModelScope.launch {
      combine(
        repository.userProfileFlow,
        repository.brainProfileFlow,
        repository.achievementsFlow
      ) { user, brain, achievements ->
        val safeUser = user ?: UserProfile()
        val safeBrain = brain ?: BrainDNA()
        val unlockedCount = achievements.count { it.isUnlocked }

        ProfileUiState(
          user = safeUser,
          brainDNA = safeBrain,
          achievements = achievements,
          unlockedAchievementCount = unlockedCount
        )
      }.collect {
        _uiState.value = it
      }
    }
  }

  class Factory(private val repository: CortexRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return ProfileViewModel(repository) as T
    }
  }
}
