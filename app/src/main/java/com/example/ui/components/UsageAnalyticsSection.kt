package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceRequestEntity
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.core.entry.entryModelOf

enum class ChartType {
  BAR, LINE
}

enum class AnalyticsTimeframe(val labelEn: String, val labelAr: String) {
  THIS_WEEK("7 Days", "أسبوع"),
  THIS_MONTH("30 Days", "شهر"),
  SIX_MONTHS("6 Months", "٦ أشهر")
}

data class ServiceInteractionStat(
  val portalName: String,
  val portalNameAr: String,
  val icon: androidx.compose.ui.graphics.vector.ImageVector,
  val interactionCount: Int,
  val percentage: Float,
  val trendDescription: String,
  val badgeColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsageAnalyticsSection(
  serviceRequests: List<ServiceRequestEntity> = emptyList(),
  isArabic: Boolean = false,
  onNavigateRequests: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var selectedChartType by remember { mutableStateOf(ChartType.BAR) }
  var selectedTimeframe by remember { mutableStateOf(AnalyticsTimeframe.SIX_MONTHS) }
  var selectedPortalFilter by remember { mutableStateOf("All") }

  // Monthly timeline interaction data points for line chart & bar chart
  // Represents user transactions with Muqeem, Absher, ZATCA, Qiwa, MISA, GOSI, Balady
  val monthlyData = remember(serviceRequests, selectedTimeframe) {
    val base = when (selectedTimeframe) {
      AnalyticsTimeframe.THIS_WEEK -> listOf(8, 12, 6, 15, 10, 18, 14)
      AnalyticsTimeframe.THIS_MONTH -> listOf(14, 22, 19, 31, 26, 38)
      AnalyticsTimeframe.SIX_MONTHS -> listOf(24, 38, 45, 52, 48, 64)
    }
    // Blend with real service requests count if any submitted
    val bonus = (serviceRequests.size * 2).coerceAtMost(10)
    base.map { it + bonus }
  }

  // Category breakdown data
  val categoryData = remember(serviceRequests) {
    listOf(
      ServiceInteractionStat(
        portalName = "ZATCA E-Invoicing",
        portalNameAr = "زاتكا الفوترة الإلكترونية",
        icon = Icons.Default.ReceiptLong,
        interactionCount = 48 + serviceRequests.count { it.serviceTitle.contains("ZATCA", ignoreCase = true) },
        percentage = 0.88f,
        trendDescription = "+18% this month",
        badgeColor = Color(0xFF1565C0)
      ),
      ServiceInteractionStat(
        portalName = "Muqeem & Jawazat",
        portalNameAr = "مقيم والجوازات",
        icon = Icons.Default.Badge,
        interactionCount = 42 + serviceRequests.count { it.serviceTitle.contains("Iqama", ignoreCase = true) || it.serviceTitle.contains("Muqeem", ignoreCase = true) },
        percentage = 0.76f,
        trendDescription = "+12% renewals",
        badgeColor = Color(0xFF0A5C36)
      ),
      ServiceInteractionStat(
        portalName = "Qiwa Work Permits",
        portalNameAr = "منصة قوى للعمل",
        icon = Icons.Default.WorkOutline,
        interactionCount = 35 + serviceRequests.count { it.serviceTitle.contains("Qiwa", ignoreCase = true) },
        percentage = 0.64f,
        trendDescription = "+8% contracts",
        badgeColor = Color(0xFF00695C)
      ),
      ServiceInteractionStat(
        portalName = "Absher Business",
        portalNameAr = "أبشر أعمال",
        icon = Icons.Default.AccountBalance,
        interactionCount = 31 + serviceRequests.count { it.serviceTitle.contains("Absher", ignoreCase = true) },
        percentage = 0.58f,
        trendDescription = "Stable",
        badgeColor = Color(0xFF2E7D32)
      ),
      ServiceInteractionStat(
        portalName = "GOSI Compliance",
        portalNameAr = "التأمينات الاجتماعية",
        icon = Icons.Default.Shield,
        interactionCount = 27,
        percentage = 0.49f,
        trendDescription = "Monthly auto-sync",
        badgeColor = Color(0xFF4A148C)
      ),
      ServiceInteractionStat(
        portalName = "Balady & CR Renewal",
        portalNameAr = "بلدي والسجل التجاري",
        icon = Icons.Default.Store,
        interactionCount = 20,
        percentage = 0.36f,
        trendDescription = "Annual renewal",
        badgeColor = Color(0xFFE65100)
      )
    )
  }

  val totalInteractions = remember(categoryData) { categoryData.sumOf { it.interactionCount } }

  // Chart entries based on selected timeframe
  val chartEntryModel = remember(monthlyData) {
    entryModelOf(*monthlyData.toTypedArray())
  }

  // Label formatting for Bottom Axis
  val bottomLabels = when (selectedTimeframe) {
    AnalyticsTimeframe.THIS_WEEK -> listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")
    AnalyticsTimeframe.THIS_MONTH -> listOf("W1", "W2", "W3", "W4", "W5", "Current")
    AnalyticsTimeframe.SIX_MONTHS -> listOf("May", "Jun", "Jul", "Aug", "Sep", "Oct")
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("usage_analytics_section"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Header: Title & Total Badge
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
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(38.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.BarChart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
              )
            }
          }
          Column {
            Text(
              text = if (isArabic) "📊 تحليلات الاستخدام والخدمات" else "📊 Government Usage Analytics",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = if (isArabic) "سجل التفاعل مع المنصات والخدمات الحكومية" else "Interaction history across official KSA portals",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = Color(0xFF166534).copy(alpha = 0.12f)
        ) {
          Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "$totalInteractions",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold,
              color = Color(0xFF166534)
            )
            Text(
              text = if (isArabic) "عملية منجزة" else "Total Ops",
              style = MaterialTheme.typography.labelSmall,
              fontSize = 9.sp,
              color = Color(0xFF166534)
            )
          }
        }
      }

      // Timeframe Filter Chips & Chart Type Switcher
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Timeframe selector
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          AnalyticsTimeframe.values().forEach { tf ->
            val isSelected = selectedTimeframe == tf
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              modifier = Modifier
                .clickable { selectedTimeframe = tf }
                .testTag("analytics_timeframe_${tf.name.lowercase()}")
            ) {
              Text(
                text = if (isArabic) tf.labelAr else tf.labelEn,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        // Toggle between Bar Chart and Line Chart
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(2.dp)
        ) {
          // Bar Chart icon button
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (selectedChartType == ChartType.BAR) MaterialTheme.colorScheme.surface else Color.Transparent,
            modifier = Modifier
              .clickable { selectedChartType = ChartType.BAR }
              .testTag("chart_type_bar")
          ) {
            Icon(
              imageVector = Icons.Default.BarChart,
              contentDescription = "Bar Chart",
              tint = if (selectedChartType == ChartType.BAR) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier
                .padding(4.dp)
                .size(18.dp)
            )
          }

          // Line Chart icon button
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (selectedChartType == ChartType.LINE) MaterialTheme.colorScheme.surface else Color.Transparent,
            modifier = Modifier
              .clickable { selectedChartType = ChartType.LINE }
              .testTag("chart_type_line")
          ) {
            Icon(
              imageVector = Icons.Default.ShowChart,
              contentDescription = "Line Chart",
              tint = if (selectedChartType == ChartType.LINE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier
                .padding(4.dp)
                .size(18.dp)
            )
          }
        }
      }

      // Vico Chart Visualization Area
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (selectedChartType == ChartType.BAR) {
                if (isArabic) "معدل العمليات الشهري (أعمدة)" else "Monthly Operation Volume (Bar Chart)"
              } else {
                if (isArabic) "منحنى وتيرة التفاعل (خطي)" else "Interaction Trend Curve (Line Chart)"
              },
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.primary
            )

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(MaterialTheme.colorScheme.primary, CircleShape)
              )
              Text(
                text = if (isArabic) "العمليات / شهر" else "Transactions / Period",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Chart with Vico Library
          if (selectedChartType == ChartType.BAR) {
            Chart(
              chart = columnChart(),
              model = chartEntryModel,
              startAxis = rememberStartAxis(),
              bottomAxis = rememberBottomAxis(
                valueFormatter = { value, _ ->
                  val index = value.toInt()
                  if (index in bottomLabels.indices) bottomLabels[index] else ""
                }
              ),
              modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .testTag("vico_bar_chart")
            )
          } else {
            Chart(
              chart = lineChart(),
              model = chartEntryModel,
              startAxis = rememberStartAxis(),
              bottomAxis = rememberBottomAxis(
                valueFormatter = { value, _ ->
                  val index = value.toInt()
                  if (index in bottomLabels.indices) bottomLabels[index] else ""
                }
              ),
              modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .testTag("vico_line_chart")
            )
          }
        }
      }

      // Detailed Portal Usage Breakdown List
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (isArabic) "تفصيل استهلاك المنصات الحكومية" else "Service Usage by Authority",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )

          Text(
            text = if (isArabic) "نسبة الاستخدام" else "Volume Share",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        categoryData.take(4).forEach { stat ->
          PortalUsageRowItem(stat = stat, isArabic = isArabic)
        }
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

      // Bottom Insight & Action
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(
            imageVector = Icons.Default.TrendingUp,
            contentDescription = null,
            tint = Color(0xFF166534),
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = if (isArabic) "الاستخدام الأعلى: زاتكا ومقيم (78% من العمليات)" else "Top Volume: ZATCA & Muqeem (78% of operations)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
          )
        }

        TextButton(
          onClick = onNavigateRequests,
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(if (isArabic) "عرض السجل" else "View Requests")
          Spacer(modifier = Modifier.width(2.dp))
          Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
        }
      }
    }
  }
}

@Composable
private fun PortalUsageRowItem(
  stat: ServiceInteractionStat,
  isArabic: Boolean
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Surface(
          shape = CircleShape,
          color = stat.badgeColor.copy(alpha = 0.12f),
          modifier = Modifier.size(24.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = stat.icon,
              contentDescription = null,
              tint = stat.badgeColor,
              modifier = Modifier.size(14.dp)
            )
          }
        }
        Text(
          text = if (isArabic) stat.portalNameAr else stat.portalName,
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.SemiBold
        )
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text(
          text = "${stat.interactionCount} ops",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = "${(stat.percentage * 100).toInt()}%",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
          )
        }
      }
    }

    LinearProgressIndicator(
      progress = { stat.percentage },
      modifier = Modifier
        .fillMaxWidth()
        .height(5.dp)
        .clip(RoundedCornerShape(3.dp)),
      color = stat.badgeColor,
      trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    )
  }
}
