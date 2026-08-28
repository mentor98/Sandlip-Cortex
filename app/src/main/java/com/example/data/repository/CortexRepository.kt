package com.example.data.repository

import com.example.data.local.dao.CortexDao
import com.example.data.local.entities.AchievementEntity
import com.example.data.local.entities.AIRivalEntity
import com.example.data.local.entities.BrainProfileEntity
import com.example.data.local.entities.BrainSnapshotEntity
import com.example.data.local.entities.ChallengeResultEntity
import com.example.data.local.entities.DailyMissionEntity
import com.example.data.local.entities.LeaderboardEntity
import com.example.data.local.entities.UserEntity
import com.example.data.local.entities.UserSettingsEntity
import com.example.domain.model.Achievement
import com.example.domain.model.AIRival
import com.example.domain.model.BrainDNA
import com.example.domain.model.BrainSnapshot
import com.example.domain.model.BuiltInAchievements
import com.example.domain.model.ChallengeEvaluation
import com.example.domain.model.ChallengeResult
import com.example.domain.model.CognitiveCategory
import com.example.domain.model.DailyMission
import com.example.domain.model.LeaderboardItem
import com.example.domain.model.UserProfile
import com.example.game.engine.BrainDNAEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CortexRepository(private val dao: CortexDao) {

  val userProfileFlow: Flow<UserProfile?> = dao.getUserFlow().map { it?.toDomain() }

  val brainProfileFlow: Flow<BrainDNA?> = dao.getBrainProfileFlow().map { it?.toDomain() }

  val snapshotsFlow: Flow<List<BrainSnapshot>> = dao.getAllSnapshotsFlow().map { list ->
    list.map { it.toDomain() }
  }

  val achievementsFlow: Flow<List<Achievement>> = dao.getAllAchievementsFlow().map { list ->
    if (list.isEmpty()) BuiltInAchievements else list.map { it.toDomain() }
  }

  val aiRivalFlow: Flow<AIRival?> = dao.getAIRivalFlow().map { it?.toDomain() }

  suspend fun initializeDefaultsIfNeeded() {
    val existingUser = dao.getUser()
    if (existingUser == null) {
      // Create initial guest/user
      val defaultUser = UserEntity(
        id = "local_user_1",
        username = "Emmanuel",
        email = "emmanuel@sandlip.io",
        avatarKey = "cyber_mind",
        level = 1,
        xp = 0,
        siScore = 500,
        isGuest = false
      )
      dao.insertUser(defaultUser)

      val defaultBrain = BrainProfileEntity(userId = defaultUser.id)
      dao.insertBrainProfile(defaultBrain)

      // Initialize achievements
      dao.insertAchievements(BuiltInAchievements.map { it.toEntity() })

      // Initialize AI Rival
      dao.insertAIRival(AIRivalEntity())

      // Initialize default settings
      dao.insertSettings(UserSettingsEntity())

      // Seed initial sample leaderboard
      seedLeaderboard()
    }
  }

  suspend fun completeBaseline(username: String, avatarKey: String, baselineDNA: BrainDNA) {
    val user = dao.getUser() ?: UserEntity(
      id = "local_user_1",
      username = username,
      email = "$username@sandlip.io",
      avatarKey = avatarKey
    )

    val updatedUser = user.copy(
      username = username,
      avatarKey = avatarKey,
      siScore = baselineDNA.siScore,
      xp = 100,
      level = 1
    )
    dao.insertUser(updatedUser)

    val profileEntity = baselineDNA.toEntity(updatedUser.id)
    dao.insertBrainProfile(profileEntity)

    // Record initial day snapshot
    val snapshot = BrainSnapshotEntity(
      userId = updatedUser.id,
      dayLabel = "Baseline",
      timestamp = System.currentTimeMillis(),
      siScore = baselineDNA.siScore,
      logic = baselineDNA.logic,
      memory = baselineDNA.memory,
      speed = baselineDNA.speed,
      pattern = baselineDNA.pattern,
      attention = baselineDNA.attention,
      strategy = baselineDNA.strategy,
      adaptability = baselineDNA.adaptability,
      spatial = baselineDNA.spatial,
      humanMind = baselineDNA.humanMind,
      creativity = baselineDNA.creativity
    )
    dao.insertSnapshot(snapshot)

    // Unlock First Thought
    unlockAchievement("first_thought")
  }

  suspend fun recordChallengeOutcome(
    challengeId: String,
    category: CognitiveCategory,
    difficulty: Int,
    isCorrect: Boolean,
    responseTimeMs: Long,
    timeLimitSeconds: Int,
    selectedOptionIndex: Int,
    textAnswer: String?,
    evaluation: ChallengeEvaluation,
    mode: String = "STANDARD"
  ): BrainDNA {
    val currentBrain = dao.getBrainProfile()?.toDomain() ?: BrainDNA()
    val (updatedBrain, _) = BrainDNAEngine.calculateUpdatedProfile(
      currentProfile = currentBrain,
      category = category,
      difficulty = difficulty,
      isCorrect = isCorrect,
      responseTimeMs = responseTimeMs,
      timeLimitSeconds = timeLimitSeconds
    )

    dao.insertBrainProfile(updatedBrain.toEntity("local_user_1"))

    // Record challenge result log
    dao.insertChallengeResult(
      ChallengeResultEntity(
        userId = "local_user_1",
        challengeId = challengeId,
        categoryCode = category.code,
        difficulty = difficulty,
        isCorrect = isCorrect,
        responseTimeMs = responseTimeMs,
        scoreEarned = evaluation.siScoreDelta,
        xpEarned = evaluation.xpEarned,
        userSelectedOptionIndex = selectedOptionIndex,
        userTextAnswer = textAnswer,
        explanation = evaluation.explanation,
        timestamp = System.currentTimeMillis(),
        mode = mode
      )
    )

    // Update user XP, Level, SI Score
    val currentUser = dao.getUser() ?: UserEntity(username = "Player", email = "p@sandlip.io", avatarKey = "cyber_mind")
    val newXp = currentUser.xp + evaluation.xpEarned
    val newLevel = (newXp / 200) + 1
    dao.updateUser(
      currentUser.copy(
        xp = newXp,
        level = newLevel,
        siScore = updatedBrain.siScore
      )
    )

    // Check achievement progressions
    checkAchievementsAfterPlay(updatedBrain, category, isCorrect, responseTimeMs, difficulty)

    return updatedBrain
  }

  suspend fun saveSessionSnapshot(dayLabel: String, brainDNA: BrainDNA) {
    val snapshot = BrainSnapshotEntity(
      userId = "local_user_1",
      dayLabel = dayLabel,
      timestamp = System.currentTimeMillis(),
      siScore = brainDNA.siScore,
      logic = brainDNA.logic,
      memory = brainDNA.memory,
      speed = brainDNA.speed,
      pattern = brainDNA.pattern,
      attention = brainDNA.attention,
      strategy = brainDNA.strategy,
      adaptability = brainDNA.adaptability,
      spatial = brainDNA.spatial,
      humanMind = brainDNA.humanMind,
      creativity = brainDNA.creativity
    )
    dao.insertSnapshot(snapshot)
  }

  suspend fun getDailyMission(): DailyMission {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val existing = dao.getDailyMission(today)
    if (existing != null) {
      return existing.toDomain()
    }

    // Generate fresh daily mission
    val categories = CognitiveCategory.entries.shuffled()
    val chosenCategory = categories.first()
    val newMission = DailyMissionEntity(
      id = "mission_$today",
      dateString = today,
      title = "Neural ${chosenCategory.displayName} Calibration",
      categoryCode = chosenCategory.code,
      targetCount = 3,
      currentProgress = 0,
      description = "Complete 3 ${chosenCategory.displayName} challenges with high accuracy to earn daily rewards.",
      xpReward = 100,
      brainPointsReward = 5,
      arenaRatingReward = 20,
      isCompleted = false,
      completedAt = null
    )
    dao.insertDailyMission(newMission)
    return newMission.toDomain()
  }

  suspend fun incrementDailyMissionProgress(category: CognitiveCategory) {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val mission = dao.getDailyMission(today) ?: return
    if (!mission.isCompleted && mission.categoryCode == category.code) {
      val newProgress = mission.currentProgress + 1
      val isNowComplete = newProgress >= mission.targetCount
      val updated = mission.copy(
        currentProgress = newProgress,
        isCompleted = isNowComplete,
        completedAt = if (isNowComplete) System.currentTimeMillis() else null
      )
      dao.updateDailyMission(updated)

      if (isNowComplete) {
        val user = dao.getUser() ?: return
        val newXp = user.xp + updated.xpReward
        dao.updateUser(user.copy(xp = newXp, level = (newXp / 200) + 1))
      }
    }
  }

  suspend fun updateRivalOutcome(userWon: Boolean, xpEarned: Int, siDelta: Int, confidenceDelta: Int) {
    val currentRival = dao.getAIRival() ?: AIRivalEntity()
    val newWins = currentRival.wins + if (!userWon) 1 else 0
    val newLosses = currentRival.losses + if (userWon) 1 else 0
    val newConfidence = (currentRival.confidenceScore + confidenceDelta).coerceIn(10, 99)
    val newSi = (currentRival.siScore - (siDelta / 2)).coerceIn(500, 950)

    dao.insertAIRival(
      currentRival.copy(
        wins = newWins,
        losses = newLosses,
        confidenceScore = newConfidence,
        siScore = newSi,
        lastDialogue = if (userWon) "You adapted faster this time. My models are recalibrating." else "I found your cognitive limit. Let us run another simulation."
      )
    )

    if (userWon) {
      unlockAchievement("nexus_breaker")
    }
  }

  fun getLeaderboardFlow(category: String): Flow<List<LeaderboardItem>> {
    return dao.getLeaderboardFlow(category).map { list -> list.map { it.toDomain() } }
  }

  private suspend fun checkAchievementsAfterPlay(
    brain: BrainDNA,
    category: CognitiveCategory,
    isCorrect: Boolean,
    responseTimeMs: Long,
    difficulty: Int
  ) {
    unlockAchievement("first_thought")

    if (brain.siScore >= 800) {
      unlockAchievement("oasis_champion")
    }

    if (isCorrect && difficulty >= 7 && responseTimeMs < 1500L) {
      unlockAchievement("speed_demon")
    }

    if (brain.bestStreak >= 10) {
      unlockAchievement("streak_titan")
    }
  }

  private suspend fun unlockAchievement(id: String) {
    val list = dao.getAllAchievements()
    val target = list.find { it.id == id }
    if (target != null && !target.isUnlocked) {
      val updated = target.copy(
        isUnlocked = true,
        currentProgress = target.maxProgress,
        unlockedTimestamp = System.currentTimeMillis()
      )
      dao.updateAchievement(updated)

      // Award XP
      val user = dao.getUser()
      if (user != null) {
        val newXp = user.xp + target.xpReward
        dao.updateUser(user.copy(xp = newXp, level = (newXp / 200) + 1))
      }
    }
  }

  private suspend fun seedLeaderboard() {
    val seeded = listOf(
      LeaderboardEntity("lb_1", 1, "KAI_CORTEX", "cyber_mind", 942, "GLOBAL", "Transcendence", false, "+8"),
      LeaderboardEntity("lb_2", 2, "AURA_99", "neural_entity", 918, "GLOBAL", "Mastermind", false, "+15"),
      LeaderboardEntity("lb_3", 3, "SYNAPSE_DEV", "architect", 895, "GLOBAL", "Mastermind", false, "+4"),
      LeaderboardEntity("lb_4", 4, "Emmanuel (You)", "cyber_mind", 782, "GLOBAL", "Cortex Elite", true, "+12"),
      LeaderboardEntity("lb_5", 5, "NEXUS_AI", "android", 774, "GLOBAL", "Adaptive Rival", false, "-8"),
      LeaderboardEntity("lb_6", 6, "VALERIA_MIND", "explorer", 760, "GLOBAL", "Cortex Elite", false, "+3"),
      LeaderboardEntity("lb_7", 7, "LOGIC_GHOST", "human", 735, "GLOBAL", "Thinker", false, "+20")
    )
    dao.insertLeaderboard(seeded)
  }

  // Extensions for Mappings
  private fun UserEntity.toDomain() = UserProfile(
    id = id,
    username = username,
    email = email,
    avatarKey = avatarKey,
    level = level,
    xp = xp,
    siScore = siScore,
    isGuest = isGuest,
    createdTimestamp = createdTimestamp
  )

  private fun BrainProfileEntity.toDomain() = BrainDNA(
    logic = logic,
    memory = memory,
    speed = speed,
    pattern = pattern,
    attention = attention,
    strategy = strategy,
    adaptability = adaptability,
    spatial = spatial,
    humanMind = humanMind,
    creativity = creativity,
    siScore = siScore,
    gamesPlayed = gamesPlayed,
    totalCorrect = totalCorrect,
    fastestResponseMs = fastestResponseMs,
    currentStreak = currentStreak,
    bestStreak = bestStreak,
    hardestSolvedDifficulty = hardestSolvedDifficulty,
    lastPlayedTimestamp = lastPlayedTimestamp
  )

  private fun BrainDNA.toEntity(userId: String) = BrainProfileEntity(
    userId = userId,
    logic = logic,
    memory = memory,
    speed = speed,
    pattern = pattern,
    attention = attention,
    strategy = strategy,
    adaptability = adaptability,
    spatial = spatial,
    humanMind = humanMind,
    creativity = creativity,
    siScore = siScore,
    gamesPlayed = gamesPlayed,
    totalCorrect = totalCorrect,
    fastestResponseMs = fastestResponseMs,
    currentStreak = currentStreak,
    bestStreak = bestStreak,
    hardestSolvedDifficulty = hardestSolvedDifficulty,
    lastPlayedTimestamp = lastPlayedTimestamp
  )

  private fun BrainSnapshotEntity.toDomain() = BrainSnapshot(
    id = id,
    dayLabel = dayLabel,
    timestamp = timestamp,
    siScore = siScore,
    logic = logic,
    memory = memory,
    speed = speed,
    pattern = pattern,
    attention = attention,
    strategy = strategy,
    adaptability = adaptability,
    spatial = spatial,
    humanMind = humanMind,
    creativity = creativity
  )

  private fun AchievementEntity.toDomain() = Achievement(
    id = id,
    title = title,
    description = description,
    category = category,
    xpReward = xpReward,
    iconName = iconName,
    isUnlocked = isUnlocked,
    currentProgress = currentProgress,
    maxProgress = maxProgress,
    unlockedTimestamp = unlockedTimestamp
  )

  private fun Achievement.toEntity() = AchievementEntity(
    id = id,
    title = title,
    description = description,
    category = category,
    xpReward = xpReward,
    iconName = iconName,
    isUnlocked = isUnlocked,
    currentProgress = currentProgress,
    maxProgress = maxProgress,
    unlockedTimestamp = unlockedTimestamp
  )

  private fun AIRivalEntity.toDomain() = AIRival(
    id = id,
    name = name,
    tagLine = tagLine,
    avatarKey = avatarKey,
    level = level,
    siScore = siScore,
    logic = logic,
    memory = memory,
    speed = speed,
    pattern = pattern,
    attention = attention,
    strategy = strategy,
    adaptability = adaptability,
    wins = wins,
    losses = losses,
    confidenceScore = confidenceScore,
    currentMood = currentMood,
    lastDialogue = lastDialogue
  )

  private fun DailyMissionEntity.toDomain() = DailyMission(
    id = id,
    dateString = dateString,
    title = title,
    category = CognitiveCategory.fromCode(categoryCode),
    targetCount = targetCount,
    currentProgress = currentProgress,
    description = description,
    xpReward = xpReward,
    brainPointsReward = brainPointsReward,
    arenaRatingReward = arenaRatingReward,
    isCompleted = isCompleted,
    completedAt = completedAt
  )

  private fun LeaderboardEntity.toDomain() = LeaderboardItem(
    rank = rank,
    username = username,
    avatarKey = avatarKey,
    siScore = siScore,
    tier = tier,
    isCurrentUser = isCurrentUser,
    changeString = changeString
  )
}
