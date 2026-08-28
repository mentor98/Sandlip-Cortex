package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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

@Database(
  entities = [
    UserEntity::class,
    BrainProfileEntity::class,
    BrainSnapshotEntity::class,
    ChallengeResultEntity::class,
    AchievementEntity::class,
    AIRivalEntity::class,
    DailyMissionEntity::class,
    LeaderboardEntity::class,
    UserSettingsEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class CortexDatabase : RoomDatabase() {

  abstract fun cortexDao(): CortexDao

  companion object {
    @Volatile
    private var INSTANCE: CortexDatabase? = null

    fun getInstance(context: Context): CortexDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          CortexDatabase::class.java,
          "sandlip_cortex.db"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
