package com.example.data

enum class DocumentType(val labelEn: String, val labelAr: String) {
  IQAMA("Digital Iqama", "الإقامة الرقمية"),
  PASSPORT("Passport Document", "جواز السفر"),
  DRIVING_LICENSE("Driving License", "رخصة القيادة"),
  COMMERCIAL_REGISTRATION("Commercial Registration", "السجل التجاري"),
  MISA_LICENSE("MISA Investment License", "ترخيص وزارة الاستثمار"),
  NATIONAL_ADDRESS("National Address", "العنوان الوطني"),
  GOSI_CERTIFICATE("GOSI Certificate", "شهادة التأمينات"),
  QIWA_CONTRACT("Qiwa Work Contract", "عقد عمل قوى")
}

data class PersonalDocument(
  val id: String,
  val titleEn: String,
  val titleAr: String,
  val docType: DocumentType,
  val documentNumber: String,
  val holderNameEn: String,
  val holderNameAr: String,
  val issuingAuthorityEn: String,
  val issuingAuthorityAr: String,
  val issueDate: String,
  val expiryDate: String,
  val status: String, // "Valid / Active", "Expiring Soon", "Verified"
  val isExpiringSoon: Boolean = false,
  val qrData: String,
  val primaryColorHex: Long = 0xFF0A5C36,
  val fields: Map<String, String> = emptyMap()
)

