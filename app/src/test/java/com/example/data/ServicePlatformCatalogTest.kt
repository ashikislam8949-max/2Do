package com.example.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ServicePlatformCatalogTest {
  @Test
  fun citizenServicesHaveUniqueIdsAndTheExpectedCategory() {
    val services = CitizenServiceCatalog.services

    assertTrue(services.isNotEmpty())
    assertEquals(services.size, services.map { it.id }.toSet().size)
    assertTrue(services.all { it.category == "Citizen Services" })
    assertTrue(services.all { it.title.isNotBlank() && it.description.isNotBlank() })
  }

  @Test
  fun announcementsAreBilingualAndIncludeIdentitySecurityGuidance() {
    val announcements = AnnouncementsRepository.announcements

    assertTrue(announcements.isNotEmpty())
    assertEquals(announcements.size, announcements.map { it.id }.toSet().size)
    assertTrue(
      announcements.all {
        it.titleEn.isNotBlank() &&
          it.titleAr.isNotBlank() &&
          it.messageEn.isNotBlank() &&
          it.messageAr.isNotBlank()
      }
    )
    assertTrue(announcements.any { it.isImportant && it.id == "account-security" })
  }
}
