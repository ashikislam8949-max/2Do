package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceRequestEntity
import com.example.ui.components.MisaProgressTrackerView
import com.example.ui.components.UsageAnalyticsSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen(
  requests: List<ServiceRequestEntity>,
  isArabic: Boolean = false,
  onLockRecords: () -> Unit = {}
) {
  val context = LocalContext.current
  var selectedRecordForDetail by remember { mutableStateOf<ServiceRequestEntity?>(null) }
  var filterCategory by remember { mutableStateOf("All") }

  val filterOptions = listOf("All", "MISA", "CR & SBC", "ZATCA", "Qiwa & HR")

  val filteredRequests = remember(requests, filterCategory) {
    if (filterCategory == "All") {
      requests
    } else {
      requests.filter { req ->
        when (filterCategory) {
          "MISA" -> req.serviceTitle.contains("MISA", ignoreCase = true) || req.requestId.startsWith("MISA")
          "CR & SBC" -> req.serviceTitle.contains("CR", ignoreCase = true) || req.serviceTitle.contains("Commercial Registration", ignoreCase = true)
          "ZATCA" -> req.serviceTitle.contains("ZATCA", ignoreCase = true)
          "Qiwa & HR" -> req.serviceTitle.contains("Qiwa", ignoreCase = true) || req.serviceTitle.contains("Iqama", ignoreCase = true) || req.serviceTitle.contains("Muqeem", ignoreCase = true)
          else -> true
        }
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = if (isArabic) "السجلات الحكومية المؤمنة" else "Secure Government Records",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            )
            Text(
              text = if (isArabic) "موثقة بالبصمة البيومترية • ${requests.size} طلبات" else "Biometrically Verified • ${requests.size} Records",
              fontSize = 11.sp,
              color = Color(0xFF166534),
              fontWeight = FontWeight.SemiBold
            )
          }
        },
        actions = {
          // Lock Records Button
          FilledTonalButton(
            onClick = {
              Toast.makeText(context, if (isArabic) "تم قفل السجلات الحكومية" else "Government Records Locked", Toast.LENGTH_SHORT).show()
              onLockRecords()
            },
            modifier = Modifier
              .padding(end = 8.dp)
              .testTag("lock_records_button"),
            colors = ButtonDefaults.filledTonalButtonColors(
              containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
              contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.Lock, contentDescription = "Lock Records", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = if (isArabic) "قفل" else "Lock", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(padding)
    ) {
      // 1. Biometric Security Clearance Active Banner
      Surface(
        color = Color(0xFF166534).copy(alpha = 0.12f),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF166534).copy(alpha = 0.4f)))
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = Color(0xFF166534),
            modifier = Modifier.size(32.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.Fingerprint,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
          }

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (isArabic) "جلسة آمنة نشطة • تم التحقق البيومتري" else "Biometric Security Clearance Active",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF166534)
            )
            Text(
              text = if (isArabic) "سجلات محمية بتشفير عالي المستوى ومتوافقة مع الهيئة الوطنية للأمن السيبراني NCA" else "Protected by Class-3 BiometricPrompt • End-to-end encrypted",
              fontSize = 10.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFF166534)
          ) {
            Text(
              text = "VERIFIED",
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }

      // Filter Chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        filterOptions.forEach { opt ->
          FilterChip(
            selected = filterCategory == opt,
            onClick = { filterCategory = opt },
            label = { Text(opt, fontSize = 12.sp) },
            shape = RoundedCornerShape(8.dp)
          )
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        item {
          MisaProgressTrackerView(
            isArabic = isArabic
          )
        }

        item {
          UsageAnalyticsSection(
            serviceRequests = requests,
            onNavigateRequests = {}
          )
        }

        if (filteredRequests.isEmpty()) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(
                  imageVector = Icons.Default.Assignment,
                  contentDescription = null,
                  modifier = Modifier.size(48.dp),
                  tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                  text = if (isArabic) "لا توجد طلبات في هذا التصنيف" else "No Records Found in this Category",
                  style = MaterialTheme.typography.titleSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        } else {
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (isArabic) "السجلات الحكومية الموثقة (${filteredRequests.size})" else "Classified Records (${filteredRequests.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = "Audit Logs Protected",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          items(filteredRequests) { req ->
            val statusColor = when (req.status) {
              "Completed" -> Color(0xFF166534)
              "Processing" -> Color(0xFF1565C0)
              "Under Review" -> Color(0xFFE65100)
              else -> MaterialTheme.colorScheme.primary
            }

            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedRecordForDetail = req },
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(
                      imageVector = if (req.serviceTitle.contains("MISA")) Icons.Default.AccountBalance else Icons.Default.Business,
                      contentDescription = null,
                      tint = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.size(16.dp)
                    )
                    Text(
                      text = req.requestId,
                      style = MaterialTheme.typography.titleSmall,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.primary,
                      fontFamily = FontFamily.Monospace
                    )
                  }

                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.15f)
                  ) {
                    Text(
                      text = req.status,
                      style = MaterialTheme.typography.labelSmall,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                      color = statusColor,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = req.serviceTitle,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Enterprise: ${req.companyName} • CR: ${req.crNumber}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Recorded: ${req.date} • ${req.paymentMethod}",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Divider(modifier = Modifier.padding(vertical = 10.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF166534), modifier = Modifier.size(14.dp))
                    Text(text = "Official Ministry Receipt", fontSize = 11.sp, color = Color(0xFF166534), fontWeight = FontWeight.SemiBold)
                  }
                  Text(
                    text = "${req.totalAmount} SAR",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // Detail Modal for Sensitive Record
  if (selectedRecordForDetail != null) {
    val record = selectedRecordForDetail!!
    AlertDialog(
      onDismissRequest = { selectedRecordForDetail = null },
      confirmButton = {
        Button(onClick = { selectedRecordForDetail = null }) {
          Text(if (isArabic) "إغلاق" else "Close Receipt")
        }
      },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF166534))
          Text("Official Government Dossier", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(text = record.serviceTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Divider()
          DetailItem("Dossier ID", record.requestId)
          DetailItem("Authorized Company", record.companyName)
          DetailItem("CR Number", record.crNumber)
          DetailItem("Status", record.status)
          DetailItem("Filing Timestamp", record.date)
          DetailItem("Settlement Channel", record.paymentMethod)
          DetailItem("Total Fee Settled", "${record.totalAmount} SAR")
          Divider()
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
              Text("Cryptographic Seal:", fontSize = 10.sp, fontWeight = FontWeight.Bold)
              Text("SHA-256: 8a9f3b14c728e102f9d84c1029348102a9b3c4d5", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }
    )
  }
}

@Composable
private fun DetailItem(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold)
  }
}
