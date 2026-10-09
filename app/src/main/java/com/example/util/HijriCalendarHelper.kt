package com.example.util

import android.os.Build
import java.time.LocalDate
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.util.Calendar

data class HijriDate(
  val year: Int,
  val month: Int, // 1 to 12
  val day: Int,
  val monthNameEn: String,
  val monthNameAr: String,
  val formattedEn: String,
  val formattedAr: String
)

data class GregorianDate(
  val year: Int,
  val month: Int, // 1 to 12
  val day: Int,
  val monthNameEn: String,
  val monthNameAr: String,
  val formattedEn: String,
  val formattedAr: String
)

data class DualDateResult(
  val hijri: HijriDate,
  val gregorian: GregorianDate,
  val dualDisplayEn: String,
  val dualDisplayAr: String
)

object HijriCalendarHelper {

  private val HIJRI_MONTHS_EN = listOf(
    "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
    "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
    "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
  )

  private val HIJRI_MONTHS_AR = listOf(
    "محرم", "صفر", "ربيع الأول", "ربيع الثاني",
    "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
    "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
  )

  private val GREGORIAN_MONTHS_EN = listOf(
    "January", "February", "March", "April",
    "May", "June", "July", "August",
    "September", "October", "November", "December"
  )

  private val GREGORIAN_MONTHS_AR = listOf(
    "يناير", "فبراير", "مارس", "أبريل",
    "مايو", "يونيو", "يوليو", "أغسطس",
    "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
  )

  fun getHijriMonths(isArabic: Boolean = false): List<String> =
    if (isArabic) HIJRI_MONTHS_AR else HIJRI_MONTHS_EN

  fun getGregorianMonths(isArabic: Boolean = false): List<String> =
    if (isArabic) GREGORIAN_MONTHS_AR else GREGORIAN_MONTHS_EN

  fun getHijriMonthName(month: Int, isArabic: Boolean = false): String {
    val idx = (month - 1).coerceIn(0, 11)
    return if (isArabic) HIJRI_MONTHS_AR[idx] else HIJRI_MONTHS_EN[idx]
  }

  fun getGregorianMonthName(month: Int, isArabic: Boolean = false): String {
    val idx = (month - 1).coerceIn(0, 11)
    return if (isArabic) GREGORIAN_MONTHS_AR[idx] else GREGORIAN_MONTHS_EN[idx]
  }

