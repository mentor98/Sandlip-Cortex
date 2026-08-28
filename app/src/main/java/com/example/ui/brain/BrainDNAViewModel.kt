package com.example.ui.brain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.repository.CortexRepository
import com.example.domain.model.BrainDNA
import com.example.domain.model.BrainSnapshot
import com.example.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BrainDNAUiState(
  val user: UserProfile = UserProfile(),
  val brainDNA: BrainDNA = BrainDNA(),
  val snapshots: List<BrainSnapshot> = emptyList(),
  val cognitivePersona: String = "Neural Architect",
  val cognitiveSummary: String = "Balanced deductive clarity with strong adaptive speed.",
  val isLoading: Boolean = false
)

class BrainDNAViewModel(private val repository: CortexRepository) : ViewModel() {

  private val _uiState = MutableStateFlow(BrainDNAUiState())
  val uiState: StateFlow<BrainDNAUiState> = _uiState.asStateFlow()

  init {
    loadBrainDNA()
  }

  private fun loadBrainDNA() {
    viewModelScope.launch {
      combine(
        repository.userProfileFlow,
        repository.brainProfileFlow,
        repository.snapshotsFlow
      ) { user, brain, snapshots ->
        val safeUser = user ?: UserProfile()
        val safeBrain = brain ?: BrainDNA()

        val strongest = safeBrain.getStrongestCategory()
        val persona = when (strongest.first.code) {
          "LOG" -> "Analytical Deductor"
          "MEM" -> "Eidetic Archivist"
          "SPD" -> "Hyper-Velocity Reactor"
          "PAT" -> "Topological Pattern Seeker"
          "ATT" -> "Laser-Focus Sentinel"
          "STR" -> "Grand Strategist"
          "ADP" -> "Fluid Chameleon Mind"
          "SPA" -> "Spatial Dimensionalist"
          "HUM" -> "Empathetic Intuitive"
          "CRE" -> "Divergent Synthesizer"
          else -> "Neural Architect"
        }

        BrainDNAUiState(
          user = safeUser,
          brainDNA = safeBrain,
          snapshots = snapshots,
          cognitivePersona = persona,
          cognitiveSummary = "Primary domain: ${strongest.first.displayName} (${strongest.second}/100). SI Composite Score: ${safeBrain.siScore}.",
          isLoading = false
        )
      }.collect {
        _uiState.value = it
      }
    }
  }

  class Factory(private val repository: CortexRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return BrainDNAViewModel(repository) as T
    }
  }
}
