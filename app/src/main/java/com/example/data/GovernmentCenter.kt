package com.example.data

data class GovernmentCenter(
  val id: String = "",
  val name: String,
  val nameAr: String = "",
  val type: String, // "Passport Office (Jawazat)", "Tax Bureau (ZATCA)", "Ministry of Commerce", "MISA Investment Center", "Absher Kiosk"
  val city: String,
  val address: String,
  val addressAr: String = "",
  val latitude: Double = 24.7136,
  val longitude: Double = 46.6753,
  val distanceKm: Double,
  val operatingHours: String,
  val status: String = "Open Now",
  val phone: String = "+966 11 405 7000",
  val servicesOffered: List<String> = listOf("Iqama Renewal", "Biometrics", "Document Attestation"),
  val rating: Double = 4.8
)
