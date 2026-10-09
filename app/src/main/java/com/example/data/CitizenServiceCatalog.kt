package com.example.data

object CitizenServiceCatalog {
  val services = listOf(
    ServiceEntity(
      id = "citizen_absher",
      title = "Absher Civil Affairs & Appointment Assistance",
      category = "Citizen Services",
      fee = 0.0,
      governmentFee = 0.0,
      duration = "Varies by service",
      imageUrl = "https://images.unsplash.com/photo-1529107386315-e1a2ed48a620?auto=format&fit=crop&w=600&q=80",
      rating = 4.8,
      reviewCount = 86,
      description = "Guidance for finding civil affairs services and preparing for appointments through the official Absher channels. Government eligibility and fees are determined by the relevant authority.",
      requirements = "1. Review the relevant Absher service requirements\n2. Prepare the requested identity documents\n3. Complete any submission through the official Absher portal",
      isFeatured = false,
      isPopularGov = true
    ),
    ServiceEntity(
      id = "citizen_najiz",
      title = "Najiz Digital Justice Service Guidance",
      category = "Citizen Services",
      fee = 0.0,
      governmentFee = 0.0,
      duration = "Varies by service",
      imageUrl = "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?auto=format&fit=crop&w=600&q=80",
      rating = 4.8,
      reviewCount = 74,
      description = "Help identifying the right Najiz digital service and preparing supporting documents for judicial and legal transactions. Applications are submitted through the official Najiz portal.",
      requirements = "1. Identify the relevant Najiz service\n2. Prepare supporting documents requested by the service\n3. Submit through the official Najiz portal",
      isFeatured = false,
      isPopularGov = true
    ),
    ServiceEntity(
      id = "citizen_national_address",
      title = "Saudi National Address Registration & Update",
      category = "Citizen Services",
      fee = 0.0,
      governmentFee = 0.0,
      duration = "Varies by service",
      imageUrl = "https://images.unsplash.com/photo-1524666041070-9f876c2b2b2b?auto=format&fit=crop&w=600&q=80",
      rating = 4.7,
      reviewCount = 63,
      description = "Guidance for registering or updating a Saudi National Address through Saudi Post (SPL), including the information commonly needed to complete the official request.",
      requirements = "1. Access to the official SPL National Address service\n2. Building and location details\n3. Identity verification as requested by SPL",
      isFeatured = false,
      isPopularGov = true
    ),
    ServiceEntity(
      id = "citizen_nafath",
      title = "Nafath Digital Identity Access Support",
      category = "Citizen Services",
      fee = 0.0,
      governmentFee = 0.0,
      duration = "Varies by service",
      imageUrl = "https://images.unsplash.com/photo-1550751827-4bd374c3f58b?auto=format&fit=crop&w=600&q=80",
      rating = 4.8,
      reviewCount = 52,
      description = "Guidance for using Nafath to access participating Saudi digital services. Identity approvals and authentication must be completed by the user in the official Nafath application.",
      requirements = "1. Install the official Nafath application\n2. Use your own registered identity and device\n3. Approve authentication requests only when you initiated them",
      isFeatured = false,
      isPopularGov = false
    ),
    ServiceEntity(
      id = "citizen_tawakkalna",
      title = "Tawakkalna Services & Digital Documents Guidance",
      category = "Citizen Services",
      fee = 0.0,
      governmentFee = 0.0,
      duration = "Varies by service",
      imageUrl = "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=600&q=80",
      rating = 4.7,
      reviewCount = 48,
      description = "Help locating relevant Tawakkalna services and understanding digital document access. Availability and eligibility are managed by the official Tawakkalna platform.",
      requirements = "1. Use the official Tawakkalna application\n2. Sign in using your own verified account\n3. Follow the service-specific instructions in the application",
      isFeatured = false,
      isPopularGov = false
    )
  )
}
