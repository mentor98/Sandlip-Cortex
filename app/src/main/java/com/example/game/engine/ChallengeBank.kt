package com.example.game.engine

import com.example.domain.model.Challenge
import com.example.domain.model.ChallengeType
import com.example.domain.model.CognitiveCategory
import com.example.domain.model.CognitiveWorld
import java.util.UUID
import kotlin.random.Random

object ChallengeBank {

  private val builtInChallenges = listOf(
    // WORLD 1 — LOGIC LAB
    Challenge(
      id = "log_1",
      category = CognitiveCategory.LOGIC,
      type = ChallengeType.MULTIPLE_CHOICE,
      difficulty = 3,
      title = "Syllogistic Inference",
      prompt = "All Quarks are Leptons. Some Leptons are Bosons. No Bosons are Fermions. Which statement MUST be true?",
      options = listOf(
        "Some Fermions are Quarks",
        "No Quarks are Fermions",
        "Some Leptons are not Fermions",
        "All Leptons are Quarks"
      ),
      correctOptionIndex = 2,
      timeLimitSeconds = 18,
      baseExplanation = "Because some Leptons are Bosons, and no Bosons are Fermions, those Leptons which are Bosons cannot be Fermions."
    ),
    Challenge(
      id = "log_2",
      category = CognitiveCategory.LOGIC,
      type = ChallengeType.MULTIPLE_CHOICE,
      difficulty = 5,
      title = "Recursive Sequence",
      prompt = "Find the next value in the series: 2, 6, 12, 20, 30, 42, ?",
      options = listOf("52", "54", "56", "58"),
      correctOptionIndex = 2,
      timeLimitSeconds = 15,
      baseExplanation = "Differences between terms are +4, +6, +8, +10, +12, so the next difference is +14. 42 + 14 = 56 (or n*(n+1): 7*8 = 56)."
    ),
    Challenge(
      id = "log_3",
      category = CognitiveCategory.LOGIC,
      type = ChallengeType.MULTIPLE_CHOICE,
      difficulty = 7,
      title = "Boolean Gate Matrix",
      prompt = "Input A = 1, Input B = 0, Input C = 1. What is the output of (A XOR B) AND (NOT C OR A)?",
      options = listOf("0 (False)", "1 (True)", "Undefined", "Floating"),
      correctOptionIndex = 1,
      timeLimitSeconds = 14,
      baseExplanation = "(1 XOR 0) = 1. (NOT 1 OR 1) = (0 OR 1) = 1. 1 AND 1 = 1 (True)."
    ),
    Challenge(
      id = "log_4",
      category = CognitiveCategory.LOGIC,
      type = ChallengeType.MULTIPLE_CHOICE,
      difficulty = 8,
      title = "Conditional Truth Teller",
      prompt = "Entity Alpha says: 'Exactly one of us is lying.' Entity Beta says: 'Alpha is telling the truth.' Who is telling the truth?",
      options = listOf("Both Alpha and Beta", "Only Alpha", "Only Beta", "Neither (Both lie)"),
      correctOptionIndex = 3,
      timeLimitSeconds = 18,
      baseExplanation = "If Alpha tells the truth, Beta tells the truth (making 0 liars, contradicting Alpha). If Alpha lies, Beta also lies. Both lie is the only consistent state."
    ),

    // WORLD 2 — MEMORY VAULT
    Challenge(
      id = "mem_1",
      category = CognitiveCategory.MEMORY,
      type = ChallengeType.MATRIX_MEMORY,
      difficulty = 4,
      title = "Matrix Flash Recall",
      prompt = "Memorize the active illuminated nodes, then reconstruct the grid.",
      visualGridSize = 3,
      targetSequence = listOf(0, 2, 4, 8),
      options = listOf("Reconstruct from memory"),
      correctOptionIndex = 0,
      timeLimitSeconds = 12,
      baseExplanation = "4 nodes illuminated in the 3x3 matrix: Top-Left, Top-Right, Center, Bottom-Right."
    ),
    Challenge(
      id = "mem_2",
      category = CognitiveCategory.MEMORY,
      type = ChallengeType.SEQUENCE_RECALL,
      difficulty = 6,
      title = "Synaptic Sequence",
      prompt = "Observe the flash order of 5 nodes and repeat the sequence in exact chronological order.",
      visualGridSize = 3,
      targetSequence = listOf(1, 4, 7, 3, 5),
      options = listOf("Execute Sequence"),
      correctOptionIndex = 0,
      timeLimitSeconds = 15,
      baseExplanation = "Temporal sequence: Top-Center -> Center -> Bottom-Center -> Middle-Left -> Middle-Right."
    ),
    Challenge(
      id = "mem_3",
      category = CognitiveCategory.MEMORY,
      type = ChallengeType.MULTIPLE_CHOICE,
      difficulty = 5,
      title = "Missing Cipher Component",
      prompt = "You were shown: [Ω, Ψ, λ, Δ, Σ, Φ, θ]. Which symbol was absent from the set?",
      options = listOf("Δ", "Ψ", "β", "Σ"),
      correctOptionIndex = 2,
      timeLimitSeconds = 10,
      baseExplanation = "The symbol 'β' (Beta) was never in the prime mnemonic set."
    ),

    // WORLD 3 — NEURAL SPEED
    Challenge(
      id = "spd_1",
      category = CognitiveCategory.SPEED,
      type = ChallengeType.STROOP_SPEED,
      difficulty = 4,
      title = "Stroop Interference",
      prompt = "Select the COLOR of the font, ignoring the literal word meaning.",
      contextVisual = "RED|#00F0FF", // Word is RED, font color is CYAN (#00F0FF)
      options = listOf("Red", "Cyan", "Green", "Magenta"),
      correctOptionIndex = 1,
      timeLimitSeconds = 8,
      baseExplanation = "The ink color displayed is Cyan, despite the cognitive interference of reading 'RED'."
    ),
    Challenge(
      id = "spd_2",
      category = CognitiveCategory.SPEED,
      type = ChallengeType.STROOP_SPEED,
      difficulty = 7,
      title = "Inverse Stroop Collision",
      prompt = "Select the literal WORD text, ignoring the ink color.",
      contextVisual = "EMERALD|#FF4365", // Word is EMERALD, color is ROSE
      options = listOf("Emerald", "Rose", "Cyan", "Amber"),
      correctOptionIndex = 0,
      timeLimitSeconds = 6,
      baseExplanation = "The prompt instructed to read the literal word 'Emerald' while suppressing the rose ink."
    ),
    Challenge(
      id = "spd_3",
      category = CognitiveCategory.SPEED,
      type = ChallengeType.MULTIPLE_CHOICE,
      difficulty = 5,
      title = "Rapid Parity Scan",
      prompt = "Is the sum of (47 + 38 + 91 + 14) EVEN or ODD?",
      options = listOf("EVEN", "ODD"),
      correctOptionIndex = 0,
      timeLimitSeconds = 7,
      baseExplanation = "Units digits: 7+8+1+4 = 20 (ends in 0). Sum is 190, which is EVEN."
    ),

    // WORLD 4 — PATTERN REALM
    Challenge(
      id = "pat_1",
      category = CognitiveCategory.PATTERN,
      type = ChallengeType.PATTERN_MATRIX,
      difficulty = 4,
      title = "Matrix Shape Progression",
      prompt = "Determine the missing symbol in the 3x3 transformational grid:\n[▲, ●, ■]\n[●, ■, ◆]\n[■, ◆, ?]",
      options = listOf("▲", "●", "★", "◆"),
      correctOptionIndex = 0,
      timeLimitSeconds = 15,
      baseExplanation = "Each row shifts elements left cyclically. The series cycles ▲ -> ● -> ■ -> ◆ -> ▲."
    ),
    Challenge(
      id = "pat_2",
      category = CognitiveCategory.PATTERN,
      type = ChallengeType.MULTIPLE_CHOICE,
      difficulty = 6,
      title = "Double Alternating Rule",
      prompt = "Complete the sequence: 3, 4, 6, 8, 12, 16, 24, ?",
      options = listOf("30", "32", "36", "48"),
      correctOptionIndex = 1,
      timeLimitSeconds = 15,
      baseExplanation = "Two interleaved sequences: (3, 6, 12, 24) doubling, and (4, 8, 16, 32) doubling. Next term is 16 * 2 = 32."
    ),
    Challenge(
      id = "pat_3",
      category = CognitiveCategory.PATTERN,
      type = ChallengeType.PATTERN_MATRIX,
      difficulty = 8,
      title = "Topological Rotation Grid",
      prompt = "Row 1: [0°, 90°, 180°]\nRow 2: [90°, 180°, 270°]\nRow 3: [180°, 270°, ?]",
      options = listOf("0° (360°)", "90°", "270°", "45°"),
      correctOptionIndex = 0,
      timeLimitSeconds = 12,
      baseExplanation = "Each cell increments clockwise by 90°. 270° + 90° = 360° / 0°."
    ),

    // WORLD 5 — HUMAN MIND (SOCIAL REASONING & CONTEXT)
    Challenge(
      id = "hum_1",
      category = CognitiveCategory.HUMAN_MIND,
      type = ChallengeType.SOCIAL_NUANCE,
      difficulty = 5,
      title = "Conversational Subtext",
      prompt = "During a sprint review, a lead architect remarks: 'That is certainly one way to implement the cache architecture.' What is the most probable contextual implication?",
      options = listOf(
        "Strong enthusiastic endorsement of the approach",
        "Polite skepticism suggesting the solution is suboptimal or unusual",
        "Confirmation that no other alternatives exist",
        "A formal request to delete the entire repository"
      ),
      correctOptionIndex = 1,
      timeLimitSeconds = 18,
      baseExplanation = "The phrasing 'that is one way' is classic professional euphemism indicating reserved doubt without overt confrontation."
    ),
    Challenge(
      id = "hum_2",
      category = CognitiveCategory.HUMAN_MIND,
      type = ChallengeType.SOCIAL_NUANCE,
      difficulty = 6,
      title = "Perspective Simulation",
      prompt = "A client says: 'Take as much time as you need, but we have a board meeting on Thursday.' How should their true priority be decoded?",
      options = listOf(
        "Deadlines are completely open and flexible",
        "They want you to take 3 weeks to ensure perfection",
        "The board meeting is a hard implied deadline despite polite phrasing",
        "They want the project cancelled immediately"
      ),
      correctOptionIndex = 2,
      timeLimitSeconds = 16,
      baseExplanation = "Contrasting 'take your time' with a specific high-stakes event creates an implied hard constraint for Thursday."
    ),

    // WORLD 6 — CREATIVE MIND (DIVERGENT THINKING)
    Challenge(
      id = "cre_1",
      category = CognitiveCategory.CREATIVITY,
      type = ChallengeType.CREATIVE_TEXT,
      difficulty = 5,
      title = "Alternative Utility Synthesis",
      prompt = "You are stranded with: a broken smartphone, a rubber band, and a plastic bottle. Propose 3 novel survival or signaling utilities.",
      options = emptyList(),
      timeLimitSeconds = 45,
      baseExplanation = "Divergent scoring evaluates reflection signaling with screen, water filtration funnel, elastic catapult."
    ),
    Challenge(
      id = "cre_2",
      category = CognitiveCategory.CREATIVITY,
      type = ChallengeType.CREATIVE_TEXT,
      difficulty = 6,
      title = "Algorithmic Metaphor",
      prompt = "Explain how a Distributed Consensus algorithm works using an analogy of a spaceship crew voting on an emergency landing.",
      options = emptyList(),
      timeLimitSeconds = 45,
      baseExplanation = "Evaluates conceptual mapping, Byzantine fault analogy, and quorum metaphors."
    ),

    // WORLD 7 — STRATEGIST
    Challenge(
      id = "str_1",
      category = CognitiveCategory.STRATEGY,
      type = ChallengeType.RISK_PAYOFF,
      difficulty = 5,
      title = "Expected Value Optimization",
      prompt = "You have 100 Energy Points. You must secure 60 points to survive.\nOption Alpha: 100% chance to gain 55 points.\nOption Beta: 70% chance to gain 80 points, 30% chance to gain 0.\nOption Gamma: 40% chance to gain 150 points.\nWhich choice maximizes probability of survival?",
      options = listOf("Option Alpha", "Option Beta", "Option Gamma", "Pass turn"),
      correctOptionIndex = 1,
      timeLimitSeconds = 20,
      baseExplanation = "Option Alpha guarantees failure (55 < 60). Option Beta gives a 70% survival probability, superior to Gamma's 40%."
    ),
    Challenge(
      id = "str_2",
      category = CognitiveCategory.STRATEGY,
      type = ChallengeType.RISK_PAYOFF,
      difficulty = 7,
      title = "Payoff Equilibrium",
      prompt = "In a 2-player game, if both cooperate, each gets +5. If one defects and one cooperates, defector gets +8, cooperator gets -2. If both defect, each gets 0. If you know opponent plays tit-for-tat, what is optimal long-term strategy?",
      options = listOf("Always Defect", "Always Cooperate", "Randomize 50/50", "Defect on first turn only"),
      correctOptionIndex = 1,
      timeLimitSeconds = 20,
      baseExplanation = "Against tit-for-tat, mutual cooperation yields +5 per turn continuously, dominating recurring mutual defection (0) or retaliatory cycles."
    ),

    // WORLD 8 — SPATIAL CORE
    Challenge(
      id = "spa_1",
      category = CognitiveCategory.SPATIAL,
      type = ChallengeType.SPATIAL_ROTATION,
      difficulty = 6,
      title = "3D Mental Rotation",
      prompt = "An 'L' shaped tetromino in 2D is rotated 90° clockwise, then reflected across the vertical Y-axis. Which visual orientation matches?",
      options = listOf("⅃ (Mirrored L)", "Γ (Top-Right hook)", "⌐ (Top-Left hook)", "L (Original)"),
      correctOptionIndex = 1,
      timeLimitSeconds = 15,
      baseExplanation = "L rotated 90° CW becomes ⅃ (horizontal hook down-left). Reflecting vertically yields Γ (Top-Right hook)."
    ),
    Challenge(
      id = "spa_2",
      category = CognitiveCategory.SPATIAL,
      type = ChallengeType.SPATIAL_ROTATION,
      difficulty = 5,
      title = "Cube Unfolding Projection",
      prompt = "A standard 6-sided die has opposite faces summing to 7 (1 opposite 6, 2 opposite 5, 3 opposite 4). In an unfolded cross net with 1 in center and 2 on top, what must be on the bottom face?",
      options = listOf("3", "4", "5", "6"),
      correctOptionIndex = 2,
      timeLimitSeconds = 15,
      baseExplanation = "When folded up from the net, the face directly opposite to the top (2) is the bottom, which must sum to 7: 7 - 2 = 5."
    ),

    // WORLD 9 — FOCUS CHAMBER (ATTENTION)
    Challenge(
      id = "att_1",
      category = CognitiveCategory.ATTENTION,
      type = ChallengeType.ANOMALY_FOCUS,
      difficulty = 5,
      title = "Distraction Suppression",
      prompt = "Find the single anomaly among the grid of characters:\nXXXXX\nXXXXX\nXXYXX\nXXXXX\nXXXXX\nState the coordinates (Row, Col) 1-indexed.",
      options = listOf("Row 2, Col 3", "Row 3, Col 3", "Row 3, Col 2", "Row 4, Col 3"),
      correctOptionIndex = 1,
      timeLimitSeconds = 10,
      baseExplanation = "The 'Y' anomaly is located at the center: Row 3, Column 3."
    ),
    Challenge(
      id = "att_2",
      category = CognitiveCategory.ATTENTION,
      type = ChallengeType.ANOMALY_FOCUS,
      difficulty = 7,
      title = "Rapid Symbol Filtering",
      prompt = "Target condition: Count occurrences of '★' while ignoring '☆', '✦', and '✧':\n★ ☆ ✦ ★ ✧ ☆ ★ ✦ ★ ☆ ✧ ★",
      options = listOf("3", "4", "5", "6"),
      correctOptionIndex = 2,
      timeLimitSeconds = 10,
      baseExplanation = "There are exactly 5 solid stars '★' in the visual noise array."
    ),

    // WORLD 10 — ADAPTATION TRIAL (DYNAMIC RULE SHIFTS)
    Challenge(
      id = "adp_1",
      category = CognitiveCategory.ADAPTABILITY,
      type = ChallengeType.RULE_SWITCH,
      difficulty = 6,
      title = "Adaptive Rule Shift: Color -> Parity",
      prompt = "RULE JUST CHANGED!\nPrevious Rule: Tap the highest value.\nNEW ACTIVE RULE: Tap the LOWEST EVEN number.",
      ruleCondition = "LOWEST EVEN NUMBER",
      options = listOf("13", "8", "24", "4"),
      correctOptionIndex = 3,
      timeLimitSeconds = 9,
      baseExplanation = "Even numbers are 8, 24, 4. The lowest even number is 4."
    ),
    Challenge(
      id = "adp_2",
      category = CognitiveCategory.ADAPTABILITY,
      type = ChallengeType.RULE_SWITCH,
      difficulty = 8,
      title = "Rule Inversion Protocol",
      prompt = "EMERGENCY RULE INVERSION!\nForbidden Action: Tapping the matching color.\nSelect the color that does NOT match either the font or the word.",
      contextVisual = "CYAN|#00FF9D", // Word CYAN, color EMERALD
      ruleCondition = "NEITHER CYAN NOR EMERALD",
      options = listOf("Cyan", "Emerald", "Amber", "Green"),
      correctOptionIndex = 2,
      timeLimitSeconds = 8,
      baseExplanation = "Cyan is the word, Emerald is the ink. 'Amber' is completely independent of both forbidden states."
    ),

    // BLACK BOX & MYSTERY
    Challenge(
      id = "bb_1",
      category = CognitiveCategory.LOGIC,
      type = ChallengeType.MULTIPLE_CHOICE,
      difficulty = 7,
      title = "Black Box: Transformation Inference",
      prompt = "Observe the Black Box Input/Output pairs:\n[A -> C]\n[B -> E]\n[C -> G]\n[D -> I]\nWhat is the output for [E -> ?]?",
      options = listOf("J", "K", "L", "M"),
      correctOptionIndex = 1,
      timeLimitSeconds = 14,
      baseExplanation = "Mapping: Position n -> (2n + 1). E is 5th letter -> 2(5)+1 = 11th letter, which is 'K'."
    )
  )