  /**
   * Converts Gregorian date (year, month 1-12, day 1-31) to official Umm Al-Qura Hijri date.
   */
  fun gregorianToHijri(year: Int, month: Int, day: Int): DualDateResult {
    return try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val safeMonth = month.coerceIn(1, 12)
        val maxDays = try {
          LocalDate.of(year, safeMonth, 1).lengthOfMonth()
        } catch (e: Exception) { 31 }
        val safeDay = day.coerceIn(1, maxDays)
        val localDate = LocalDate.of(year, safeMonth, safeDay)
        val hijrahDate = HijrahDate.from(localDate)

        val hYear = hijrahDate.get(ChronoField.YEAR)
        val hMonth = hijrahDate.get(ChronoField.MONTH_OF_YEAR)
        val hDay = hijrahDate.get(ChronoField.DAY_OF_MONTH)

        buildDualDateResult(year, safeMonth, safeDay, hYear, hMonth, hDay)
      } else {
        fallbackGregorianToHijri(year, month, day)
      }
    } catch (e: Exception) {
      fallbackGregorianToHijri(year, month, day)
    }
  }

  /**
   * Converts Hijri date (year e.g. 1448, month 1-12, day 1-30) to Gregorian date.
   */
  fun hijriToGregorian(hYear: Int, hMonth: Int, hDay: Int): DualDateResult {
    val safeHMonth = hMonth.coerceIn(1, 12)
    val safeHDay = hDay.coerceIn(1, 30)
    return try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val hijrahDate = try {
          HijrahChronology.INSTANCE.date(hYear, safeHMonth, safeHDay)
        } catch (e: Exception) {
          HijrahChronology.INSTANCE.date(hYear, safeHMonth, safeHDay.coerceAtMost(29))
        }
        val localDate = LocalDate.from(hijrahDate)

        val gYear = localDate.year
        val gMonth = localDate.monthValue
        val gDay = localDate.dayOfMonth

        buildDualDateResult(gYear, gMonth, gDay, hYear, safeHMonth, safeHDay)
      } else {
        fallbackHijriToGregorian(hYear, safeHMonth, safeHDay)
      }
    } catch (e: Exception) {
      fallbackHijriToGregorian(hYear, safeHMonth, safeHDay)
    }
  }

  /**
   * Returns today's date formatted in both Hijri and Gregorian.
   */
  fun getTodayDualDate(): DualDateResult {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val today = LocalDate.now()
      gregorianToHijri(today.year, today.monthValue, today.dayOfMonth)
    } else {
      val cal = Calendar.getInstance()
      gregorianToHijri(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
    }
  }

  /**
   * Formats an ISO string date (e.g. "2028-10-14") or returns dual date string.
   */
  fun formatIsoDateDual(isoDateString: String, isArabic: Boolean = false): String {
    return try {
      val parts = isoDateString.split("-")
      if (parts.size == 3) {
        val y = parts[0].toInt()
        val m = parts[1].toInt()
        val d = parts[2].toInt()
        val res = gregorianToHijri(y, m, d)
        if (isArabic) res.dualDisplayAr else res.dualDisplayEn
      } else {
        isoDateString
      }
    } catch (e: Exception) {
      isoDateString
    }
  }

  private fun buildDualDateResult(
    gYear: Int, gMonth: Int, gDay: Int,
    hYear: Int, hMonth: Int, hDay: Int
  ): DualDateResult {
    val hMonthEn = getHijriMonthName(hMonth, isArabic = false)
    val hMonthAr = getHijriMonthName(hMonth, isArabic = true)
    val gMonthEn = getGregorianMonthName(gMonth, isArabic = false)
    val gMonthAr = getGregorianMonthName(gMonth, isArabic = true)

    val formattedHijriEn = "%02d %s %d AH".format(hDay, hMonthEn, hYear)
    val formattedHijriAr = "%d %s %d هـ".format(hDay, hMonthAr, hYear)

    val formattedGregorianEn = "%02d %s %d AD".format(gDay, gMonthEn, gYear)
    val formattedGregorianAr = "%d %s %d م".format(gDay, gMonthAr, gYear)

    val dualEn = "$formattedHijriEn • $formattedGregorianEn"
    val dualAr = "$formattedHijriAr • $formattedGregorianAr"

    return DualDateResult(
      hijri = HijriDate(
        year = hYear,
        month = hMonth,
        day = hDay,
        monthNameEn = hMonthEn,
        monthNameAr = hMonthAr,
        formattedEn = formattedHijriEn,
        formattedAr = formattedHijriAr
      ),
      gregorian = GregorianDate(
        year = gYear,
        month = gMonth,
        day = gDay,
        monthNameEn = gMonthEn,
        monthNameAr = gMonthAr,
        formattedEn = formattedGregorianEn,
        formattedAr = formattedGregorianAr
      ),
      dualDisplayEn = dualEn,
      dualDisplayAr = dualAr
    )
  }

  // ICU fallback for older API runtimes
  private fun fallbackGregorianToHijri(year: Int, month: Int, day: Int): DualDateResult {
    return try {
      val icuCal = android.icu.util.IslamicCalendar()
      icuCal.setCalculationType(android.icu.util.IslamicCalendar.CalculationType.ISLAMIC_UMALQURA)
      icuCal.set(year, month - 1, day)
      val hYear = icuCal.get(android.icu.util.Calendar.YEAR)
      val hMonth = icuCal.get(android.icu.util.Calendar.MONTH) + 1
      val hDay = icuCal.get(android.icu.util.Calendar.DAY_OF_MONTH)
      buildDualDateResult(year, month, day, hYear, hMonth, hDay)
    } catch (e: Exception) {
      // Approximate mathematical formula as last resort
      val approxHYear = ((year - 622) * 1.0307).toInt()
      buildDualDateResult(year, month, day, approxHYear, month, day.coerceAtMost(29))
    }
  }

  private fun fallbackHijriToGregorian(hYear: Int, hMonth: Int, hDay: Int): DualDateResult {
    return try {
      val icuCal = android.icu.util.IslamicCalendar()
      icuCal.setCalculationType(android.icu.util.IslamicCalendar.CalculationType.ISLAMIC_UMALQURA)
      icuCal.set(android.icu.util.Calendar.YEAR, hYear)
      icuCal.set(android.icu.util.Calendar.MONTH, hMonth - 1)
      icuCal.set(android.icu.util.Calendar.DAY_OF_MONTH, hDay)
      val gYear = icuCal.get(Calendar.YEAR)
      val gMonth = icuCal.get(Calendar.MONTH) + 1
      val gDay = icuCal.get(Calendar.DAY_OF_MONTH)
      buildDualDateResult(gYear, gMonth, gDay, hYear, hMonth, hDay)
    } catch (e: Exception) {
      val approxGYear = (hYear / 1.0307 + 622).toInt()
      buildDualDateResult(approxGYear, hMonth, hDay.coerceAtMost(28), hYear, hMonth, hDay)
    }
  }
}
