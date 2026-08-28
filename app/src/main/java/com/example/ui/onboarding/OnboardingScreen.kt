package com.example.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AvatarType
import com.example.domain.model.BrainDNA
import com.example.domain.model.Challenge
import com.example.domain.model.CognitiveCategory
import com.example.ui.components.BrainDNABarRow
import com.example.ui.components.BrainRadarChart
import com.example.ui.components.CategoryIcon
import com.example.ui.components.CortexCard
import com.example.ui.components.NeonButton
import com.example.ui.theme.CortexBorder
import com.example.ui.theme.CortexBorderGlow
import com.example.ui.theme.CortexCyan
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.CortexEmerald
import com.example.ui.theme.CortexPrimary
import com.example.ui.theme.CortexPrimaryLight
import com.example.ui.theme.CortexRose
import com.example.ui.theme.CortexSurface
import com.example.ui.theme.CortexSurfaceElevated
import com.example.ui.theme.CortexTextMuted
import com.example.ui.theme.CortexTextPrimary
import com.example.ui.theme.CortexTextSecondary

@Composable
fun OnboardingScreen(
  viewModel: OnboardingViewModel,
  onFinishOnboarding: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(CortexDeepVoid)
      .statusBarsPadding()
      .navigationBarsPadding()
  ) {
    AnimatedContent(
      targetState = uiState.stepIndex,
      transitionSpec = { fadeIn() togetherWith fadeOut() },
      label = "onboarding_step"
    ) { step ->
      when (step) {
        0 -> WelcomeStep(onStart = { viewModel.nextStep() })
        1 -> PhilosophyStep(
          onBack = { viewModel.previousStep() },
          onNext = { viewModel.nextStep() }
        )
        2 -> UsernameStep(
          username = uiState.username,
          onUsernameChange = { viewModel.setUsername(it) },
          onBack = { viewModel.previousStep() },
          onNext = { viewModel.nextStep() }
        )
        3 -> AvatarStep(
          selectedAvatar = uiState.selectedAvatar,
          onSelectAvatar = { viewModel.setAvatar(it) },
          onBack = { viewModel.previousStep() },
          onNext = { viewModel.nextStep() }
        )
        4 -> PrivacyStep(
          onBack = { viewModel.previousStep() },
          onNext = { viewModel.nextStep() }
        )
        5 -> BaselineStep(
          challenges = uiState.baselineChallenges,
          currentIndex = uiState.currentBaselineIndex,
          onAnswer = { isCorrect -> viewModel.submitBaselineAnswer(isCorrect) }
        )
        6 -> FirstBrainDNAStep(
          brainDNA = uiState.generatedBrainDNA ?: BrainDNA(),
          username = uiState.username,
          onEnterCortex = onFinishOnboarding
        )
        else -> WelcomeStep(onStart = { viewModel.nextStep() })
      }
    }
  }
}

@Composable
private fun WelcomeStep(onStart: () -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(110.dp)
        .clip(CircleShape)
        .background(
          Brush.radialGradient(
            colors = listOf(CortexCyan.copy(alpha = 0.35f), CortexPrimary.copy(alpha = 0.15f), Color.Transparent)
          )
        )
        .border(1.5.dp, CortexCyan, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Psychology,
        contentDescription = "Cortex Logo",
        tint = CortexCyan,
        modifier = Modifier.size(56.dp)
      )
    }

    Spacer(modifier = Modifier.height(28.dp))

    Text(
      text = "SANDLIP CORTEX",
      color = Color.White,
      fontSize = 28.sp,
      fontWeight = FontWeight.Black,
      letterSpacing = 2.sp,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "Your Mind. Your Patterns. Your Evolution.",
      color = CortexCyan,
      fontSize = 14.sp,
      fontWeight = FontWeight.SemiBold,
      letterSpacing = 0.5.sp,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "An AI-powered adaptive cognitive intelligence platform designed to analyze how your mind deduces, adapts, and evolves.",
      color = CortexTextSecondary,
      fontSize = 14.sp,
      lineHeight = 20.sp,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(48.dp))

    NeonButton(
      text = "INITIALIZE CORTEX",
      icon = Icons.AutoMirrored.Filled.ArrowForward,
      onClick = onStart,
      modifier = Modifier.fillMaxWidth(),
      testTag = "btn_begin_onboarding"
    )
  }
}

