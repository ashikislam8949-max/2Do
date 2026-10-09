package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf

enum class MisaStageStatus {
  COMPLETED,
  IN_PROGRESS,
  PENDING
}

data class MisaApplicationStage(
  val stageIndex: Int,
  val nameEn: String,
  val nameAr: String,
  val shortCode: String,
  val status: MisaStageStatus,
  val completionScore: Float, // 0 to 100
  val estimatedDuration: String,
  val ministrySection: String,
  val actionRequired: String,
  val officialNotes: String
)

data class MisaApplicationTrack(
  val applicationId: String,
  val titleEn: String,
  val titleAr: String,
  val companyName: String,
  val crNumber: String,
  val totalProgress: Int, // e.g. 85
  val filingDate: String,
  val estimatedApprovalDate: String,
  val licenseType: String,
  val stages: List<MisaApplicationStage>
)

object MisaApplicationsRepository {
  fun getDefaultTracks(): List<MisaApplicationTrack> = listOf(
    MisaApplicationTrack(
      applicationId = "MISA-RHQ-2026-904",
      titleEn = "Regional Headquarters (RHQ) License",
      titleAr = "ترخيص المقرات الإقليمية (RHQ) مع الإعفاء الضريبي",
      companyName = "StarBridge Global Technologies KSA",
      crNumber = "1010948210",
      totalProgress = 85,
      filingDate = "02 Oct 2026",
      estimatedApprovalDate = "12 Oct 2026",
      licenseType = "Regional Headquarters (RHQ) • 30-Yr Tax Exemption",
      stages = listOf(
        MisaApplicationStage(
          stageIndex = 0,
          nameEn = "Filing & Apostille",
          nameAr = "تقديم الوثائق والمصادقة",
          shortCode = "S1: Filing",
          status = MisaStageStatus.COMPLETED,
          completionScore = 100f,
          estimatedDuration = "24 Hours",
          ministrySection = "MISA Document Verification Dept",
          actionRequired = "Completed - Parent Bylaws & Apostille Verified",
          officialNotes = "All 4 international entity incorporation deeds verified and approved."
        ),
        MisaApplicationStage(
          stageIndex = 1,
          nameEn = "Activity Eligibility",
          nameAr = "دراسة الأهلية والتصنيف",
          shortCode = "S2: Review",
          status = MisaStageStatus.COMPLETED,
          completionScore = 100f,
          estimatedDuration = "48 Hours",
          ministrySection = "Strategic Investment Advisory Council",
          actionRequired = "Completed - RHQ Economic Activities Matched",
          officialNotes = "Enterprise verified operating in 3 countries outside KSA (UAE, UK, Singapore)."
        ),
        MisaApplicationStage(
          stageIndex = 2,
          nameEn = "Security & Regulatory",
          nameAr = "الموافقات التنظيمية والأمنية",
          shortCode = "S3: Clearance",
          status = MisaStageStatus.COMPLETED,
          completionScore = 100f,
          estimatedDuration = "3 Days",
          ministrySection = "Inter-Agency Security Clearance & SAMA/CST",
          actionRequired = "Completed - Security Clearances Issued",
          officialNotes = "Security review and sovereign clearance certified without reservations."
        ),
        MisaApplicationStage(
          stageIndex = 3,
          nameEn = "SADAD Fee Assessment",
          nameAr = "احتساب الرسوم وإصدار سداد",
          shortCode = "S4: SADAD",
          status = MisaStageStatus.IN_PROGRESS,
          completionScore = 75f,
          estimatedDuration = "24 Hours (Active)",
          ministrySection = "MISA Revenue & Financial Settlement",
          actionRequired = "Invoice SADAD-9481 Issued • Pending Payment Settlement",
          officialNotes = "RHQ license fee waived (0 SAR gov incentive); Chamber fee 2,000 SAR pending SADAD settlement."
        ),
        MisaApplicationStage(
          stageIndex = 4,
          nameEn = "Final License & SBC",
          nameAr = "إصدار الترخيص والربط بالسجل",
          shortCode = "S5: Issued",
          status = MisaStageStatus.PENDING,
          completionScore = 0f,
          estimatedDuration = "Immediate upon Payment",
          ministrySection = "Investor Care Digital Delivery & SBC Integration",
          actionRequired = "Queued - Final Digital Signature Generation",
          officialNotes = "Will generate unified 700 number and electronic investment license PDF."
        )
      )
    ),
    MisaApplicationTrack(
      applicationId = "MISA-INV-2026-382",
      titleEn = "100% Foreign Ownership Trading License",
      titleAr = "ترخيص الاستثمار الأجنبي التجاري بنسبة 100%",
      companyName = "Apex Cloud Innovations Arabia",
      crNumber = "1010892019",
      totalProgress = 40,
      filingDate = "06 Oct 2026",
      estimatedApprovalDate = "16 Oct 2026",
      licenseType = "Commercial & IT Investment • Full Foreign Ownership",
      stages = listOf(
        MisaApplicationStage(
          stageIndex = 0,
          nameEn = "Filing & Apostille",
          nameAr = "تقديم الوثائق والمصادقة",
          shortCode = "S1: Filing",
          status = MisaStageStatus.COMPLETED,
          completionScore = 100f,
          estimatedDuration = "24 Hours",
          ministrySection = "Document Verification",
          actionRequired = "Completed - Certified Financials Submitted",
          officialNotes = "2-year balance sheets uploaded and verified."
        ),
        MisaApplicationStage(
          stageIndex = 1,
          nameEn = "Activity Eligibility",
          nameAr = "دراسة الأهلية والتصنيف",
          shortCode = "S2: Review",
          status = MisaStageStatus.IN_PROGRESS,
          completionScore = 60f,
          estimatedDuration = "Under Active Review",
          ministrySection = "Commercial Licensing Committee",
          actionRequired = "In Progress - Reviewing ISIC Classification (Wholesale IT & Cloud)",
          officialNotes = "Checking capitalization threshold compliance."
        ),
        MisaApplicationStage(
          stageIndex = 2,
          nameEn = "Security & Regulatory",
          nameAr = "الموافقات التنظيمية والأمنية",
          shortCode = "S3: Clearance",
          status = MisaStageStatus.PENDING,
          completionScore = 0f,
          estimatedDuration = "3 Days",
          ministrySection = "Regulatory Clearances",
          actionRequired = "Pending Stage 2 Completion",
          officialNotes = "Scheduled for next workflow."
        ),
        MisaApplicationStage(
          stageIndex = 3,
          nameEn = "SADAD Fee Assessment",
          nameAr = "احتساب الرسوم وإصدار سداد",
          shortCode = "S4: SADAD",
          status = MisaStageStatus.PENDING,
          completionScore = 0f,
          estimatedDuration = "24 Hours",
          ministrySection = "Financial Settlement",
          actionRequired = "Pending",
          officialNotes = "Standard MISA annual subscription fee (10,000 SAR)."
        ),
        MisaApplicationStage(
          stageIndex = 4,
          nameEn = "Final License & SBC",
          nameAr = "إصدار الترخيص والربط بالسجل",
          shortCode = "S5: Issued",
          status = MisaStageStatus.PENDING,
          completionScore = 0f,
          estimatedDuration = "Immediate",
          ministrySection = "SBC Registry",
          actionRequired = "Pending",
          officialNotes = "Will issue digital MISA certificate."
        )
      )
    ),
    MisaApplicationTrack(
      applicationId = "MISA-STARTUP-2026-118",
      titleEn = "MISA Innovative Startup License",
      titleAr = "ترخيص ريادة الأعمال التقنية المبتكرة",
      companyName = "Sovereign AI Labs KSA",
      crNumber = "1010776512",
      totalProgress = 100,
      filingDate = "20 Sep 2026",
      estimatedApprovalDate = "24 Sep 2026",
      licenseType = "Fast-track Entrepreneurship • VC Endorsed",
      stages = listOf(
        MisaApplicationStage(
          stageIndex = 0,
          nameEn = "Filing & Apostille",
          nameAr = "تقديم الوثائق والمصادقة",
          shortCode = "S1: Filing",
          status = MisaStageStatus.COMPLETED,
          completionScore = 100f,
          estimatedDuration = "Same Day",
          ministrySection = "Startup Accelerator Desk",
          actionRequired = "Completed - VC Endorsement Accepted",
          officialNotes = "Endorsed by Saudi Venture Capital fund."
        ),
        MisaApplicationStage(
          stageIndex = 1,
          nameEn = "Activity Eligibility",
          nameAr = "دراسة الأهلية والتصنيف",
          shortCode = "S2: Review",
          status = MisaStageStatus.COMPLETED,
          completionScore = 100f,
          estimatedDuration = "24 Hours",
          ministrySection = "Innovation Committee",
          actionRequired = "Completed - Zero Minimum Capital Approved",
          officialNotes = "Approved under Vision 2030 tech founder initiative."
        ),
        MisaApplicationStage(
          stageIndex = 2,
          nameEn = "Security & Regulatory",
          nameAr = "الموافقات التنظيمية والأمنية",
          shortCode = "S3: Clearance",
          status = MisaStageStatus.COMPLETED,
          completionScore = 100f,
          estimatedDuration = "24 Hours",
          ministrySection = "National Fast-Track Desk",
          actionRequired = "Completed - Expedited Approval Granted",
          officialNotes = "Approved in 24 hours."
        ),
        MisaApplicationStage(
          stageIndex = 3,
          nameEn = "SADAD Fee Assessment",
          nameAr = "احتساب الرسوم وإصدار سداد",
          shortCode = "S4: SADAD",
          status = MisaStageStatus.COMPLETED,
          completionScore = 100f,
          estimatedDuration = "Same Day",
          ministrySection = "Settlement Desk",
          actionRequired = "Completed - Subsidized Fee Settled",
          officialNotes = "2,000 SAR subsidized startup fee paid."
        ),
        MisaApplicationStage(
          stageIndex = 4,
          nameEn = "Final License & SBC",
          nameAr = "إصدار الترخيص والربط بالسجل",
          shortCode = "S5: Issued",
          status = MisaStageStatus.COMPLETED,
          completionScore = 100f,
          estimatedDuration = "Instant",
          ministrySection = "Saudi Business Center",
          actionRequired = "Active & Verified • Digital License #102031094821",
          officialNotes = "100% active in commercial registry."
        )
      )
    )
  )
}

