package com.example.ui.gameplay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.domain.model.Challenge
import com.example.domain.model.ChallengeType
import com.example.domain.model.SessionSummary
import com.example.ui.components.CategoryIcon
import com.example.ui.components.CortexCard
import com.example.ui.components.NeonButton
import com.example.ui.theme.CortexAmber
import com.example.ui.theme.CortexBorder
import com.example.ui.theme.CortexCyan
import com.example.ui.theme.CortexDeepVoid
import com.example.ui.theme.CortexEmerald
import com.example.ui.theme.CortexPrimary
import com.example.ui.theme.CortexRose
import com.example.ui.theme.CortexSurface
import com.example.ui.theme.CortexSurfaceElevated
import com.example.ui.theme.CortexSurfaceVariant
import com.example.ui.theme.CortexTextMuted
import com.example.ui.theme.CortexTextPrimary
import com.example.ui.theme.CortexTextSecondary

@Composable
fun GameplayScreen(
  viewModel: GameplayViewModel,
  onNavigateBack: () -> Unit,
  onSessionComplete: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val challenge = uiState.currentChallenge

  if (uiState.isSessionFinished && uiState.sessionSummary != null) {
    SessionSummaryView(
      summary = uiState.sessionSummary!!,
      onContinue = onSessionComplete
    )
    return
  }

  if (challenge == null) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(CortexDeepVoid),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator(color = CortexCyan)
    }
    return
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(CortexDeepVoid)
      .statusBarsPadding()
      .navigationBarsPadding()
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState())
    ) {
      Spacer(modifier = Modifier.height(8.dp))

      // Top Status Bar: Exit, Progress & Category Tag
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onNavigateBack,
          modifier = Modifier.testTag("btn_gameplay_exit")
        ) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Exit", tint = CortexTextMuted)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          CategoryIcon(category = challenge.category, size = 16.dp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = challenge.category.displayName.uppercase(),
            color = challenge.category.themeColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
          )
        }

        Text(
          text = "${uiState.currentIndex + 1}/${uiState.challenges.size}",
          color = Color.White,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Timer Countdown Bar
      val timerProgress by animateFloatAsState(
        targetValue = if (challenge.timeLimitSeconds > 0) (uiState.remainingSeconds.toFloat() / challenge.timeLimitSeconds.toFloat()) else 1f,
        label = "timer_progress"
      )

      val timerColor by animateColorAsState(
        targetValue = when {
          uiState.remainingSeconds <= 3 -> CortexRose
          uiState.remainingSeconds <= 6 -> CortexAmber
          else -> CortexCyan
        },
        label = "timer_color"
      )

      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "NEURAL LATENCY BUFFER",
            color = CortexTextMuted,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Text(
            text = "${uiState.remainingSeconds}s",
            color = timerColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(CortexSurfaceElevated)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth(fraction = timerProgress.coerceIn(0f, 1f))
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp))
              .background(timerColor)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Dynamic Rule Inversion Alert
      if (challenge.type == ChallengeType.RULE_SWITCH) {
        CortexCard(
          modifier = Modifier.fillMaxWidth(),
          borderColor = CortexRose,
          backgroundColor = CortexRose.copy(alpha = 0.15f),
          glow = true
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Warning, contentDescription = "Rule Shift", tint = CortexRose, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "DYNAMIC RULE INVERSION ACTIVE: Suppress instinct.",
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
        Spacer(modifier = Modifier.height(12.dp))
      }

      // Main Challenge Card
      CortexCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = challenge.category.themeColor.copy(alpha = 0.4f),
        backgroundColor = CortexSurfaceElevated,
        glow = true
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = challenge.title.uppercase(),
              color = CortexTextPrimary,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(challenge.category.themeColor.copy(alpha = 0.2f))
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = "DIFF ${challenge.difficulty}",
                color = challenge.category.themeColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = challenge.prompt,
            color = Color.White,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Medium
          )

          // Stroop Visual Element
          if (challenge.type == ChallengeType.STROOP_SPEED && challenge.contextVisual != null) {
            val parts = challenge.contextVisual.split("|")
            val wordText = parts.getOrNull(0) ?: "WORD"
            val hexColor = parts.getOrNull(1) ?: "#00F0FF"
            val parsedColor = try {
              Color(android.graphics.Color.parseColor(hexColor))
            } catch (e: Exception) {
              CortexCyan
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CortexSurfaceVariant)
                .border(1.dp, CortexBorder, RoundedCornerShape(12.dp)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = wordText,
                color = parsedColor,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Challenge Input Renderers
      when (challenge.type) {
        ChallengeType.MATRIX_MEMORY -> {
          MatrixMemoryView(
            gridSize = challenge.visualGridSize,
            targetSequence = challenge.targetSequence,
            selectedNodes = uiState.selectedMatrixNodes,
            isRevealPhase = uiState.isMemoryRevealPhase,
            onToggleNode = { viewModel.onToggleMatrixNode(it) },
            onSubmit = { viewModel.submitMatrixMemory() }
          )
        }

        ChallengeType.CREATIVE_TEXT -> {
          CreativeTextView(
            text = uiState.creativeInputText,
            onTextChange = { viewModel.setCreativeText(it) },
            isEvaluating = uiState.isEvaluatingCreative,
            onSubmit = { viewModel.submitCreativeText() }
          )
        }

        else -> {
          // Standard Multiple Choice & Speed options
          challenge.options.forEachIndexed { index, option ->
            CortexCard(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .clickable { viewModel.onSelectOption(index) }
                .testTag("gameplay_opt_$index"),
              backgroundColor = CortexSurfaceElevated
            ) {
              Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(CortexSurface)
                    .border(1.dp, CortexCyan.copy(alpha = 0.5f), CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "${'A' + index}",
                    color = CortexCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                  text = option,
                  color = Color.White,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(100.dp))
    }

    // Feedback Overlay Sheet
    AnimatedVisibility(
      visible = uiState.showFeedbackSheet,
      enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
      exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
      modifier = Modifier.align(Alignment.BottomCenter)
    ) {
      FeedbackOverlay(
        evaluation = uiState.currentEvaluation,
        onNext = { viewModel.nextChallenge() }
      )
    }
  }
}

@Composable
private fun MatrixMemoryView(
  gridSize: Int,
  targetSequence: List<Int>,
  selectedNodes: List<Int>,
  isRevealPhase: Boolean,
  onToggleNode: (Int) -> Unit,
  onSubmit: () -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = if (isRevealPhase) "MEMORIZE ILLUMINATED SYNAPSES" else "RECONSTRUCT TARGET PATTERN",
      color = if (isRevealPhase) CortexAmber else CortexCyan,
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )

    Spacer(modifier = Modifier.height(14.dp))

    val totalNodes = gridSize * gridSize
    LazyVerticalGrid(
      columns = GridCells.Fixed(gridSize),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier
        .size(240.dp)
        .padding(8.dp)
    ) {
      items(totalNodes) { index ->
        val isTargetInReveal = isRevealPhase && targetSequence.contains(index)
        val isUserSelected = !isRevealPhase && selectedNodes.contains(index)

        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
              when {
                isTargetInReveal -> CortexCyan
                isUserSelected -> CortexEmerald
                else -> CortexSurfaceElevated
              }
            )
            .border(
              width = 1.5.dp,
              color = when {
                isTargetInReveal -> Color.White
                isUserSelected -> CortexEmerald
                else -> CortexBorder
              },
              shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = !isRevealPhase) { onToggleNode(index) }
            .testTag("matrix_node_$index")
        )
      }
    }

    if (!isRevealPhase) {
      Spacer(modifier = Modifier.height(16.dp))
      NeonButton(
        text = "SUBMIT RECALL PATTERN",
        icon = Icons.AutoMirrored.Filled.ArrowForward,
        onClick = onSubmit,
        modifier = Modifier.fillMaxWidth(),
        testTag = "btn_submit_matrix"
      )
    }
  }
}

