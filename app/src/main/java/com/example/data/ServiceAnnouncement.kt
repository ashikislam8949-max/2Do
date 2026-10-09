package com.example.data

data class ServiceAnnouncement(
  val id: String,
  val titleEn: String,
  val titleAr: String,
  val messageEn: String,
  val messageAr: String,
  val categoryEn: String,
  val categoryAr: String,
  val isImportant: Boolean = false
)

object AnnouncementsRepository {
  val announcements = listOf(
    ServiceAnnouncement(
      id = "citizen-services",
      titleEn = "Citizen services added to the catalog",
      titleAr = "إضافة خدمات المواطنين إلى الدليل",
      messageEn = "Browse guidance for Absher, Najiz, Nafath, Tawakkalna, and National Address services. Complete identity checks and official submissions directly with the relevant government platform.",
      messageAr = "تصفح الإرشادات لخدمات أبشر وناجز ونفاذ وتوكلنا والعنوان الوطني. أكمل التحقق من الهوية والطلبات الرسمية مباشرة عبر المنصة الحكومية المعنية.",
      categoryEn = "Service catalog",
      categoryAr = "دليل الخدمات"
    ),
    ServiceAnnouncement(
      id = "request-tracking",
      titleEn = "Track service requests in one place",
      titleAr = "تابع طلبات الخدمات في مكان واحد",
      messageEn = "Open Service Requests to review the current status of submitted requests and their reference numbers.",
      messageAr = "افتح طلبات الخدمات لمراجعة حالة الطلبات المقدمة وأرقامها المرجعية.",
      categoryEn = "Requests",
      categoryAr = "الطلبات"
    ),
    ServiceAnnouncement(
      id = "account-security",
      titleEn = "Protect your digital identity",
      titleAr = "احمِ هويتك الرقمية",
      messageEn = "Never share your password, one-time passcode, or Nafath approval with anyone. Approve sign-in requests only when you initiated them in an official government service.",
      messageAr = "لا تشارك كلمة المرور أو رمز التحقق لمرة واحدة أو موافقة نفاذ مع أي شخص. وافق على طلبات الدخول فقط عند بدء استخدامها بنفسك في خدمة حكومية رسمية.",
      categoryEn = "Security",
      categoryAr = "الأمان",
      isImportant = true
    ),
    ServiceAnnouncement(
      id = "government-portals",
      titleEn = "Use official government portals",
      titleAr = "استخدم البوابات الحكومية الرسمية",
      messageEn = "The portal directory helps you find official service links. Service availability, eligibility, processing times, and government fees are set by each authority.",
      messageAr = "يساعدك دليل البوابات في العثور على روابط الخدمات الرسمية. تحدد كل جهة حكومية توفر الخدمات والأهلية ومدد المعالجة والرسوم.",
      categoryEn = "Digital government",
      categoryAr = "الحكومة الرقمية"
    )
  )
}