object PersonalDocumentsRepository {
  fun getDefaultDocuments(): List<PersonalDocument> = listOf(
    PersonalDocument(
      id = "doc_iqama",
      titleEn = "Muqeem Digital Iqama ID",
      titleAr = "بطاقة الإقامة الرقمية - مقيم",
      docType = DocumentType.IQAMA,
      documentNumber = "2489310245",
      holderNameEn = "Muhammad Tariq Khan",
      holderNameAr = "محمد طارق خان",
      issuingAuthorityEn = "Ministry of Interior • Passports (Jawazat)",
      issuingAuthorityAr = "وزارة الداخلية • المديرية العامة للجوازات",
      issueDate = "2023-10-15",
      expiryDate = "2028-10-14",
      status = "Active & Verified",
      isExpiringSoon = false,
      qrData = "IQAMA:2489310245|NAME:MUHAMMAD TARIQ KHAN|EXP:2028-10-14|STATUS:VALID",
      primaryColorHex = 0xFF0A5C36, // Saudi Green
      fields = mapOf(
        "Profession" to "Software Engineer",
        "Nationality" to "Pakistani",
        "Religion" to "Muslim",
        "Sponsor" to "2Do Tech Information Technology Est."
      )
    ),
    PersonalDocument(
      id = "doc_cr",
      titleEn = "Commercial Registration (CR)",
      titleAr = "السجل التجاري الإلكتروني",
      docType = DocumentType.COMMERCIAL_REGISTRATION,
      documentNumber = "1010948210",
      holderNameEn = "Al-Madinah Advanced Technologies Est.",
      holderNameAr = "مؤسسة المدينة للتقنيات المتقدمة",
      issuingAuthorityEn = "Ministry of Commerce • Saudi Business Center",
      issuingAuthorityAr = "وزارة التجارة • المركز السعودي للأعمال",
      issueDate = "2023-01-20",
      expiryDate = "2028-01-19",
      status = "Valid & Active",
      isExpiringSoon = false,
      qrData = "CR:1010948210|UNIFIED_700:7001948201|NAME:AL-MADINAH TECH|EXP:2028-01-19",
      primaryColorHex = 0xFF1565C0, // Professional Blue
      fields = mapOf(
        "Unified 700 Number" to "7001948201",
        "Legal Form" to "Sole Proprietorship",
        "Chamber Membership" to "Active (Riyadh Chamber)",
        "Main Activity" to "Cloud & Software Development Services"
      )
    ),
    PersonalDocument(
      id = "doc_misa",
      titleEn = "MISA Foreign Investment License",
      titleAr = "ترخيص الاستثمار الأجنبي الموحد - وزارة الاستثمار",
      docType = DocumentType.MISA_LICENSE,
      documentNumber = "102031094821",
      holderNameEn = "2Do Tech Global Technologies KSA LLC",
      holderNameAr = "شركة تو دو تك للتقنيات العالمية ذ.م.م",
      issuingAuthorityEn = "Ministry of Investment (MISA)",
      issuingAuthorityAr = "وزارة الاستثمار • منصة استثمر في السعودية",
      issueDate = "2023-03-15",
      expiryDate = "2028-03-14",
      status = "Active & In Good Standing",
      isExpiringSoon = false,
      qrData = "MISA:102031094821|COMPANY:2DO TECH GLOBAL|OWNERSHIP:100% FOREIGN|EXP:2028-03-14",
      primaryColorHex = 0xFF4A148C, // MISA Royal Purple
      fields = mapOf(
        "License Type" to "Service & IT Investment (100% Foreign Ownership)",
        "RHQ Status" to "Regional Headquarters Compliant",
        "Unified 700 Number" to "7001948201",
        "Authorized Capital" to "500,000 SAR"
      )
    ),
    PersonalDocument(
      id = "doc_passport",
      titleEn = "International Biometric Passport",
      titleAr = "جواز السفر الدولي الإلكتروني",
      docType = DocumentType.PASSPORT,
      documentNumber = "EP82910482",
      holderNameEn = "Muhammad Tariq Khan",
      holderNameAr = "محمد طارق خان",
      issuingAuthorityEn = "Passport & Immigration Office",
      issuingAuthorityAr = "مصلحة الجوازات والهجرة",
      issueDate = "2022-04-10",
      expiryDate = "2032-04-09",
      status = "Valid (8 Years Remaining)",
      isExpiringSoon = false,
      qrData = "P<PAKKHAN<<MUHAMMAD<TARIQ<<<<<<<<<<<<<<<<<<EP829104829PAK9001014M3204095<<<<<<<<<<<<<<02",
      primaryColorHex = 0xFF1B5E20, // Forest Green
      fields = mapOf(
        "Date of Birth" to "1990-01-01",
        "Place of Issue" to "Islamabad",
        "Type" to "Regular (P)",
        "Tracking Code" to "BIO-PK-2022-SA"
      )
    ),
    PersonalDocument(
      id = "doc_license",
      titleEn = "Saudi Driving License (رخصة قيادة)",
      titleAr = "رخصة قيادة سعودية خاصة",
      docType = DocumentType.DRIVING_LICENSE,
      documentNumber = "DL-2489310245",
      holderNameEn = "Muhammad Tariq Khan",
      holderNameAr = "محمد طارق خان",
      issuingAuthorityEn = "General Directorate of Traffic (Moroor)",
      issuingAuthorityAr = "الإدارة العامة للمرور",
      issueDate = "2021-08-12",
      expiryDate = "2031-08-11",
      status = "Valid / Active",
      isExpiringSoon = false,
      qrData = "DL:2489310245|CLASS:PRIVATE_LIGHT|TRAFFIC_POINTS:0|EXP:2031-08-11",
      primaryColorHex = 0xFF37474F, // Slate Navy
      fields = mapOf(
        "Vehicle Class" to "Private Light (خصوصي)",
        "Blood Type" to "O+",
        "Traffic Violation Points" to "0 Points (Clean)",
        "Status" to "Absher Verified"
      )
    ),
    PersonalDocument(
      id = "doc_address",
      titleEn = "National Address (العنوان الوطني)",
      titleAr = "إثبات العنوان الوطني الموحد - سبل",
      docType = DocumentType.NATIONAL_ADDRESS,
      documentNumber = "RRRD2941",
      holderNameEn = "Muhammad Tariq Khan",
      holderNameAr = "محمد طارق خان",
      issuingAuthorityEn = "Saudi Post (SPL) • Ministry of Municipalities",
      issuingAuthorityAr = "البريد السعودي (سبل) • وزارة الشؤون البلدية",
      issueDate = "2023-05-01",
      expiryDate = "2027-05-01",
      status = "Verified & Geocoded",
      isExpiringSoon = false,
      qrData = "SPL:RRRD2941|BLDG:7341|ST:KING FAHD RD|DIST:AL-OLAYA|CITY:RIYADH|ZIP:12211",
      primaryColorHex = 0xFFE65100, // SPL Amber Orange
      fields = mapOf(
        "Short Address" to "RRRD2941",
        "Building No" to "7341, King Fahd Branch Rd",
        "District & City" to "Al-Olaya, Riyadh 12211",
        "Additional No" to "3920"
      )
    ),
    PersonalDocument(
      id = "doc_gosi",
      titleEn = "GOSI Social Insurance Certificate",
      titleAr = "شهادة التأمينات الاجتماعية الموثقة",
      docType = DocumentType.GOSI_CERTIFICATE,
      documentNumber = "GOSI-59182374",
      holderNameEn = "Muhammad Tariq Khan",
      holderNameAr = "محمد طارق خان",
      issuingAuthorityEn = "General Organization for Social Insurance (GOSI)",
      issuingAuthorityAr = "المؤسسة العامة للتأمينات الاجتماعية",
      issueDate = "2024-01-01",
      expiryDate = "Continuous Active",
      status = "Active Contributor",
      isExpiringSoon = false,
      qrData = "GOSI:59182374|CONTRIBUTION_MONTHS:48|WPS:COMPLIANT|STATUS:ACTIVE",
      primaryColorHex = 0xFF4A148C, // Royal Purple
      fields = mapOf(
        "Contribution Months" to "48 Months",
        "Wage Protection (WPS)" to "Fully Compliant",
        "Current Employer" to "Al-Madinah Advanced Technologies Est.",
        "Annuity & Hazards" to "Active Coverage"
      )
    ),
    PersonalDocument(
      id = "doc_qiwa",
      titleEn = "Qiwa Authenticated Digital Contract",
      titleAr = "عقد العمل الرقمي الموثق - منصة قوى",
      docType = DocumentType.QIWA_CONTRACT,
      documentNumber = "QC-2024-91823",
      holderNameEn = "Muhammad Tariq Khan",
      holderNameAr = "محمد طارق خان",
      issuingAuthorityEn = "Ministry of Human Resources (Qiwa Platform)",
      issuingAuthorityAr = "وزارة الموارد البشرية والتنمية الاجتماعية (قوى)",
      issueDate = "2024-02-01",
      expiryDate = "2026-02-01",
      status = "Attested & Binding",
      isExpiringSoon = false,
      qrData = "QIWA:QC-2024-91823|WORKER:2489310245|ROLE:SOFTWARE ENGINEER|EXP:2026-02-01",
      primaryColorHex = 0xFF00695C, // Teal
      fields = mapOf(
        "Contract Duration" to "2 Years (Renewable)",
        "Job Title" to "Senior Software Engineer",
        "Probation Period" to "Completed (Passed)",
        "Notice Period" to "60 Days"
      )
    )
  )
}