@Composable
fun MisaProgressTrackerView(
  isArabic: Boolean = false,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val applicationTracks = remember { MisaApplicationsRepository.getDefaultTracks() }
  var selectedApplicationIndex by remember { mutableStateOf(0) }
  var selectedStageIndex by remember { mutableStateOf(3) } // Default to active stage S4
  var isBarChart by remember { mutableStateOf(true) }

  val activeTrack = applicationTracks[selectedApplicationIndex]

  // Vico Model: values corresponding to the 5 stages
  val vicoChartEntryModel = remember(activeTrack) {
    val scores = activeTrack.stages.map { it.completionScore }
    entryModelOf(*scores.toTypedArray())
  }

  val bottomAxisLabels = remember(activeTrack) {
    activeTrack.stages.map { it.shortCode }
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("misa_progress_tracker_view"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Header with Ministry of Investment Branding
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = Color(0xFF4A148C).copy(alpha = 0.15f),
            modifier = Modifier.size(42.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                tint = Color(0xFF4A148C),
                modifier = Modifier.size(22.dp)
              )
            }
          }
          Column {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(
                text = if (isArabic) "وزارة الاستثمار • MISA" else "MINISTRY OF INVESTMENT • MISA",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A148C),
                letterSpacing = 1.sp
              )
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFF4A148C)
              ) {
                Text(
                  text = "STAGE TRACKER",
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
              }
            }
            Text(
              text = if (isArabic) "متابعة مراحل ترخيص الاستثمار" else "MISA Licensing Stage Progress",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Circular Overall Progress Pill
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF4A148C).copy(alpha = 0.12f)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = "${activeTrack.totalProgress}%",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.ExtraBold,
              color = Color(0xFF4A148C)
            )
            Text(
              text = if (isArabic) "مكتمل" else "Done",
              style = MaterialTheme.typography.labelSmall,
              fontSize = 10.sp,
              color = Color(0xFF4A148C)
            )
          }
        }
      }

      // 2. Application Switcher (Tabs)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        applicationTracks.forEachIndexed { index, track ->
          val isSelected = selectedApplicationIndex == index
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) Color(0xFF4A148C) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier
              .clickable {
                selectedApplicationIndex = index
                selectedStageIndex = track.stages.indexOfFirst { it.status == MisaStageStatus.IN_PROGRESS }.takeIf { it >= 0 } ?: 4
              }
              .testTag("misa_app_tab_$index")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = if (track.totalProgress == 100) Icons.Default.CheckCircle else Icons.Default.Timelapse,
                contentDescription = null,
                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = track.applicationId,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      // 3. Application Details Summary Card
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (isArabic) activeTrack.titleAr else activeTrack.titleEn,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = if (activeTrack.totalProgress == 100) Color(0xFF166534).copy(alpha = 0.15f) else Color(0xFFE65100).copy(alpha = 0.15f)
            ) {
              Text(
                text = if (activeTrack.totalProgress == 100) "LICENSED" else "IN REVIEW",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = if (activeTrack.totalProgress == 100) Color(0xFF166534) else Color(0xFFE65100),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Text(
            text = "Enterprise: ${activeTrack.companyName} • CR: ${activeTrack.crNumber}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Filing Date: ${activeTrack.filingDate}",
              fontSize = 10.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "Est. Clearance: ${activeTrack.estimatedApprovalDate}",
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF4A148C)
            )
          }
        }
      }

      // 4. Vico Chart Progress Visualization Card
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(
                imageVector = Icons.Default.Timeline,
                contentDescription = null,
                tint = Color(0xFF4A148C),
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = if (isArabic) "مقياس إنجاز المراحل (Vico Chart)" else "Stage Progress Velocity (Vico Chart)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }

            // Chart Type Toggle (Bar vs Line)
            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(2.dp)
            ) {
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (isBarChart) Color(0xFF4A148C) else Color.Transparent,
                modifier = Modifier
                  .clickable { isBarChart = true }
                  .testTag("misa_chart_type_bar")
              ) {
                Icon(
                  imageVector = Icons.Default.BarChart,
                  contentDescription = "Bar Chart",
                  tint = if (isBarChart) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier
                    .padding(3.dp)
                    .size(16.dp)
                )
              }
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (!isBarChart) Color(0xFF4A148C) else Color.Transparent,
                modifier = Modifier
                  .clickable { isBarChart = false }
                  .testTag("misa_chart_type_line")
              ) {
                Icon(
                  imageVector = Icons.Default.ShowChart,
                  contentDescription = "Line Chart",
                  tint = if (!isBarChart) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier
                    .padding(3.dp)
                    .size(16.dp)
                )
              }
            }
          }

          // Render Vico Chart Component
          if (isBarChart) {
            Chart(
              chart = columnChart(),
              model = vicoChartEntryModel,
              startAxis = rememberStartAxis(
                valueFormatter = { value, _ -> "${value.toInt()}%" }
              ),
              bottomAxis = rememberBottomAxis(
                valueFormatter = { value, _ ->
                  val index = value.toInt()
                  if (index in bottomAxisLabels.indices) bottomAxisLabels[index] else ""
                }
              ),
              modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .testTag("misa_vico_chart")
            )
          } else {
            Chart(
              chart = lineChart(),
              model = vicoChartEntryModel,
              startAxis = rememberStartAxis(
                valueFormatter = { value, _ -> "${value.toInt()}%" }
              ),
              bottomAxis = rememberBottomAxis(
                valueFormatter = { value, _ ->
                  val index = value.toInt()
                  if (index in bottomAxisLabels.indices) bottomAxisLabels[index] else ""
                }
              ),
              modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .testTag("misa_vico_line_chart")
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              Box(modifier = Modifier.size(8.dp).background(Color(0xFF4A148C), CircleShape))
              Text("Stage Completion %", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
              text = "Live Ministry Feed • SLA 5-7 Days",
              fontSize = 9.sp,
              color = Color(0xFF166534),
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      // 5. Interactive Stage Stepper Row
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = if (isArabic) "مراحل الطلب الخمس (انقر للتفاصيل)" else "Official Application Stages (Tap to inspect)",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          activeTrack.stages.forEachIndexed { index, stage ->
            val isSelected = selectedStageIndex == index
            val (badgeColor, statusLabel) = when (stage.status) {
              MisaStageStatus.COMPLETED -> Color(0xFF166534) to "Passed"
              MisaStageStatus.IN_PROGRESS -> Color(0xFFE65100) to "Active"
              MisaStageStatus.PENDING -> Color(0xFF757575) to "Queued"
            }

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) Color(0xFF4A148C).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
              border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF4A148C))) else null,
              modifier = Modifier
                .width(135.dp)
                .clickable { selectedStageIndex = index }
                .testTag("misa_stage_chip_$index")
            ) {
              Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "Stage ${index + 1}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color(0xFF4A148C) else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = badgeColor.copy(alpha = 0.2f)
                  ) {
                    Text(
                      text = statusLabel,
                      fontSize = 8.sp,
                      fontWeight = FontWeight.Bold,
                      color = badgeColor,
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                  }
                }

                Text(
                  text = if (isArabic) stage.nameAr else stage.nameEn,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  maxLines = 2
                )

                LinearProgressIndicator(
                  progress = { stage.completionScore / 100f },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                  color = if (stage.status == MisaStageStatus.COMPLETED) Color(0xFF166534) else Color(0xFF4A148C),
                  trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )
              }
            }
          }
        }
      }

      // 6. Selected Stage Deep-Dive Card
      val detailedStage = activeTrack.stages.getOrNull(selectedStageIndex) ?: activeTrack.stages[0]
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF4A148C).copy(alpha = 0.08f),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF4A148C).copy(alpha = 0.3f))),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Surface(
                shape = CircleShape,
                color = Color(0xFF4A148C),
                modifier = Modifier.size(24.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                    text = "${detailedStage.stageIndex + 1}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
              Text(
                text = if (isArabic) detailedStage.nameAr else detailedStage.nameEn,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A148C)
              )
            }

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFF4A148C).copy(alpha = 0.15f)
            ) {
              Text(
                text = "Turnaround: ${detailedStage.estimatedDuration}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A148C),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
              text = "Reviewing Body: ${detailedStage.ministrySection}",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "Action: ${detailedStage.actionRequired}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "Official Reviewer Note: \"${detailedStage.officialNotes}\"",
              fontSize = 11.sp,
              color = Color(0xFF166534),
              fontWeight = FontWeight.Medium
            )
          }

          // Action Buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedButton(
              onClick = {
                Toast.makeText(context, "Syncing status with MISA Sovereign Gateway API...", Toast.LENGTH_SHORT).show()
              },
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              modifier = Modifier.testTag("misa_sync_api_button")
            ) {
              Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Sync MISA API", fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
              onClick = {
                Toast.makeText(context, "Dossier report generated for ${activeTrack.applicationId}", Toast.LENGTH_SHORT).show()
              },
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A148C)),
              modifier = Modifier.testTag("misa_download_dossier_button")
            ) {
              Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Download Dossier", fontSize = 11.sp)
            }
          }
        }
      }
    }
  }
}