  fun getAllChallenges(): List<Challenge> = builtInChallenges

  fun getChallengesForCategory(category: CognitiveCategory, count: Int = 5): List<Challenge> {
    val filtered = builtInChallenges.filter { it.category == category }
    return if (filtered.isNotEmpty()) {
      filtered.shuffled().take(count)
    } else {
      generateProceduralChallenges(category, count)
    }
  }

  fun getChallengesForWorld(world: CognitiveWorld, count: Int = 5): List<Challenge> {
    return if (world.category != null) {
      val matched = builtInChallenges.filter { it.category == world.category }
      if (matched.size >= count) matched.shuffled().take(count)
      else (matched + generateProceduralChallenges(world.category, count - matched.size)).take(count)
    } else {
      // Special modes like Mystery, Black Box, Survival, Infinite
      builtInChallenges.shuffled().take(count)
    }
  }

  fun getBaselineAssessment(): List<Challenge> {
    // 1 challenge from each of the primary cognitive categories
    val list = mutableListOf<Challenge>()
    for (category in CognitiveCategory.entries) {
      val challenge = builtInChallenges.firstOrNull { it.category == category }
        ?: generateProceduralChallenges(category, 1).first()
      list.add(challenge)
    }
    return list
  }

  fun generateProceduralChallenges(category: CognitiveCategory, count: Int): List<Challenge> {
    val result = mutableListOf<Challenge>()
    for (i in 0 until count) {
      val seed = Random.nextInt(100, 999)
      val difficulty = Random.nextInt(2, 9)
      when (category) {
        CognitiveCategory.LOGIC -> {
          val a = Random.nextInt(3, 12)
          val diff = Random.nextInt(3, 7)
          val series = listOf(a, a + diff, a + 2 * diff, a + 3 * diff)
          val correct = a + 4 * diff
          val distractors = listOf(correct - diff, correct + diff, correct + 2 * diff).map { it.toString() }
          val options = (distractors + correct.toString()).shuffled()
          result.add(
            Challenge(
              id = "proc_log_${UUID.randomUUID()}",
              category = category,
              type = ChallengeType.MULTIPLE_CHOICE,
              difficulty = difficulty,
              title = "Arithmetic Progression #$seed",
              prompt = "What is the next number in the sequence: ${series.joinToString(", ")}, ?",
              options = options,
              correctOptionIndex = options.indexOf(correct.toString()),
              timeLimitSeconds = 12,
              baseExplanation = "The constant difference between terms is +$diff. ${series.last()} + $diff = $correct."
            )
          )
        }
        CognitiveCategory.MEMORY -> {
          val size = 3
          val activeNodes = (0 until size * size).shuffled().take(Random.nextInt(3, 6))
          result.add(
            Challenge(
              id = "proc_mem_${UUID.randomUUID()}",
              category = category,
              type = ChallengeType.MATRIX_MEMORY,
              difficulty = difficulty,
              title = "Matrix Synapse Memory #$seed",
              prompt = "Memorize the illuminated nodes and reconstruct them on the grid.",
              visualGridSize = size,
              targetSequence = activeNodes,
              options = listOf("Reconstruct from memory"),
              correctOptionIndex = 0,
              timeLimitSeconds = 12,
              baseExplanation = "Retained ${activeNodes.size} target synaptic positions."
            )
          )
        }
        CognitiveCategory.SPEED -> {
          val colors = listOf("Cyan" to "#00F0FF", "Emerald" to "#00FF9D", "Rose" to "#FF4365", "Amber" to "#FFB800")
          val word = colors.random()
          val ink = colors.filter { it != word }.random()
          result.add(
            Challenge(
              id = "proc_spd_${UUID.randomUUID()}",
              category = category,
              type = ChallengeType.STROOP_SPEED,
              difficulty = difficulty,
              title = "Neural Speed Conflict #$seed",
              prompt = "Identify the visual INK COLOR as rapidly as possible!",
              contextVisual = "${word.first.uppercase()}|${ink.second}",
              options = colors.map { it.first },
              correctOptionIndex = colors.indexOfFirst { it.first == ink.first },
              timeLimitSeconds = 7,
              baseExplanation = "The font was rendered in ${ink.first} ink."
            )
          )
        }
        CognitiveCategory.PATTERN -> {
          val step = Random.nextInt(2, 6)
          val s1 = step
          val s2 = step * 2
          val s3 = step * 4
          val s4 = step * 8
          val correct = (step * 16).toString()
          val opts = listOf(correct, (step * 12).toString(), (step * 14).toString(), (step * 18).toString()).shuffled()
          result.add(
            Challenge(
              id = "proc_pat_${UUID.randomUUID()}",
              category = category,
              type = ChallengeType.MULTIPLE_CHOICE,
              difficulty = difficulty,
              title = "Exponential Scale Pattern #$seed",
              prompt = "Identify the next magnitude: $s1, $s2, $s3, $s4, ?",
              options = opts,
              correctOptionIndex = opts.indexOf(correct),
              timeLimitSeconds = 12,
              baseExplanation = "Each consecutive value is multiplied by 2. $s4 * 2 = $correct."
            )
          )
        }
        CognitiveCategory.ADAPTABILITY -> {
          val nums = listOf(Random.nextInt(10, 50), Random.nextInt(10, 50), Random.nextInt(10, 50), Random.nextInt(10, 50))
          val maxEven = nums.filter { it % 2 == 0 }.maxOrNull() ?: 42
          val allOptions = nums.map { it.toString() }
          result.add(
            Challenge(
              id = "proc_adp_${UUID.randomUUID()}",
              category = category,
              type = ChallengeType.RULE_SWITCH,
              difficulty = difficulty,
              title = "Dynamic Switch: Highest Even #$seed",
              prompt = "NEW ADAPTIVE CONDITION: Select the LARGEST EVEN number from the set.",
              options = allOptions,
              correctOptionIndex = allOptions.indexOf(maxEven.toString()).coerceAtLeast(0),
              timeLimitSeconds = 8,
              baseExplanation = "The highest even number among the choices is $maxEven."
            )
          )
        }
        else -> {
          val a = Random.nextInt(10, 30)
          val b = Random.nextInt(5, 15)
          val correct = (a + b).toString()
          val opts = listOf(correct, (a + b + 2).toString(), (a + b - 3).toString(), (a + b + 5).toString()).shuffled()
          result.add(
            Challenge(
              id = "proc_gen_${UUID.randomUUID()}",
              category = category,
              type = ChallengeType.MULTIPLE_CHOICE,
              difficulty = difficulty,
              title = "${category.displayName} Core Challenge #$seed",
              prompt = "Evaluate the synthesis: ($a + $b) = ?",
              options = opts,
              correctOptionIndex = opts.indexOf(correct),
              timeLimitSeconds = 10,
              baseExplanation = "Evaluation: $a + $b = $correct."
            )
          )
        }
      }
    }
    return result
  }
}