@Composable
private fun PhilosophyStep(onBack: () -> Unit, onNext: () -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
      ) {
        IconButton(onClick = onBack) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CortexTextMuted)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "WHAT KIND OF THINKER ARE YOU?",
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(16.dp))

      CortexCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = CortexSurfaceElevated,
        glow = true
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "“The game studies how you think, not only whether you are right or wrong.”",
            color = CortexCyan,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 22.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Every decision, response latency, consistency under distraction, and reaction to rule inversions shapes your dynamic digital cognitive profile called BRAIN DNA.",
            color = CortexTextSecondary,
            fontSize = 13.sp,
            lineHeight = 19.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      CortexCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "CORE ARCHITECTURE",
            color = CortexTextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "PLAYER ACTION  ➔  BEHAVIOR TELEMETRY  ➔  AI COGNITIVE PROFILE  ➔  DYNAMIC DIFFICULTY ADAPTATION  ➔  MIND EVOLUTION",
            color = CortexEmerald,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 17.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    NeonButton(
      text = "CONTINUE",
      onClick = onNext,
      modifier = Modifier.fillMaxWidth(),
      testTag = "btn_philosophy_continue"
    )
  }
}

@Composable
private fun UsernameStep(
  username: String,
  onUsernameChange: (String) -> Unit,
  onBack: () -> Unit,
  onNext: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
      ) {
        IconButton(onClick = onBack) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CortexTextMuted)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "IDENTIFY YOUR MIND",
        color = Color.White,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Choose a designation for the Sandlip Cortex neural network.",
        color = CortexTextSecondary,
        fontSize = 14.sp,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(32.dp))

      OutlinedTextField(
        value = username,
        onValueChange = onUsernameChange,
        label = { Text("Operative Username") },
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = CortexCyan,
          unfocusedBorderColor = CortexBorder,
          focusedLabelColor = CortexCyan,
          unfocusedLabelColor = CortexTextMuted,
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White,
          focusedContainerColor = CortexSurfaceElevated,
          unfocusedContainerColor = CortexSurface
        ),
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("input_username")
      )
    }

    NeonButton(
      text = "NEXT: SELECT AVATAR",
      onClick = onNext,
      enabled = username.isNotBlank(),
      modifier = Modifier.fillMaxWidth(),
      testTag = "btn_username_next"
    )
  }
}

@Composable
private fun AvatarStep(
  selectedAvatar: AvatarType,
  onSelectAvatar: (AvatarType) -> Unit,
  onBack: () -> Unit,
  onNext: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
      ) {
        IconButton(onClick = onBack) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CortexTextMuted)
        }
      }

      Text(
        text = "SELECT COGNITIVE AVATAR",
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Choose your symbolic representation.",
        color = CortexTextSecondary,
        fontSize = 13.sp
      )

      Spacer(modifier = Modifier.height(20.dp))

      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(AvatarType.entries) { avatar ->
          val isSelected = selectedAvatar == avatar
          val icon = when (avatar) {
            AvatarType.HUMAN -> Icons.Default.Person
            AvatarType.ANDROID -> Icons.Default.SmartToy
            AvatarType.CYBER_MIND -> Icons.Default.Psychology
            AvatarType.NEURAL_ENTITY -> Icons.Default.Grain
            AvatarType.EXPLORER -> Icons.Default.Explore
            AvatarType.ARCHITECT -> Icons.Default.DesignServices
          }

          CortexCard(
            modifier = Modifier
              .clickable { onSelectAvatar(avatar) }
              .testTag("avatar_${avatar.key}"),
            borderColor = if (isSelected) CortexCyan else CortexBorder,
            backgroundColor = if (isSelected) CortexSurfaceElevated else CortexSurface,
            glow = isSelected
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                imageVector = icon,
                contentDescription = avatar.title,
                tint = if (isSelected) CortexCyan else CortexTextMuted,
                modifier = Modifier.size(36.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = avatar.title,
                color = if (isSelected) Color.White else CortexTextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = avatar.subtitle,
                color = CortexTextMuted,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 13.sp
              )
            }
          }
        }
      }
    }

    NeonButton(
      text = "CONTINUE TO PRIVACY",
      onClick = onNext,
      modifier = Modifier.fillMaxWidth(),
      testTag = "btn_avatar_next"
    )
  }
}

