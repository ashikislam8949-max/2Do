package com.example.data

import kotlinx.coroutines.flow.Flow

class ServiceRepository(private val db: AppDatabase) {
  val allServices: Flow<List<ServiceEntity>> = db.serviceDao().getAllServices()
  val featuredServices: Flow<List<ServiceEntity>> = db.serviceDao().getFeaturedServices()
  val popularGovServices: Flow<List<ServiceEntity>> = db.serviceDao().getPopularGovServices()
  val serviceRequests: Flow<List<ServiceRequestEntity>> = db.serviceRequestDao().getRequests()
  val savedServices: Flow<List<SavedServiceEntity>> = db.savedServiceDao().getSavedServices()

  suspend fun initializeDefaultServices() {
    val initialServices =
      listOf(
        ServiceEntity(
          id = "s1",
          title = "Commercial Registration (CR) Renewal & Amendment",
          category = "Government Services",
          fee = 500.00,
          governmentFee = 1200.00,
          duration = "24-48 Hours",
          imageUrl = "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 342,
          description = "Official Ministry of Commerce (MC) Commercial Registration renewal, activity additions, and branch management via Saudi Business Center (SBC) integrated with Lahint GovTech infrastructure.",
          requirements = "1. Active National Unified Number (700xxx)\n2. Chamber of Commerce Membership\n3. ZATCA Tax Certificate",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "s2",
          title = "ZATCA E-Invoicing Phase 2 Integration & ERP Setup",
          category = "IT & Cloud",
          fee = 3500.00,
          governmentFee = 0.00,
          duration = "3-5 Business Days",
          imageUrl = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 180,
          description = "Seamless integration of your POS/ERP systems with ZATCA Fatoora portal for Phase 2 XML invoices, UUID generation, and cryptographic stamps.",
          requirements = "1. ZATCA Portal Portal Access (Fatoora Onboarding)\n2. Company ERP API Specifications\n3. Commercial Registration Copy",
          isFeatured = true,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "s3",
          title = "Muqeem & Qiwa Work Permit & Iqama Renewal",
          category = "Government Services",
          fee = 300.00,
          governmentFee = 650.00,
          duration = "Same Day",
          imageUrl = "https://images.unsplash.com/photo-1521737604893-d14cc237f11d?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 520,
          description = "Automated processing for employee Iqama renewals, profession updates, work permit issuance, and Qiwa contract attestations.",
          requirements = "1. Employee Passport Copy & Valid Iqama\n2. Medical Insurance Certificate (Chi)\n3. Qiwa Establishment Account",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "s4",
          title = "NCA Cybersecurity Compliance & Audit (ECC-1:2018)",
          category = "Cybersecurity",
          fee = 7500.00,
          governmentFee = 0.00,
          duration = "2 Weeks",
          imageUrl = "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80",
          rating = 5.0,
          reviewCount = 64,
          description = "Comprehensive cybersecurity vulnerability assessment, penetration testing, and National Cybersecurity Authority (NCA) compliance audit for KSA enterprises.",
          requirements = "1. Network Architecture Diagram\n2. IT Asset Inventory\n3. Previous Audit Reports (if any)",
          isFeatured = true,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "s5",
          title = "Balady Municipal Commercial License Issuance",
          category = "Licensing & CR",
          fee = 800.00,
          governmentFee = 1500.00,
          duration = "3-4 Days",
          imageUrl = "https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=600&q=80",
          rating = 4.7,
          reviewCount = 145,
          description = "Ministry of Municipal and Rural Affairs (Balady) commercial shop and office licensing, engineering drawings approval, and sign permit.",
          requirements = "1. Lease Agreement (Ejar)\n2. Commercial Registration Copy\n3. Civil Defense Approval Certificate",
          isFeatured = false,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "s6",
          title = "Enterprise Cloud Migration & AWS/Azure KSA Region Setup",
          category = "IT & Cloud",
          fee = 5000.00,
          governmentFee = 0.00,
          duration = "1 Week",
          imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 92,
          description = "Migrate legacy databases and applications to AWS Middle East (Bahrain/Riyadh) or Microsoft Cloud KSA data centers adhering to CITC data residency laws.",
          requirements = "1. Current IT Infrastructure Overview\n2. Database Schema & Server Specs\n3. Security Policies",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "s7",
          title = "Lahint AI-Powered GOSI Social Insurance Automation",
          category = "Government Services",
          fee = 400.00,
          governmentFee = 0.00,
          duration = "Instant / Real-time",
          imageUrl = "https://images.unsplash.com/photo-1554224155-8d04cb21cd6c?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 215,
          description = "Integrated Lahint GovTech automated social insurance processing for employee registrations, wage updates, and GOSI certificate issuance.",
          requirements = "1. Establishment GOSI Number\n2. Employee National ID / Iqama\n3. Wage Details",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "s8",
          title = "Mudad Payroll & Wage Protection System (WPS) Integration",
          category = "IT & Cloud",
          fee = 1200.00,
          governmentFee = 300.00,
          duration = "24 Hours",
          imageUrl = "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 134,
          description = "Automate your monthly salary transfers through Mudad WPS compliance engine with instant bank reconciliation and Ministry of Human Resources reporting.",
          requirements = "1. Bank Corporate Account IBAN\n2. Monthly Payroll Sheet\n3. Qiwa Establishment Registration",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "s9",
          title = "MISA Foreign Investor License & Investment Certificate",
          category = "Government Services",
          fee = 2500.00,
          governmentFee = 10000.00,
          duration = "5-7 Business Days",
          imageUrl = "https://images.unsplash.com/photo-1507679799987-c73779587ccf?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 110,
          description = "Ministry of Investment (MISA) investor license processing, professional and trading license issuance, and regional headquarters advisory.",
          requirements = "1. Audited Financial Statements (Last 2 Years)\n2. Parent Company Commercial Registration\n3. Passport Copies of Shareholders",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "s10",
          title = "Saudi Trademark & Intellectual Property Registration (SAIP)",
          category = "Licensing & CR",
          fee = 1500.00,
          governmentFee = 5200.00,
          duration = "10-14 Days",
          imageUrl = "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 85,
          description = "Protect your brand identity and logos across the Kingdom through Saudi Authority for Intellectual Property (SAIP) official filings.",
          requirements = "1. Brand Logo Vector File (.AI/.EPS)\n2. Commercial Registration Copy\n3. Power of Attorney",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "s11",
          title = "CST (CITC) Telecom & Technology Service Provider Licensing",
          category = "Licensing & CR",
          fee = 4500.00,
          governmentFee = 8000.00,
          duration = "2-3 Weeks",
          imageUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&w=600&q=80",
          rating = 4.7,
          reviewCount = 42,
          description = "Communications, Space and Technology Commission (CST) class and individual licensing for cloud providers, ISPs, and digital platforms.",
          requirements = "1. Technical Architecture & Security Plan\n2. Commercial Registration Copy\n3. ISO Certifications",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "s12",
          title = "ZATCA VAT Return Filing & Tax Compliance Audit",
          category = "Government Services",
          fee = 1000.00,
          governmentFee = 0.00,
          duration = "24 Hours",
          imageUrl = "https://images.unsplash.com/photo-1554224154-26032ffc0d07?auto=format&fit=crop&w=600&q=80",
          rating = 5.0,
          reviewCount = 310,
          description = "Quarterly/Monthly ZATCA VAT return filing, sales/purchase ledger reconciliation, and certified tax audit reporting.",
          requirements = "1. ZATCA Portal Login Credentials\n2. Sales & Purchase Invoices\n3. Bank Statements",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "s13",
          title = "Muqeem Exit & Re-Entry Visa Issuance",
          category = "Iqama & Residency",
          fee = 100.00,
          governmentFee = 200.00,
          duration = "Instant / Real-time",
          imageUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 430,
          description = "Instant single or multiple exit/re-entry visa issuance for employees and dependents via integrated Muqeem gateway.",
          requirements = "1. Valid Iqama Number\n2. Passport Validity (6+ Months)\n3. No Outstanding Traffic Violations",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "s14",
          title = "Iqama Profession & Job Title Modification",
          category = "Iqama & Residency",
          fee = 250.00,
          governmentFee = 1000.00,
          duration = "24-48 Hours",
          imageUrl = "https://images.unsplash.com/photo-1521737604893-d14cc237f11d?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 195,
          description = "Official Ministry of Interior (MoI) and Qiwa profession update service matching educational degree attestations with company commercial activities.",
          requirements = "1. Attested University Degree\n2. Valid Iqama & Passport Copy\n3. Qiwa Contract Update",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "s15",
          title = "Dependent Family Iqama Sponsorship & Renewal",
          category = "Iqama & Residency",
          fee = 350.00,
          governmentFee = 500.00,
          duration = "2-3 Days",
          imageUrl = "https://images.unsplash.com/photo-1511895426328-dc8714191300?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 280,
          description = "Family residence visa sponsorship, medical insurance verification, and annual dependent Iqama renewals through Absher Business & Muqeem.",
          requirements = "1. Dependent Passports & Medical Check\n2. Valid Sponsor Iqama\n3. Health Insurance Policy",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "s16",
          title = "Final Exit Visa Issuance & Settlement (Muqeem)",
          category = "Iqama & Residency",
          fee = 150.00,
          governmentFee = 0.00,
          duration = "Instant",
          imageUrl = "https://images.unsplash.com/photo-1507679799987-c73779587ccf?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 160,
          description = "Official final exit visa processing for departing employees within 60-day legal window, complete with financial clearance checks.",
          requirements = "1. Valid Iqama & Passport\n2. End of Service Clearance\n3. No Traffic Fines",
          isFeatured = false,
          isPopularGov = true
        ),
        // ==================== SAUDI MISA SERVICES (MINISTRY OF INVESTMENT) ====================
        ServiceEntity(
          id = "misa_rhq",
          title = "MISA Regional Headquarters (RHQ) License & Tax Relief",
          category = "MISA & Investment",
          fee = 4500.00,
          governmentFee = 0.00,
          duration = "5-7 Business Days",
          imageUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=600&q=80",
          rating = 5.0,
          reviewCount = 94,
          description = "Ministry of Investment (MISA) Regional Headquarters (RHQ) License for multinational corporations establishing MENA headquarters in Riyadh. Entitles enterprise to 30-year 0% corporate income tax and withholding tax exemptions, unlimited work visas, and priority Saudi government megaproject procurement.",
          requirements = "1. Proof of operation in at least 2 countries outside Saudi Arabia\n2. Audited Global Financial Statements\n3. RHQ Operating Model & Strategic Business Plan\n4. Board of Directors Resolution establishing Riyadh Regional HQ",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "misa_renewal",
          title = "MISA Annual Investment License Renewal & Compliance",
          category = "MISA & Investment",
          fee = 1200.00,
          governmentFee = 60000.00,
          duration = "24-48 Hours",
          imageUrl = "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 156,
          description = "Expedited annual renewal of MISA foreign investor license through the Ministry of Investment portal, including compliance validation, Saudization ratio audit (Nitaqat), and ZATCA tax clearance certification.",
          requirements = "1. Valid Commercial Registration (CR)\n2. ZATCA Tax & Zakat Compliance Certificate\n3. GOSI & Saudization Certificate (Qiwa)\n4. Annual Audited Financial Balance Sheet",
          isFeatured = true,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "misa_amendment",
          title = "MISA License Amendment & Economic Activity Addition",
          category = "MISA & Investment",
          fee = 1500.00,
          governmentFee = 2000.00,
          duration = "2-3 Business Days",
          imageUrl = "https://images.unsplash.com/photo-1507679799987-c73779587ccf?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 88,
          description = "Modifying registered commercial activities on MISA investment license, adding new ISIC4 classifications (commercial, software, consulting, contracting), updating capital, and expanding branches across the Kingdom.",
          requirements = "1. Active MISA Foreign Investment License\n2. Shareholder / Board Resolution approving activity additions\n3. Draft Amended Articles of Association (AoA)\n4. Relevant Technical Accreditations if applicable",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "misa_startup",
          title = "MISA Entrepreneurship & Innovative Startup License",
          category = "MISA & Investment",
          fee = 1800.00,
          governmentFee = 2000.00,
          duration = "3-5 Business Days",
          imageUrl = "https://images.unsplash.com/photo-1559136555-9303baea8ebd?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 112,
          description = "Fast-track MISA investment license for innovative technology founders and startups endorsed by authorized venture capital funds (SVC) or certified Saudi incubators. Exempt from prior audited financials requirement with zero minimum capital.",
          requirements = "1. Endorsement letter from authorized Saudi incubator or VC fund\n2. Startup Pitch Deck & Product Architecture Overview\n3. Passports of Founding Team\n4. Proof of Prototype or Intellectual Property",
          isFeatured = true,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "misa_branch",
          title = "MISA Branch Office of Foreign Company Licensing",
          category = "MISA & Investment",
          fee = 2800.00,
          governmentFee = 10000.00,
          duration = "4-6 Business Days",
          imageUrl = "https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 76,
          description = "Establishment and licensing of a direct legal branch of an international company in Saudi Arabia without requiring local Saudi equity, allowing direct bidding on sovereign and private contracts.",
          requirements = "1. Parent Company Certificate of Incorporation & Bylaws apostilled by Saudi Embassy\n2. Power of Attorney for Branch General Manager\n3. Audited Financial Statements for last 2 fiscal years\n4. Board Resolution to open Saudi branch",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "misa_industrial",
          title = "MISA Industrial & Manufacturing Investment License",
          category = "MISA & Investment",
          fee = 3500.00,
          governmentFee = 10000.00,
          duration = "5-8 Business Days",
          imageUrl = "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 62,
          description = "Specialized MISA license for industrial manufacturing, advanced tech factories, and assembly plants. Includes MODON industrial land allocation facilitation, customs duty exemption on machinery, and SIDF financing eligibility.",
          requirements = "1. Industrial Feasibility Study & Machinery Specifications\n2. Environmental Impact Evaluation (NCEC)\n3. Parent Company Financials & Experience Portfolio\n4. Proposed Plant Location and Power Requirements",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "misa_visa",
          title = "MISA Executive Investor Visa & Premium Residency Fast-Track",
          category = "MISA & Investment",
          fee = 800.00,
          governmentFee = 3000.00,
          duration = "24-48 Hours",
          imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 204,
          description = "Official Ministry of Investment (MISA) investor visa endorsements, executive business visit visas, and accelerated recommendation files for Saudi Premium Residency (Special Talent & Investor products).",
          requirements = "1. Active MISA Foreign Investment License\n2. Executive / Shareholder Passport Copy (valid 6+ months)\n3. Company Authorization & Chamber of Commerce Attestation\n4. Shareholder or GM Resolution",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "misa_capital",
          title = "MISA Capital Restructuring & Shareholder Equity Transfer",
          category = "MISA & Investment",
          fee = 2200.00,
          governmentFee = 2000.00,
          duration = "3-5 Business Days",
          imageUrl = "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=600&q=80",
          rating = 4.7,
          reviewCount = 58,
          description = "Modifying registered enterprise capital on MISA license, transferring equity shares between foreign and domestic shareholders, onboarding global institutional investors, and notarizing updated capital structures.",
          requirements = "1. Extraordinary General Assembly Resolution\n2. Certified Auditor Report on Capital\n3. Bank Capital Transfer / Deposit Certificate\n4. IDs and Passports of new shareholders",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "misa_realestate",
          title = "MISA Real Estate Investment License (REGA Endorsed)",
          category = "MISA & Investment",
          fee = 4500.00,
          governmentFee = 10000.00,
          duration = "1-2 Weeks",
          imageUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 43,
          description = "MISA real estate investment license for foreign corporate entities undertaking property development, residential communities, and commercial real estate projects exceeding 30 Million SAR inside the Kingdom.",
          requirements = "1. Real Estate General Authority (REGA) Clearance\n2. Minimum Project Capital Proof (30M+ SAR)\n3. Land Title Deed (Suk) or Master Development Agreement\n4. Corporate Balance Sheets",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "misa_cancellation",
          title = "MISA Investment License Cancellation & Orderly Liquidation",
          category = "MISA & Investment",
          fee = 3000.00,
          governmentFee = 0.00,
          duration = "2-3 Weeks",
          imageUrl = "https://images.unsplash.com/photo-1450133064473-71024230f91b?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 37,
          description = "Official deregistration and cancellation of MISA investment license, managing Umm Al-Qura gazette announcements, liquidator appointment, final tax discharge with ZATCA, and complete exit compliance.",
          requirements = "1. Shareholder Resolution for Liquidation & Liquidator Appointment\n2. Final ZATCA Tax & Zakat Discharge Certificate\n3. GOSI & Qiwa Labor File Closure Letters\n4. Final Audited Liquidation Balance Sheet",
          isFeatured = false,
          isPopularGov = false
        ),
        // ==================== SAUDI CR SERVICES (COMMERCIAL REGISTRATION) ====================
        ServiceEntity(
          id = "cr_issuance",
          title = "New Commercial Registration (CR) Instant Issuance",
          category = "Commercial Registration (CR)",
          fee = 600.00,
          governmentFee = 1200.00,
          duration = "Same Day (2-4 Hours)",
          imageUrl = "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 480,
          description = "Instant electronic issuance of Commercial Registration (CR) for Individual Establishments or Limited Liability Companies (LLC) via Saudi Business Center (SBC) and Ministry of Commerce, automatically issuing the Unified 700 Number and Chamber membership.",
          requirements = "1. Active Absher Account / National ID / Premium Residency / MISA License\n2. Reserved Commercial Trade Name\n3. National Address Proof (SPL)\n4. Selected ISIC Economic Activities",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "cr_renewal",
          title = "Commercial Registration (CR) Multi-Year Renewal",
          category = "Commercial Registration (CR)",
          fee = 400.00,
          governmentFee = 1000.00,
          duration = "Instant / Real-time",
          imageUrl = "https://images.unsplash.com/photo-1450133064473-71024230f91b?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 610,
          description = "1 to 5-year online renewal of primary or secondary Commercial Registration with Ministry of Commerce and Saudi Business Center, with instant Chamber of Commerce fee settlement and digital certificate generation.",
          requirements = "1. Commercial Registration Number (10 digits)\n2. National Unified Number (700xxx)\n3. Payment of Chamber & Ministry Fees via SADAD",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "cr_amendment",
          title = "CR Amendment & ISIC Activity Modification",
          category = "Commercial Registration (CR)",
          fee = 500.00,
          governmentFee = 500.00,
          duration = "24 Hours",
          imageUrl = "https://images.unsplash.com/photo-1507679799987-c73779587ccf?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 295,
          description = "Amending and expanding economic activities on Commercial Registration in full alignment with ISIC4 classification standards, changing head office location, updating general manager, and capital adjustment.",
          requirements = "1. Active Commercial Registration\n2. Authorized Manager Absher Verification\n3. Regulatory approvals for specialized sectors (SFDA, CST, MoI if required)",
          isFeatured = true,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "cr_tradename",
          title = "Trade Name Reservation & Electronic Notarization",
          category = "Commercial Registration (CR)",
          fee = 300.00,
          governmentFee = 0.00,
          duration = "Instant - 24 Hours",
          imageUrl = "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 340,
          description = "Searching, reserving, and securing unique commercial trade names in Arabic or dual Arabic-English through Ministry of Commerce official registry, verifying trademark conflict absence and compliance with Saudi corporate naming rules.",
          requirements = "1. SBC Business Account Access\n2. Proposed Commercial Trade Name in Arabic and English\n3. Intended commercial business activity category",
          isFeatured = false,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "cr_branch",
          title = "Sub-CR Branch Issuance Across Saudi Provinces",
          category = "Commercial Registration (CR)",
          fee = 450.00,
          governmentFee = 600.00,
          duration = "Same Day",
          imageUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 210,
          description = "Issuing secondary/sub-branch Commercial Registration certificates for opening new branches, logistics warehouses, or corporate branch offices across Riyadh, Jeddah, Eastern Province, Neom, and other provinces under the primary establishment.",
          requirements = "1. Valid Primary Main CR\n2. National Address or Ejar Commercial Lease Contract for new branch\n3. Branch Manager Identification",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "cr_aoa",
          title = "Articles of Association (AoA) Electronic Notarization",
          category = "Commercial Registration (CR)",
          fee = 1500.00,
          governmentFee = 500.00,
          duration = "24-48 Hours",
          imageUrl = "https://images.unsplash.com/photo-1450133064473-71024230f91b?auto=format&fit=crop&w=600&q=80",
          rating = 5.0,
          reviewCount = 185,
          description = "Drafting, legal revision, and Ministry of Justice electronic notary attestation of LLC Articles of Association (عقد التأسيس), partner amendments, and governance charters under the New Saudi Companies Law.",
          requirements = "1. National IDs / Iqamas / Foreign Passports of Shareholders\n2. Shareholder Equity and Profit Allocation Breakdown\n3. General Manager Authority and Management Powers Schedule",
          isFeatured = true,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "cr_transfer",
          title = "CR Ownership Transfer & Sale of Establishment",
          category = "Commercial Registration (CR)",
          fee = 1200.00,
          governmentFee = 1000.00,
          duration = "2-3 Business Days",
          imageUrl = "https://images.unsplash.com/photo-1521737604893-d14cc237f11d?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 140,
          description = "Complete legal transfer of Commercial Registration ownership or company equity from current owner to a new buyer, with electronic deed attestation, Qiwa labor file transition, and ZATCA tax clearance.",
          requirements = "1. Mutual Electronic Consent of Buyer and Seller via Absher\n2. ZATCA Tax & Zakat Clearance Certificate\n3. GOSI Zero Debt Certificate\n4. Qiwa Employee Transfer Acceptance",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "cr_conversion",
          title = "Conversion of Establishment to LLC (Corporate Restructuring)",
          category = "Commercial Registration (CR)",
          fee = 2500.00,
          governmentFee = 1500.00,
          duration = "3-5 Business Days",
          imageUrl = "https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 98,
          description = "Corporate transformation converting an individual sole proprietorship into a Limited Liability Company (LLC) under the New Saudi Companies Law, shielding personal assets and enabling institutional equity investment.",
          requirements = "1. Certified Financial Balance Sheet\n2. Establishment Asset Evaluation Statement\n3. Draft Articles of Association (AoA)\n4. Partner Identifications and Capital Allocation",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "cr_extract",
          title = "Certified CR Extract & Official Digital Statement",
          category = "Commercial Registration (CR)",
          fee = 150.00,
          governmentFee = 100.00,
          duration = "Instant Download",
          imageUrl = "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 520,
          description = "Official certified electronic extract of Commercial Registration featuring the Ministry of Commerce QR validation seal and digital stamp, required for corporate bank accounts, Etimad tenders, and official bids.",
          requirements = "1. Active Commercial Registration Number\n2. National Unified Number (700xxx)",
          isFeatured = false,
          isPopularGov = true
        ),
        ServiceEntity(
          id = "cr_cancellation",
          title = "CR Cancellation & Final Deregistration (شطب السجل)",
          category = "Commercial Registration (CR)",
          fee = 600.00,
          governmentFee = 0.00,
          duration = "2-3 Business Days",
          imageUrl = "https://images.unsplash.com/photo-1450133064473-71024230f91b?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 115,
          description = "Official electronic cancellation and deregistration of primary or branch Commercial Registration (شطب السجل) via SBC, with automated clearance validation from Qiwa, GOSI, Balady, and ZATCA.",
          requirements = "1. Zero Active Workers on Establishment File (Qiwa)\n2. ZATCA Final Tax Clearance Certificate\n3. Balady Municipal Commercial License Cancellation\n4. Chamber of Commerce Membership Clearance",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "cr_chamber",
          title = "Unified Chamber of Commerce Membership & Attestation",
          category = "Commercial Registration (CR)",
          fee = 350.00,
          governmentFee = 2000.00,
          duration = "Same Day",
          imageUrl = "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?auto=format&fit=crop&w=600&q=80",
          rating = 4.8,
          reviewCount = 390,
          description = "Annual Chamber of Commerce membership registration (Riyadh, Jeddah, Asharqia Chambers) and electronic document ratification service for commercial agreements, employee certificates, and foreign trade documents.",
          requirements = "1. Valid Commercial Registration\n2. Authorized Signatory Absher Verification\n3. Official Corporate Seal Impression",
          isFeatured = false,
          isPopularGov = false
        ),
        ServiceEntity(
          id = "cr_700",
          title = "Unified National Number (700) Issuance & SBC Activation",
          category = "Commercial Registration (CR)",
          fee = 300.00,
          governmentFee = 0.00,
          duration = "24 Hours",
          imageUrl = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=600&q=80",
          rating = 4.9,
          reviewCount = 275,
          description = "Issuing, verifying, and activating the 10-digit National Unified Number (الرقم الموحد 700) linking corporate profiles across ZATCA, Muqeem, Qiwa, GOSI, Balady, and corporate banking platforms.",
          requirements = "1. Approved Commercial Registration or MISA Foreign Investment License\n2. Authorized General Manager National ID / Iqama\n3. National Address Proof",
          isFeatured = false,
          isPopularGov = false
        )
      )
    db.serviceDao().insertAll(initialServices)

    val initialRequests = listOf(
      ServiceRequestEntity(
        requestId = "MISA-RHQ-2026-904",
        date = "08 Oct 2026, 14:30",
        serviceTitle = "MISA Regional Headquarters (RHQ) License & Tax Relief",
        companyName = "StarBridge Global Technologies KSA",
        crNumber = "1010948210",
        totalAmount = 4500.00,
        status = "Processing",
        paymentMethod = "Corporate SADAD Invoice"
      ),
      ServiceRequestEntity(
        requestId = "CR-SBC-2026-812",
        date = "06 Oct 2026, 11:15",
        serviceTitle = "Commercial Registration (CR) Multi-Year Renewal",
        companyName = "StarBridge Global Technologies KSA",
        crNumber = "1010948210",
        totalAmount = 1400.00,
        status = "Completed",
        paymentMethod = "Mada Enterprise Card"
      ),
      ServiceRequestEntity(
        requestId = "ZATCA-EINV-2026-745",
        date = "03 Oct 2026, 09:40",
        serviceTitle = "ZATCA E-Invoicing Phase 2 Integration & ERP Setup",
        companyName = "StarBridge Global Technologies KSA",
        crNumber = "1010948210",
        totalAmount = 3500.00,
        status = "Completed",
        paymentMethod = "Government E-Wallet"
      ),
      ServiceRequestEntity(
        requestId = "QIWA-PERMIT-2026-621",
        date = "01 Oct 2026, 16:20",
        serviceTitle = "Muqeem & Qiwa Work Permit & Iqama Renewal",
        companyName = "StarBridge Global Technologies KSA",
        crNumber = "1010948210",
        totalAmount = 950.00,
        status = "Under Review",
        paymentMethod = "Corporate SADAD Invoice"
      )
    )
    db.serviceRequestDao().insertAll(initialRequests)
  }

