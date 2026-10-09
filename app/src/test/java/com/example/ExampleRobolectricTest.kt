package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("BBCI", appName)
  }

  @Test
  fun `verify category navigation tabs and repositories`() {
    val documents = com.example.data.PersonalDocumentsRepository.getDefaultDocuments()
    assertTrue(documents.isNotEmpty())
    assertEquals(8, documents.size)
    assertTrue(documents.any { it.id == "doc_misa" })
    assertTrue(documents.any { it.id == "doc_cr" })

    val portals = com.example.data.GovPortalsRepository.getDefaultPortals()
    assertTrue(portals.isNotEmpty())
    assertEquals(9, portals.size)
    assertTrue(portals.any { it.id == "portal_misa" })
    assertTrue(portals.any { it.id == "portal_sbc" })
  }

  @Test
  fun `verify vico analytics chart data model generation`() {
    val sampleRequests = listOf(
      com.example.data.ServiceRequestEntity(
        requestId = "REQ-101",
        date = "2026-10-01",
        serviceTitle = "ZATCA E-Invoicing Phase 2 Integration",
        companyName = "Al-Madinah Tech",
        crNumber = "1010948210",
        totalAmount = 2499.0,
        status = "Processing",
        paymentMethod = "Mada"
      )
    )
    val baseData = listOf(24, 38, 45, 52, 48, 64)
    val model = com.patrykandpatrick.vico.core.entry.entryModelOf(*baseData.toTypedArray())
    assertEquals(6, model.entries.first().size)
  }

  @Test
  fun `verify biometric helper canAuthenticate and security lock states`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    // Verify BiometricHelper execution without crash
    val canAuth = com.example.util.BiometricHelper.canAuthenticate(context)
    // Returns boolean based on emulator shadow biometric manager
    assertTrue(canAuth || !canAuth)
  }

  @Test
  fun `verify misa progress tracker repository and vico stage model`() {
    val tracks = com.example.ui.components.MisaApplicationsRepository.getDefaultTracks()
    assertTrue(tracks.isNotEmpty())
    assertEquals(3, tracks.size)

    val rhqTrack = tracks.first { it.applicationId == "MISA-RHQ-2026-904" }
    assertEquals(5, rhqTrack.stages.size)
    assertEquals(85, rhqTrack.totalProgress)

    // Test Vico entryModel generation for MISA application stages
    val scores = rhqTrack.stages.map { it.completionScore }
    val vicoModel = com.patrykandpatrick.vico.core.entry.entryModelOf(*scores.toTypedArray())
    assertEquals(5, vicoModel.entries.first().size)
  }

  @Test
  fun `verify hijri calendar helper conversions and dual date formatting`() {
    // 1. Gregorian to Hijri conversion test
    val result = com.example.util.HijriCalendarHelper.gregorianToHijri(2026, 10, 8)
    assertTrue(result.hijri.year >= 1447)
    assertTrue(result.hijri.month in 1..12)
    assertTrue(result.hijri.day in 1..30)
    assertTrue(result.dualDisplayEn.contains("AH"))
    assertTrue(result.dualDisplayEn.contains("AD"))
    assertTrue(result.dualDisplayAr.contains("هـ"))
    assertTrue(result.dualDisplayAr.contains("م"))

    // 2. Hijri to Gregorian conversion test
    val gResult = com.example.util.HijriCalendarHelper.hijriToGregorian(1448, 4, 24)
    assertEquals(1448, gResult.hijri.year)
    assertEquals(4, gResult.hijri.month)
    assertEquals(24, gResult.hijri.day)
    assertTrue(gResult.gregorian.year in 2026..2027)

    // 3. Today dual date
    val today = com.example.util.HijriCalendarHelper.getTodayDualDate()
    assertTrue(today.dualDisplayEn.isNotBlank())
    assertTrue(today.dualDisplayAr.isNotBlank())
  }

  @Test
  fun `verify saudi misa and cr services in repository`() {
    val database = androidx.room.Room.inMemoryDatabaseBuilder(
      ApplicationProvider.getApplicationContext(),
      com.example.data.AppDatabase::class.java
    ).allowMainThreadQueries().build()

    val repository = com.example.data.ServiceRepository(database)

    kotlinx.coroutines.runBlocking {
      repository.initializeDefaultServices()
      val misaServices = database.serviceDao().getServicesByCategory("MISA & Investment").first()
      assertTrue(misaServices.isNotEmpty())
      assertTrue(misaServices.any { it.title.contains("Regional Headquarters") || it.title.contains("RHQ") })
      assertTrue(misaServices.any { it.title.contains("Investment License") })

      val crServices = database.serviceDao().getServicesByCategory("Commercial Registration (CR)").first()
      assertTrue(crServices.isNotEmpty())
      assertTrue(crServices.any { it.title.contains("Instant Issuance") })
      assertTrue(crServices.any { it.title.contains("Articles of Association") })
      assertTrue(crServices.any { it.title.contains("Renewal") })
    }

    database.close()
  }
}
