package com.example.data

import android.location.Location

object GovernmentCentersRepository {

  val allCenters: List<GovernmentCenter> = listOf(
    // Riyadh Branches
    GovernmentCenter(
      id = "riyadh_jawazat_main",
      name = "Riyadh General Directorate of Passports (Jawazat)",
      nameAr = "المديرية العامة للجوازات - الرياض",
      type = "Passport Office (Jawazat)",
      city = "Riyadh",
      address = "Al-Wizarat District, King Abdulaziz Rd, Riyadh 12622",
      addressAr = "حي الوزارات، طريق الملك عبدالعزيز، الرياض",
      latitude = 24.6705,
      longitude = 46.7082,
      distanceKm = 1.2,
      operatingHours = "08:00 AM - 02:30 PM",
      status = "Open Now",
      phone = "+966 11 477 1100",
      servicesOffered = listOf("Iqama Issuance & Renewal", "Exit/Re-Entry Visa", "Biometrics Enrollment", "Final Exit Visa", "Muqeem Services"),
      rating = 4.7
    ),
    GovernmentCenter(
      id = "riyadh_jawazat_remittance",
      name = "Jawazat Expatriate Affairs Center - Al Murabba",
      nameAr = "مركز خدمات الوافدين للجوازات - المربع",
      type = "Passport Office (Jawazat)",
      city = "Riyadh",
      address = "Al Murabba Dist, Near National Museum, Riyadh 12613",
      addressAr = "حي المربع، بجوار المتحف الوطني، الرياض",
      latitude = 24.6548,
      longitude = 46.7118,
      distanceKm = 2.1,
      operatingHours = "08:00 AM - 02:00 PM",
      status = "Open Now",
      phone = "+966 11 405 6000",
      servicesOffered = listOf("Iqama Status Verification", "Absher Fingerprint Verification", "Transfer of Sponsorship"),
      rating = 4.6
    ),
    GovernmentCenter(
      id = "riyadh_zatca_hq",
      name = "ZATCA Main Tax & Zakat Bureau",
      nameAr = "هيئة الزكاة والضريبة والجمارك - المقر الرئيسي",
      type = "Tax Bureau (ZATCA)",
      city = "Riyadh",
      address = "King Fahd Road, Al Olaya, Riyadh 11185",
      addressAr = "طريق الملك فهد، العليا، الرياض",
      latitude = 24.7112,
      longitude = 46.6744,
      distanceKm = 2.8,
      operatingHours = "08:00 AM - 04:00 PM",
      status = "Open Now",
      phone = "19993",
      servicesOffered = listOf("VAT Registration & Returns", "E-Invoicing Fatoorah Verification", "Zakat Assessments", "Withholding Tax Support"),
      rating = 4.8
    ),
    GovernmentCenter(
      id = "riyadh_mc_olaya",
      name = "Ministry of Commerce Customer Excellence Center",
      nameAr = "مركز خدمات العملاء - وزارة التجارة",
      type = "Ministry of Commerce",
      city = "Riyadh",
      address = "Olaya Street, Al Olaya District, Riyadh 12211",
      addressAr = "شارع العليا، حي العليا، الرياض",
      latitude = 24.7003,
      longitude = 46.6854,
      distanceKm = 3.9,
      operatingHours = "08:00 AM - 03:00 PM",
      status = "Open Now",
      phone = "1900",
      servicesOffered = listOf("Commercial Registration (CR)", "Trade Name Reservation", "Articles of Association", "Trademark Attestation"),
      rating = 4.9
    ),
    GovernmentCenter(
      id = "riyadh_misa_investor",
      name = "MISA Investor Service Center (Business Front)",
      nameAr = "مركز خدمات المستثمرين - وزارة الاستثمار",
      type = "MISA Investment Center",
      city = "Riyadh",
      address = "Airport Road, Riyadh Front Business District, Riyadh 13413",
      addressAr = "طريق المطار، واجهة الرياض للأعمال، الرياض",
      latitude = 24.8385,
      longitude = 46.7266,
      distanceKm = 6.4,
      operatingHours = "08:00 AM - 03:30 PM",
      status = "Open Now",
      phone = "199099",
      servicesOffered = listOf("Foreign Investor License (MISA)", "Expedited Iqama for Investors", "Tax Exemption Consultation", "Vision 2030 Incentive Advisory"),
      rating = 4.9
    ),
    GovernmentCenter(
      id = "riyadh_absher_kiosk_park",
      name = "Absher Government Self-Service Kiosk",
      nameAr = "جهاز أبشر للخدمات الذاتية - الرياض بارك",
      type = "Absher Kiosk",
      city = "Riyadh",
      address = "Riyadh Park Mall, Northern Ring Rd, Riyadh 13511",
      addressAr = "مول الرياض بارك، الطريق الدائري الشمالي، الرياض",
      latitude = 24.7577,
      longitude = 46.6305,
      distanceKm = 5.2,
      operatingHours = "09:00 AM - 11:00 PM",
      status = "Open Now",
      phone = "920020405",
      servicesOffered = listOf("Absher Account Activation", "Instant Biometric Update", "Mobile Number Verification", "Digital ID Printing"),
      rating = 4.5
    ),

    // Jeddah Branches
    GovernmentCenter(
      id = "jeddah_jawazat_main",
      name = "Jeddah Directorate of Passports (Jawazat)",
      nameAr = "جوازات منطقة مكة المكرمة - جدة",
      type = "Passport Office (Jawazat)",
      city = "Jeddah",
      address = "Al-Baghdadiyah Al-Gharbiyah, Al-Mina Rd, Jeddah 22234",
      addressAr = "حي البغدادية الغربية، طريق الميناء، جدة",
      latitude = 21.5033,
      longitude = 39.1867,
      distanceKm = 2.1,
      operatingHours = "08:00 AM - 02:30 PM",
      status = "Open Now",
      phone = "+966 12 647 8899",
      servicesOffered = listOf("Iqama Issuance & Renewal", "Exit/Re-Entry Visa", "Transfer of Sponsorship", "Biometrics"),
      rating = 4.6
    ),
    GovernmentCenter(
      id = "jeddah_zatca_branch",
      name = "ZATCA Western Regional Tax Center",
      nameAr = "هيئة الزكاة والضريبة والجمارك - فرع جدة",
      type = "Tax Bureau (ZATCA)",
      city = "Jeddah",
      address = "Madinah Road, Al Rawdah, Jeddah 23432",
      addressAr = "طريق المدينة المنورة، الروضة، جدة",
      latitude = 21.5433,
      longitude = 39.1729,
      distanceKm = 4.0,
      operatingHours = "08:00 AM - 04:00 PM",
      status = "Open Now",
      phone = "19993",
      servicesOffered = listOf("VAT Audit & Registration", "Customs Clearance & Duties", "E-Invoicing Fatoorah Support"),
      rating = 4.7
    ),
    GovernmentCenter(
      id = "jeddah_mc_andalus",
      name = "Ministry of Commerce Western Province Branch",
      nameAr = "فرع وزارة التجارة بالمنطقة الغربية - جدة",
      type = "Ministry of Commerce",
      city = "Jeddah",
      address = "Al Andalus District, King Fahd Rd, Jeddah 23212",
      addressAr = "حي الأندلس، طريق الملك فهد، جدة",
      latitude = 21.5298,
      longitude = 39.1601,
      distanceKm = 3.5,
      operatingHours = "08:00 AM - 03:00 PM",
      status = "Open Now",
      phone = "1900",
      servicesOffered = listOf("Commercial Registration", "Chamber of Commerce Attestation", "Industrial Licensing"),
      rating = 4.8
    ),

    // Dammam & Eastern Province
    GovernmentCenter(
      id = "dammam_jawazat_main",
      name = "Eastern Province Passports Directorate (Jawazat)",
      nameAr = "جوازات المنطقة الشرقية - الدمام",
      type = "Passport Office (Jawazat)",
      city = "Dammam",
      address = "Al-Shati District, Prince Nayef Rd, Dammam 32413",
      addressAr = "حي الشاطئ، طريق الأمير نايف، الدمام",
      latitude = 26.4489,
      longitude = 50.1165,
      distanceKm = 1.8,
      operatingHours = "08:00 AM - 02:30 PM",
      status = "Open Now",
      phone = "+966 13 833 4455",
      servicesOffered = listOf("Iqama Services", "Muqeem Passports", "Biometrics", "Expedited Corporate Services"),
      rating = 4.7
    ),
    GovernmentCenter(
      id = "dammam_zatca_center",
      name = "ZATCA Eastern Province Regional Center",
      nameAr = "هيئة الزكاة والضريبة والجمارك - الدمام",
      type = "Tax Bureau (ZATCA)",
      city = "Dammam",
      address = "King Abdulaziz St, Al-Danah Dist, Dammam 32242",
      addressAr = "شارع الملك عبدالعزيز، حي الدانة، الدمام",
      latitude = 26.4342,
      longitude = 50.1033,
      distanceKm = 3.5,
      operatingHours = "08:00 AM - 04:00 PM",
      status = "Open Now",
      phone = "19993",
      servicesOffered = listOf("Corporate Tax", "VAT Returns", "King Fahd Causeway Customs Tax Support"),
      rating = 4.6
    ),
    GovernmentCenter(
      id = "khobar_mc_corniche",
      name = "Ministry of Commerce - Al Khobar Service Branch",
      nameAr = "فرع وزارة التجارة - الخبر",
      type = "Ministry of Commerce",
      city = "Dammam",
      address = "Corniche Rd, Al Khobar Shamalia, Khobar 34414",
      addressAr = "طريق الكورنيش، الخبر الشمالية، الخبر",
      latitude = 26.2917,
      longitude = 50.2185,
      distanceKm = 7.8,
      operatingHours = "08:00 AM - 03:00 PM",
      status = "Open Now",
      phone = "1900",
      servicesOffered = listOf("CR Registration", "Business License Renewals", "Consumer Protection"),
      rating = 4.8
    ),

    // Makkah
    GovernmentCenter(
      id = "makkah_jawazat",
      name = "Makkah Al-Mukarramah Passports Directorate",
      nameAr = "جوازات العاصمة المقدسة - مكة المكرمة",
      type = "Passport Office (Jawazat)",
      city = "Makkah",
      address = "Al Shumaisi / Al Umrah Rd, Makkah 24412",
      addressAr = "طريق الشميسي، العمرة، مكة المكرمة",
      latitude = 21.4372,
      longitude = 39.8166,
      distanceKm = 3.2,
      operatingHours = "08:00 AM - 02:30 PM",
      status = "Open Now",
      phone = "+966 12 522 1100",
      servicesOffered = listOf("Hajj/Umrah Muqeem Permits", "Iqama Extensions", "Biometrics"),
      rating = 4.7
    ),

    // Madinah
    GovernmentCenter(
      id = "madinah_jawazat",
      name = "Madinah Regional Passports Directorate",
      nameAr = "إدارة جوازات منطقة المدينة المنورة",
      type = "Passport Office (Jawazat)",
      city = "Madinah",
      address = "Sultana Road, Al Qiblatayn Dist, Madinah 42351",
      addressAr = "طريق سلطانة، حي القبلتين، المدينة المنورة",
      latitude = 24.4842,
      longitude = 39.5961,
      distanceKm = 2.4,
      operatingHours = "08:00 AM - 02:30 PM",
      status = "Open Now",
      phone = "+966 14 845 2233",
      servicesOffered = listOf("Iqama Renewals", "Visa Cancellations", "Absher Support"),
      rating = 4.8
    )
  )