@Composable
private fun CreativeTextView(
  text: String,
  onTextChange: (String) -> Unit,
  isEvaluating: Boolean,
  onSubmit: () -> Unit
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    OutlinedTextField(
      value = text,
      onValueChange = onTextChange,
      label = { Text("Enter your creative synthesis / solution") },
      minLines = 4,
      maxLines = 6,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = CortexCyan,
        unfocusedBorderColor = CortexBorder,
        focusedContainerColor = CortexSurfaceElevated,
        unfocusedContainerColor = CortexSurface,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
      ),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("input_creative_solution")
    )

    Spacer(modifier = Modifier.height(16.dp))

    NeonButton(
      text = if (isEvaluating) "AI EVALUATING NOVELTY..." else "SUBMIT LATERAL SYNTHESIS",
      icon = Icons.Default.AutoAwesome,
      enabled = text.isNotBlank() && !isEvaluating,
      onClick = onSubmit,
      modifier = Modifier.fillMaxWidth(),
      testTag = "btn_submit_creative"
    )
  }
}

@Composable
private fun FeedbackOverlay(
  evaluation: com.example.domain.model.ChallengeEvaluation?,
  onNext: () -> Unit
) {
  if (evaluation == null) return

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
      .background(CortexSurfaceElevated)
      .border(1.dp, if (evaluation.isCorrect) CortexEmerald else CortexRose, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
      .padding(20.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = if (evaluation.isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = if (evaluation.isCorrect) CortexEmerald else CortexRose,
            modifier = Modifier.size(28.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = if (evaluation.isCorrect) "NEURAL LOCK ACHIEVED" else "COGNITIVE FRICTION DETECTED",
            color = if (evaluation.isCorrect) CortexEmerald else CortexRose,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
          )
        }

        // Latency
        Text(
          text = "${evaluation.responseTimeMs}ms",
          color = CortexTextMuted,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // XP & SI Delta
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(CortexSurface)
            .padding(vertical = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "+${evaluation.xpEarned} XP",
            color = CortexCyan,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(CortexSurface)
            .padding(vertical = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (evaluation.siScoreDelta >= 0) "+${evaluation.siScoreDelta} SI" else "${evaluation.siScoreDelta} SI",
            color = if (evaluation.siScoreDelta >= 0) CortexEmerald else CortexRose,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = evaluation.explanation,
        color = CortexTextSecondary,
        fontSize = 13.sp,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(16.dp))

      NeonButton(
        text = "NEXT CHALLENGE",
        icon = Icons.AutoMirrored.Filled.ArrowForward,
        onClick = onNext,
        modifier = Modifier.fillMaxWidth(),
        testTag = "btn_next_challenge"
      )
    }
  }
}

@Composable
private fun SessionSummaryView(
  summary: SessionSummary,
  onContinue: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(CortexDeepVoid)
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(20.dp)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = null,
        tint = CortexEmerald,
        modifier = Modifier.size(54.dp)
      )

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "SIMULATION COMPLETE",
        color = Color.White,
        fontSize = 22.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.sp
      )

      Text(
        text = "Cognitive Session Telemetry Compiled",
        color = CortexCyan,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Session Stats Card
      CortexCard(modifier = Modifier.fillMaxWidth(), glow = true) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("ACCURACY", color = CortexTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              Text("${(summary.correctCount.toFloat() / summary.challengesCompleted * 100).toInt()}%", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Column {
              Text("AVG SPEED", color = CortexTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              Text("${summary.averageResponseTimeMs}ms", color = CortexCyan, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Column {
              Text("SI EVOLUTION", color = CortexTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              Text(if (summary.siScoreDelta >= 0) "+${summary.siScoreDelta}" else "${summary.siScoreDelta}", color = CortexEmerald, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // AI Director Synthesis
      CortexCard(modifier = Modifier.fillMaxWidth(), backgroundColor = CortexSurfaceElevated) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CortexCyan, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("AI DIRECTOR SYNTHESIS", color = CortexCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = summary.aiInsight,
            color = CortexTextSecondary,
            fontSize = 13.sp,
            lineHeight = 19.sp
          )
        }
      }
    }

    NeonButton(
      text = "RETURN TO CORTEX",
      icon = Icons.AutoMirrored.Filled.ArrowForward,
      onClick = onContinue,
      modifier = Modifier.fillMaxWidth(),
      testTag = "btn_session_summary_continue"
    )
  }
}
