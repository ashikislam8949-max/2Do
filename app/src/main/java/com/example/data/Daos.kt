package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
  @Query("SELECT * FROM services")
  fun getAllServices(): Flow<List<ServiceEntity>>

  @Query("SELECT * FROM services WHERE isFeatured = 1")
  fun getFeaturedServices(): Flow<List<ServiceEntity>>

  @Query("SELECT * FROM services WHERE isPopularGov = 1")
  fun getPopularGovServices(): Flow<List<ServiceEntity>>

  @Query("SELECT * FROM services WHERE category = :category")
  fun getServicesByCategory(category: String): Flow<List<ServiceEntity>>

  @Query("SELECT * FROM services WHERE id = :serviceId")
  suspend fun getServiceById(serviceId: String): ServiceEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(services: List<ServiceEntity>)
}

@Dao
interface ServiceRequestDao {
  @Query("SELECT * FROM service_requests ORDER BY date DESC")
  fun getRequests(): Flow<List<ServiceRequestEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRequest(request: ServiceRequestEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(requests: List<ServiceRequestEntity>)

  @Query("DELETE FROM service_requests WHERE requestId = :requestId")
  suspend fun deleteRequest(requestId: String)
}

@Dao
interface SavedServiceDao {
  @Query("SELECT * FROM saved_services")
  fun getSavedServices(): Flow<List<SavedServiceEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSavedService(item: SavedServiceEntity)

  @Query("DELETE FROM saved_services WHERE serviceId = :serviceId")
  suspend fun removeSavedService(serviceId: String)

  @Query("SELECT EXISTS(SELECT * FROM saved_services WHERE serviceId = :serviceId)")
  fun isSaved(serviceId: String): Flow<Boolean>
}

@Dao
interface UserDao {
  @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
  suspend fun getUserByEmail(email: String): UserEntity?

  @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
  suspend fun getUserById(userId: String): UserEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: UserEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun updateUser(user: UserEntity)
}