data class GovPortal(
  val id: String,
  val nameEn: String,
  val nameAr: String,
  val authorityEn: String,
  val authorityAr: String,
  val category: String, // "Residency & Passports", "Tax & E-Invoicing", "Labor & HR", "Business & Investment", "Municipal"
  val descriptionEn: String,
  val descriptionAr: String,
  val portalUrl: String,
  val status: String, // "Online • 24/7", "Instant Processing", "Active"
  val servicesCount: Int,
  val popularServices: List<String>,
  val badgeColorHex: Long = 0xFF0A5C36,
  val isPopular: Boolean = true
)

object GovPortalsRepository {
  fun getDefaultPortals(): List<GovPortal> = listOf(
    GovPortal(
      id = "portal_absher",
      nameEn = "Absher Business & Individuals",
      nameAr = "منصة أبشر (أعمال وأفراد)",
      authorityEn = "Ministry of Interior (MoI)",
      authorityAr = "وزارة الداخلية",
      category = "Residency & Passports",
      descriptionEn = "Saudi Arabia's premier e-government portal for civil affairs, traffic, passport renewals, and company authorized signatures.",
      descriptionAr = "البوابة الوطنية الرائدة لخدمات الجوازات، الأحوال المدنية، المرور، والتفاويض الإلكترونية.",
      portalUrl = "https://www.absher.sa",
      status = "Live • 24/7",
      servicesCount = 280,
      popularServices = listOf("Driving License Renewal", "Absher Authorizations", "Civil Status Updates", "Traffic Violations"),
      badgeColorHex = 0xFF0A5C36
    ),
    GovPortal(
      id = "portal_muqeem",
      nameEn = "Muqeem Residency Gateway",
      nameAr = "بوابة مقيم الإلكترونية",
      authorityEn = "General Directorate of Passports (Jawazat)",
      authorityAr = "المديرية العامة للجوازات",
      category = "Residency & Passports",
      descriptionEn = "Comprehensive gateway for corporate expat management, instant exit/re-entry visas, Iqama renewals, and interactive reports.",
      descriptionAr = "البوابة المعتمدة لإدارة إقامات الموظفين، إصدار تأشيرات الخروج والعودة، والتقارير التفاعلية.",
      portalUrl = "https://muqeem.sa",
      status = "Official Portal",
      servicesCount = 45,
      popularServices = listOf("Iqama Instant Renewal", "Exit & Re-Entry Visa", "Transfer of Sponsorship", "Profession Amendment"),
      badgeColorHex = 0xFF1B824E
    ),
    GovPortal(
      id = "portal_zatca",
      nameEn = "ZATCA (Zakat, Tax & Customs)",
      nameAr = "هيئة الزكاة والضريبة والجمارك (زاتكا)",
      authorityEn = "Zakat, Tax and Customs Authority",
      authorityAr = "هيئة الزكاة والضريبة والجمارك",
      category = "Tax & E-Invoicing",
      descriptionEn = "Fatoora Phase 2 E-Invoicing onboarding, VAT quarterly tax filings, corporate income tax, and customs clearance.",
      descriptionAr = "الربط مع منظومة الفاتورة الإلكترونية (فاتورة 2)، الإقرارات الضريبية، وضريبة القيمة المضافة.",
      portalUrl = "https://zatca.gov.sa",
      status = "Phase 2 Active",
      servicesCount = 60,
      popularServices = listOf("Fatoora Integration", "VAT Return Filing", "Withholding Tax", "Customs Declarations"),
      badgeColorHex = 0xFF1565C0
    ),
    GovPortal(
      id = "portal_qiwa",
      nameEn = "Qiwa Labor Platform",
      nameAr = "منصة قوى للعمل والمنشآت",
      authorityEn = "Ministry of Human Resources and Social Development",
      authorityAr = "وزارة الموارد البشرية والتنمية الاجتماعية",
      category = "Labor & HR",
      descriptionEn = "Unified business labor ecosystem for work permits, digitized employment contracts, Saudization tracking (Nitaqat), and transfer services.",
      descriptionAr = "المنظومة الموحدة لإصدار رخص العمل، توثيق عقود الموظفين، متابعة نطاقات، ونقل الخدمات.",
      portalUrl = "https://qiwa.sa",
      status = "Instant Sync",
      servicesCount = 85,
      popularServices = listOf("Work Permit Issuance", "Digital Contract Attestation", "Nitaqat Calculator", "Employee Transfer"),
      badgeColorHex = 0xFF00695C
    ),
    GovPortal(
      id = "portal_misa",
      nameEn = "MISA Foreign Investment Portal",
      nameAr = "وزارة الاستثمار (منصة استثمر في السعودية)",
      authorityEn = "Ministry of Investment (MISA)",
      authorityAr = "وزارة الاستثمار",
      category = "Business & Investment",
      descriptionEn = "100% foreign ownership investment licenses, Regional Headquarters (RHQ) program, startup licenses, and expedited enterprise setup in Saudi Arabia.",
      descriptionAr = "إصدار تراخيص الاستثمار الأجنبي، برنامج المقرات الإقليمية، رخص ريادة الأعمال، وتسهيلات الأعمال والمستثمرين.",
      portalUrl = "https://misa.gov.sa",
      status = "Fast-Track",
      servicesCount = 45,
      popularServices = listOf("100% Foreign Investor License", "Regional HQ (RHQ) Program", "Entrepreneurship License", "MISA License Renewal", "Executive Investor Visas", "Industrial License"),
      badgeColorHex = 0xFF4A148C
    ),
    GovPortal(
      id = "portal_gosi",
      nameEn = "GOSI Social Insurance Portal",
      nameAr = "التأمينات الاجتماعية (منصة تأميناتي)",
      authorityEn = "General Organization for Social Insurance",
      authorityAr = "المؤسسة العامة للتأمينات الاجتماعية",
      category = "Labor & HR",
      descriptionEn = "Enterprise social insurance management, wage updates, Wage Protection System compliance, and occupational hazards coverage.",
      descriptionAr = "إدارة اشتراكات التأمينات للمنشآت، تسجيل الأجور، حماية الأجور، وتغطية المخاطر المهنية.",
      portalUrl = "https://gosi.gov.sa",
      status = "Operational",
      servicesCount = 50,
      popularServices = listOf("Employee Wage Registration", "GOSI Compliance Certificate", "WPS Verification", "Annuity Claims"),
      badgeColorHex = 0xFF2E7D32
    ),
    GovPortal(
      id = "portal_balady",
      nameEn = "Balady Municipal Services",
      nameAr = "منصة بلدي للرخص والخدمات البلدية",
      authorityEn = "Ministry of Municipal and Rural Affairs (MOMRAH)",
      authorityAr = "وزارة الشؤون البلدية والقروية والإسكان",
      category = "Municipal",
      descriptionEn = "Commercial facility licensing, municipal permits, advertising signs approval, and civil engineering compliance certificates.",
      descriptionAr = "إصدار وتجديد الرخص التجارية للمحلات والمكاتب، تصاريح اللوحات الإعلانية، والامتثال الإنشائي.",
      portalUrl = "https://balady.gov.sa",
      status = "Online",
      servicesCount = 70,
      popularServices = listOf("Commercial Store License", "Signboard Permits", "Health Certificates", "Building Compliance"),
      badgeColorHex = 0xFFEF6C00
    ),
    GovPortal(
      id = "portal_sbc",
      nameEn = "Saudi Business Center (SBC) & MC",
      nameAr = "المركز السعودي للأعمال ووزارة التجارة",
      authorityEn = "Ministry of Commerce & Inter-Agency Council",
      authorityAr = "وزارة التجارة والمركز السعودي للأعمال",
      category = "Business & Investment",
      descriptionEn = "Unified gateway for starting and operating business in KSA: Instant CR issuance, 1-5 year CR renewals, Articles of Association (AoA), trade name reservation, and branch registrations.",
      descriptionAr = "الوجهة الموحدة لبدء وممارسة الأعمال: إصدار وتجديد السجلات التجارية فورياً، توثيق عقود التأسيس، وحجز الأسماء التجارية، وإصدار السجلات الفرعية.",
      portalUrl = "https://sbc.gov.sa",
      status = "Unified Gateway",
      servicesCount = 125,
      popularServices = listOf("Instant CR Issuance", "CR Renewal (1-5 Years)", "Articles of Association (AoA)", "Trade Name Reservation", "Sub-CR Branch Issuance", "CR Ownership Transfer"),
      badgeColorHex = 0xFF0277BD
    ),
    GovPortal(
      id = "portal_chamber",
      nameEn = "Chambers of Commerce (Riyadh / Asharqia)",
      nameAr = "الغرف التجارية السعودية (غرفة الرياض والشرقية)",
      authorityEn = "Federation of Saudi Chambers",
      authorityAr = "اتحاد الغرف السعودية",
      category = "Business & Investment",
      descriptionEn = "Electronic document ratification, certificate of origin issuance, commercial arbitration, and business delegations.",
      descriptionAr = "التصديق الإلكتروني للوثائق والخطابات، إصدار شهادات المنشأ، والتحكيم التجاري.",
      portalUrl = "https://www.chamber.sa",
      status = "Active",
      servicesCount = 35,
      popularServices = listOf("Electronic Document Ratification", "Chamber Membership Renewal", "Certificates of Origin", "Tenders Support"),
      badgeColorHex = 0xFF455A64
    )
  )
}
