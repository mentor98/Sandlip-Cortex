package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.AchievementEntity
import com.example.data.local.entities.AIRivalEntity
import com.example.data.local.entities.BrainProfileEntity
import com.example.data.local.entities.BrainSnapshotEntity
import com.example.data.local.entities.ChallengeResultEntity
import com.example.data.local.entities.DailyMissionEntity
import com.example.data.local.entities.LeaderboardEntity
import com.example.data.local.entities.UserEntity
import com.example.data.local.entities.UserSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CortexDao {

  // User
  @Query("SELECT * FROM users LIMIT 1")
  fun getUserFlow(): Flow<UserEntity?>

  @Query("SELECT * FROM users LIMIT 1")
  suspend fun getUser(): UserEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: UserEntity)

  @Update
  suspend fun updateUser(user: UserEntity)

  // Brain Profile
  @Query("SELECT * FROM brain_profiles LIMIT 1")
  fun getBrainProfileFlow(): Flow<BrainProfileEntity?>

  @Query("SELECT * FROM brain_profiles LIMIT 1")
  suspend fun getBrainProfile(): BrainProfileEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBrainProfile(profile: BrainProfileEntity)

  // Brain Snapshots / Mind Evolution
  @Query("SELECT * FROM brain_snapshots ORDER BY timestamp ASC")
  fun getAllSnapshotsFlow(): Flow<List<BrainSnapshotEntity>>

  @Query("SELECT * FROM brain_snapshots ORDER BY timestamp ASC")
  suspend fun getAllSnapshots(): List<BrainSnapshotEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSnapshot(snapshot: BrainSnapshotEntity)

  // Challenge Results
  @Query("SELECT * FROM challenge_results ORDER BY timestamp DESC")
  fun getAllResultsFlow(): Flow<List<ChallengeResultEntity>>

  @Query("SELECT * FROM challenge_results ORDER BY timestamp DESC LIMIT :limit")
  suspend fun getRecentResults(limit: Int): List<ChallengeResultEntity>

  @Query("SELECT * FROM challenge_results WHERE categoryCode = :categoryCode ORDER BY timestamp DESC LIMIT :limit")
  suspend fun getCategoryResults(categoryCode: String, limit: Int): List<ChallengeResultEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChallengeResult(result: ChallengeResultEntity)

  // Achievements
  @Query("SELECT * FROM achievements")
  fun getAllAchievementsFlow(): Flow<List<AchievementEntity>>

  @Query("SELECT * FROM achievements")
  suspend fun getAllAchievements(): List<AchievementEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAchievements(achievements: List<AchievementEntity>)

  @Update
  suspend fun updateAchievement(achievement: AchievementEntity)

  // AI Rival
  @Query("SELECT * FROM ai_rivals LIMIT 1")
  fun getAIRivalFlow(): Flow<AIRivalEntity?>

  @Query("SELECT * FROM ai_rivals LIMIT 1")
  suspend fun getAIRival(): AIRivalEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAIRival(rival: AIRivalEntity)

  // Daily Mission
  @Query("SELECT * FROM daily_missions WHERE dateString = :dateString LIMIT 1")
  fun getDailyMissionFlow(dateString: String): Flow<DailyMissionEntity?>

  @Query("SELECT * FROM daily_missions WHERE dateString = :dateString LIMIT 1")
  suspend fun getDailyMission(dateString: String): DailyMissionEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDailyMission(mission: DailyMissionEntity)

  @Update
  suspend fun updateDailyMission(mission: DailyMissionEntity)

  // Leaderboard
  @Query("SELECT * FROM leaderboard_entries WHERE categoryFilter = :category ORDER BY rank ASC")
  fun getLeaderboardFlow(category: String): Flow<List<LeaderboardEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLeaderboard(entries: List<LeaderboardEntity>)

  // Settings
  @Query("SELECT * FROM user_settings LIMIT 1")
  fun getSettingsFlow(): Flow<UserSettingsEntity?>

  @Query("SELECT * FROM user_settings LIMIT 1")
  suspend fun getSettings(): UserSettingsEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSettings(settings: UserSettingsEntity)

  // Data reset
  @Query("DELETE FROM challenge_results")
  suspend fun clearChallengeResults()

  @Query("DELETE FROM brain_snapshots")
  suspend fun clearSnapshots()
}
