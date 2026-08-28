package com.example.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.repository.CortexRepository
import com.example.ui.arena.AIRivalBattleScreen
import com.example.ui.arena.ArenaScreen
import com.example.ui.arena.ArenaViewModel
import com.example.ui.brain.BrainDNAScreen
import com.example.ui.brain.BrainDNAViewModel
import com.example.ui.brain.MindEvolutionScreen
import com.example.ui.gameplay.GameplayScreen
import com.example.ui.gameplay.GameplayViewModel
import com.example.ui.home.HomeScreen
import com.example.ui.home.HomeViewModel
import com.example.ui.onboarding.OnboardingScreen
import com.example.ui.onboarding.OnboardingViewModel
import com.example.ui.play.PlayHubScreen
import com.example.ui.profile.AchievementsScreen
import com.example.ui.profile.PrivacyScreen
import com.example.ui.profile.ProfileScreen
import com.example.ui.profile.ProfileViewModel
import com.example.ui.profile.SettingsScreen
import com.example.ui.theme.CortexDeepVoid

@Composable
fun CortexNavHost(
  repository: CortexRepository,
  navController: NavHostController = rememberNavController(),
  startDestination: String = Screen.Home.route
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val showBottomBar = currentRoute in listOf(
    Screen.Home.route,
    Screen.PlayHub.route,
    Screen.Arena.route,
    Screen.BrainDNA.route,
    Screen.Profile.route
  )

  Scaffold(
    containerColor = CortexDeepVoid,
    bottomBar = {
      if (showBottomBar) {
        CortexBottomNavBar(
          currentRoute = currentRoute,
          onNavigate = { targetRoute ->
            navController.navigate(targetRoute) {
              popUpTo(Screen.Home.route) { saveState = true }
              launchSingleTop = true
              restoreState = true
            }
          }
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      NavHost(
        navController = navController,
        startDestination = startDestination
      ) {
        composable(Screen.Onboarding.route) {
          val vm: OnboardingViewModel = viewModel(factory = OnboardingViewModel.Factory(repository))
          OnboardingScreen(
            viewModel = vm,
            onFinishOnboarding = {
              navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Onboarding.route) { inclusive = true }
              }
            }
          )
        }

        composable(Screen.Home.route) {
          val vm: HomeViewModel = viewModel(factory = HomeViewModel.Factory(repository))
          HomeScreen(
            viewModel = vm,
            onNavigateToWorld = { worldId, mode ->
              navController.navigate(Screen.Gameplay.createRoute(worldId, mode))
            },
            onNavigateToBrainDNA = {
              navController.navigate(Screen.BrainDNA.route)
            },
            onNavigateToAIRival = {
              navController.navigate(Screen.AIRivalBattle.route)
            },
            onNavigateToPlayHub = {
              navController.navigate(Screen.PlayHub.route)
            },
            onNavigateToEvolution = {
              navController.navigate(Screen.MindEvolution.route)
            }
          )
        }

        composable(Screen.PlayHub.route) {
          PlayHubScreen(
            onSelectWorld = { worldId, mode ->
              navController.navigate(Screen.Gameplay.createRoute(worldId, mode))
            }
          )
        }

        composable(
          route = Screen.Gameplay.route,
          arguments = listOf(
            navArgument("worldId") { type = NavType.StringType },
            navArgument("mode") { type = NavType.StringType; defaultValue = "WORLD" }
          )
        ) { backStackEntry ->
          val worldId = backStackEntry.arguments?.getString("worldId") ?: "logic_lab"
          val mode = backStackEntry.arguments?.getString("mode") ?: "WORLD"
          val vm: GameplayViewModel = viewModel(factory = GameplayViewModel.Factory(repository, worldId, mode))

          GameplayScreen(
            viewModel = vm,
            onNavigateBack = { navController.popBackStack() },
            onSessionComplete = { navController.popBackStack() }
          )
        }

        composable(Screen.BrainDNA.route) {
          val vm: BrainDNAViewModel = viewModel(factory = BrainDNAViewModel.Factory(repository))
          BrainDNAScreen(
            viewModel = vm,
            onNavigateToEvolution = { navController.navigate(Screen.MindEvolution.route) },
            onTrainCategory = { categoryCode ->
              navController.navigate(Screen.Gameplay.createRoute(categoryCode, "DRILL"))
            }
          )
        }

        composable(Screen.MindEvolution.route) {
          val vm: BrainDNAViewModel = viewModel(factory = BrainDNAViewModel.Factory(repository))
          MindEvolutionScreen(
            viewModel = vm,
            onNavigateBack = { navController.popBackStack() }
          )
        }

        composable(Screen.Arena.route) {
          val vm: ArenaViewModel = viewModel(factory = ArenaViewModel.Factory(repository))
          ArenaScreen(
            viewModel = vm,
            onStartRivalBattle = { navController.navigate(Screen.AIRivalBattle.route) }
          )
        }

        composable(Screen.AIRivalBattle.route) {
          val vm: ArenaViewModel = viewModel(factory = ArenaViewModel.Factory(repository))
          AIRivalBattleScreen(
            viewModel = vm,
            onNavigateBack = { navController.popBackStack() }
          )
        }

        composable(Screen.Profile.route) {
          val vm: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory(repository))
          ProfileScreen(
            viewModel = vm,
            onNavigateToAchievements = { navController.navigate(Screen.Achievements.route) },
            onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
            onNavigateToPrivacy = { navController.navigate(Screen.Privacy.route) }
          )
        }

        composable(Screen.Achievements.route) {
          val vm: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory(repository))
          AchievementsScreen(
            viewModel = vm,
            onNavigateBack = { navController.popBackStack() }
          )
        }

        composable(Screen.Settings.route) {
          SettingsScreen(
            onNavigateBack = { navController.popBackStack() }
          )
        }

        composable(Screen.Privacy.route) {
          PrivacyScreen(
            onNavigateBack = { navController.popBackStack() }
          )
        }
      }
    }
  }
}
