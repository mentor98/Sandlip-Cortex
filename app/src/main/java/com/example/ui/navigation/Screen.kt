package com.example.ui.navigation

sealed class Screen(val route: String) {
  data object Onboarding : Screen("onboarding")
  data object BaselineAssessment : Screen("baseline_assessment")
  data object Home : Screen("home")
  data object PlayHub : Screen("play_hub")
  data object Gameplay : Screen("gameplay/{worldId}/{mode}") {
    fun createRoute(worldId: String, mode: String = "WORLD") = "gameplay/$worldId/$mode"
  }
  data object SessionSummary : Screen("session_summary")
  data object BrainDNA : Screen("brain_dna")
  data object MindEvolution : Screen("mind_evolution")
  data object Training : Screen("training/{categoryCode}") {
    fun createRoute(categoryCode: String) = "training/$categoryCode"
  }
  data object Arena : Screen("arena")
  data object AIRivalBattle : Screen("ai_rival_battle")
  data object OasisCommunity : Screen("oasis_community")
  data object Profile : Screen("profile")
  data object Achievements : Screen("achievements")
  data object Settings : Screen("settings")
  data object Privacy : Screen("privacy")
}

enum class BottomNavDestination(
  val route: String,
  val label: String,
  val iconName: String
) {
  HOME(Screen.Home.route, "HOME", "Home"),
  PLAY(Screen.PlayHub.route, "PLAY", "SportsEsports"),
  ARENA(Screen.Arena.route, "ARENA", "EmojiEvents"),
  BRAIN(Screen.BrainDNA.route, "BRAIN", "Psychology"),
  PROFILE(Screen.Profile.route, "PROFILE", "Person")
}