  /**
   * Recalculates distance for all centers given the user's current GPS location.
   */
  fun getCentersWithCalculatedDistance(
    userLat: Double,
    userLng: Double,
    cityFilter: String? = null,
    categoryFilter: String? = null
  ): List<GovernmentCenter> {
    val results = mutableListOf<GovernmentCenter>()
    val distanceResult = FloatArray(1)

    for (center in allCenters) {
      if (cityFilter != null && cityFilter != "All" && !center.city.equals(cityFilter, ignoreCase = true)) {
        continue
      }
      if (categoryFilter != null && categoryFilter != "All" && !center.type.contains(categoryFilter, ignoreCase = true)) {
        continue
      }

      Location.distanceBetween(userLat, userLng, center.latitude, center.longitude, distanceResult)
      val distKm = Math.round((distanceResult[0] / 1000.0) * 10.0) / 10.0

      results.add(center.copy(distanceKm = distKm))
    }

    return results.sortedBy { it.distanceKm }
  }

  fun getCityDefaultCoordinates(cityName: String): Pair<Double, Double> {
    return when {
      cityName.contains("Jeddah", ignoreCase = true) -> Pair(21.5433, 39.1729)
      cityName.contains("Dammam", ignoreCase = true) || cityName.contains("Eastern", ignoreCase = true) -> Pair(26.4207, 50.0888)
      cityName.contains("Makkah", ignoreCase = true) || cityName.contains("Mecca", ignoreCase = true) -> Pair(21.4225, 39.8262)
      cityName.contains("Madinah", ignoreCase = true) || cityName.contains("Medina", ignoreCase = true) -> Pair(24.4672, 39.6111)
      else -> Pair(24.7136, 46.6753) // Riyadh Default
    }
  }
}
