package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [ServiceEntity::class, ServiceRequestEntity::class, SavedServiceEntity::class, UserEntity::class],
  version = 4,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun serviceDao(): ServiceDao
  abstract fun serviceRequestDao(): ServiceRequestDao
  abstract fun savedServiceDao(): SavedServiceDao
  abstract fun userDao(): UserDao

  companion object {
    @Volatile private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance =
          Room.databaseBuilder(
              context.applicationContext,
              AppDatabase::class.java,
              "starbridge_gateway_db"
            )
            .fallbackToDestructiveMigration()
            .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