  suspend fun submitRequest(requestId: String, date: String, serviceTitle: String, companyName: String, crNumber: String, totalAmount: Double, paymentMethod: String) {
    db.serviceRequestDao().insertRequest(ServiceRequestEntity(requestId, date, serviceTitle, companyName, crNumber, totalAmount, "Under Review", paymentMethod))
  }

  suspend fun toggleSavedService(serviceId: String, title: String, category: String, fee: Double, imageUrl: String, rating: Double, isCurrentlySaved: Boolean) {
    if (isCurrentlySaved) {
      db.savedServiceDao().removeSavedService(serviceId)
    } else {
      db.savedServiceDao().insertSavedService(SavedServiceEntity(serviceId, title, category, fee, imageUrl, rating))
    }
  }

  fun isSaved(serviceId: String): Flow<Boolean> {
    return db.savedServiceDao().isSaved(serviceId)
  }

  suspend fun registerUser(user: UserEntity) {
    db.userDao().insertUser(user)
  }

  suspend fun loginUser(email: String, pass: String): UserEntity? {
    val user = db.userDao().getUserByEmail(email)
    return if (user != null && user.password == pass) user else null
  }

  suspend fun updateUserProfile(user: UserEntity) {
    db.userDao().updateUser(user)
  }

  suspend fun getUserById(userId: String): UserEntity? {
    return db.userDao().getUserById(userId)
  }
}
