package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class ServiceEntity(
  @PrimaryKey val id: String,
  val title: String,
  val category: String, // "Government Services", "IT & Cloud", "Cybersecurity", "Licensing & CR"
  val fee: Double,
  val governmentFee: Double,
  val duration: String,
  val imageUrl: String,
  val rating: Double,
  val reviewCount: Int,
  val description: String,
  val requirements: String,
  val isFeatured: Boolean,
  val isPopularGov: Boolean
)

@Entity(tableName = "service_requests")
data class ServiceRequestEntity(
  @PrimaryKey val requestId: String,
  val date: String,
  val serviceTitle: String,
  val companyName: String,
  val crNumber: String,
  val totalAmount: Double,
  val status: String, // "Under Review", "Document Verification", "Processing", "Completed"
  val paymentMethod: String
)

@Entity(tableName = "saved_services")
data class SavedServiceEntity(
  @PrimaryKey val serviceId: String,
  val title: String,
  val category: String,
  val fee: Double,
  val imageUrl: String,
  val rating: Double
)

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey val userId: String,
  val fullName: String,
  val email: String,
  val password: String,
  val companyName: String,
  val crNumber: String,
  val phone: String
)
