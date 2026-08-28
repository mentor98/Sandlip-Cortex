package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey val id: String = "local_user_1",
  val username: String,
  val email: String,
  val avatarKey: String,
  val level: Int = 1,
  val xp: Int = 0,
  val siScore: Int = 500,
  val isGuest: Boolean = false,
  val createdTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "brain_profiles")
data class BrainProfileEntity(
  @PrimaryKey val userId: String = "local_user_1",
  val logic: Int = 50,
  val memory: Int = 50,
  val speed: Int = 50,
  val pattern: Int = 50,
  val attention: Int = 50,
  val strategy: Int = 50,
  val adaptability: Int = 50,
  val spatial: Int = 50,
  val humanMind: Int = 50,
  val creativity: Int = 50,
  val siScore: Int = 500,
  val gamesPlayed: Int = 0,
  val totalCorrect: Int = 0,
  val fastestResponseMs: Long = 0L,
  val currentStreak: Int = 0,
  val bestStreak: Int = 0,
  val hardestSolvedDifficulty: Int = 1,
  val lastPlayedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "brain_snapshots")
data class BrainSnapshotEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val userId: String = "local_user_1",
  val dayLabel: String,
  val timestamp: Long,
  val siScore: Int,
  val logic: Int,
  val memory: Int,
  val speed: Int,
  val pattern: Int,
  val attention: Int,
  val strategy: Int,
  val adaptability: Int,
  val spatial: Int,
  val humanMind: Int,
  val creativity: Int
)

@Entity(tableName = "challenge_results")
data class ChallengeResultEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val userId: String = "local_user_1",
  val challengeId: String,
  val categoryCode: String,
  val difficulty: Int,
  val isCorrect: Boolean,
  val responseTimeMs: Long,
  val scoreEarned: Int,
  val xpEarned: Int,
  val userSelectedOptionIndex: Int,
  val userTextAnswer: String?,
  val explanation: String,
  val timestamp: Long = System.currentTimeMillis(),
  val mode: String = "STANDARD"
)

@Entity(tableName = "achievements")
data class AchievementEntity(
  @PrimaryKey val id: String,
  val title: String,
  val description: String,
  val category: String,
  val xpReward: Int,
  val iconName: String,
  val isUnlocked: Boolean,
  val currentProgress: Int,
  val maxProgress: Int,
  val unlockedTimestamp: Long?
)

@Entity(tableName = "ai_rivals")
data class AIRivalEntity(
  @PrimaryKey val id: String = "rival_nexus",
  val name: String = "NEXUS",
  val tagLine: String = "Your Adaptive Cognitive Rival",
  val avatarKey: String = "cyber_mind",
  val level: Int = 5,
  val siScore: Int = 740,
  val logic: Int = 78,
  val memory: Int = 72,
  val speed: Int = 84,
  val pattern: Int = 88,
  val attention: Int = 80,
  val strategy: Int = 76,
  val adaptability: Int = 85,
  val wins: Int = 0,
  val losses: Int = 0,
  val confidenceScore: Int = 75,
  val currentMood: String = "Analytical",
  val lastDialogue: String = "I have observed your pattern recognition latency."
)

@Entity(tableName = "daily_missions")
data class DailyMissionEntity(
  @PrimaryKey val id: String,
  val dateString: String,
  val title: String,
  val categoryCode: String,
  val targetCount: Int,
  val currentProgress: Int,
  val description: String,
  val xpReward: Int,
  val brainPointsReward: Int,
  val arenaRatingReward: Int,
  val isCompleted: Boolean,
  val completedAt: Long?
)

@Entity(tableName = "leaderboard_entries")
data class LeaderboardEntity(
  @PrimaryKey val id: String,
  val rank: Int,
  val username: String,
  val avatarKey: String,
  val siScore: Int,
  val categoryFilter: String = "GLOBAL",
  val tier: String = "Cortex Elite",
  val isCurrentUser: Boolean = false,
  val changeString: String = "+12"
)

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
  @PrimaryKey val id: String = "default_settings",
  val soundEnabled: Boolean = true,
  val hapticsEnabled: Boolean = true,
  val aiExplanationsEnabled: Boolean = true,
  val reducedMotion: Boolean = false,
  val darkTheme: Boolean = true,
  val notificationsEnabled: Boolean = true
)