@Composable
private fun PrivacyStep(onBack: () -> Unit, onNext: () -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
      ) {
        IconButton(onClick = onBack) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CortexTextMuted)
        }
      }

      Icon(
        imageVector = Icons.Default.Security,
        contentDescription = "Privacy",
        tint = CortexEmerald,
        modifier = Modifier.size(40.dp)
      )

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "TRANSPARENCY & PRIVACY",
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(16.dp))

      CortexCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "NO MEDICAL OR CLINICAL CLAIMS",
            color = CortexRose,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Sandlip Cortex and Brain DNA are entertainment and gameplay metrics. They do NOT measure clinical IQ or diagnose psychological/neurological conditions.",
            color = CortexTextSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      CortexCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "DATA LOCALIZATION",
            color = CortexEmerald,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "All your gameplay data, session telemetry, and Brain DNA profiles are stored locally on your device via Room Database. You can reset or delete data anytime in Settings.",
            color = CortexTextSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    NeonButton(
      text = "BEGIN BASELINE ASSESSMENT",
      onClick = onNext,
      modifier = Modifier.fillMaxWidth(),
      testTag = "btn_begin_baseline"
    )
  }
}

@Composable
private fun BaselineStep(
  challenges: List<Challenge>,
  currentIndex: Int,
  onAnswer: (Boolean) -> Unit
) {
  val challenge = challenges.getOrNull(currentIndex) ?: return

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(20.dp)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "BASELINE ASSESSMENT",
          color = CortexCyan,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Text(
          text = "${currentIndex + 1} / ${challenges.size}",
          color = CortexTextMuted,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Category Tag
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(CortexSurfaceElevated)
          .padding(horizontal = 10.dp, vertical = 4.dp)
      ) {
        CategoryIcon(category = challenge.category, size = 14.dp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = challenge.category.displayName.uppercase(),
          color = challenge.category.themeColor,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      CortexCard(modifier = Modifier.fillMaxWidth(), glow = true) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = challenge.title,
            color = CortexTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = challenge.prompt,
            color = CortexTextSecondary,
            fontSize = 14.sp,
            lineHeight = 20.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Options
      challenge.options.forEachIndexed { index, option ->
        val isCorrectOption = index == challenge.correctOptionIndex
        CortexCard(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable { onAnswer(isCorrectOption) }
            .testTag("baseline_opt_$index"),
          backgroundColor = CortexSurfaceElevated
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(CortexSurface)
                .border(1.dp, CortexBorder, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${'A' + index}",
                color = CortexCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = option,
              color = Color.White,
              fontSize = 14.sp
            )
          }
        }
      }
    }
  }
}

@Composable
private fun FirstBrainDNAStep(
  brainDNA: BrainDNA,
  username: String,
  onEnterCortex: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(20.dp)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = Icons.Default.CheckCircle,
      contentDescription = null,
      tint = CortexEmerald,
      modifier = Modifier.size(42.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
      text = "INITIAL BRAIN DNA GENERATED",
      color = Color.White,
      fontSize = 20.sp,
      fontWeight = FontWeight.Black,
      letterSpacing = 1.sp
    )

    Text(
      text = "Operative: $username • Baseline SI Score: ${brainDNA.siScore}",
      color = CortexCyan,
      fontSize = 13.sp,
      fontWeight = FontWeight.Medium
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Radar Chart
    BrainRadarChart(
      brainDNA = brainDNA,
      modifier = Modifier
        .size(220.dp)
        .padding(8.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Bar breakdown
    CortexCard(modifier = Modifier.fillMaxWidth()) {
      Column(modifier = Modifier.padding(14.dp)) {
        CognitiveCategory.entries.forEach { category ->
          BrainDNABarRow(category = category, score = brainDNA.getScore(category))
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    NeonButton(
      text = "ENTER THE CORTEX",
      icon = Icons.AutoMirrored.Filled.ArrowForward,
      onClick = onEnterCortex,
      modifier = Modifier.fillMaxWidth(),
      testTag = "btn_enter_cortex"
    )
  }
}
