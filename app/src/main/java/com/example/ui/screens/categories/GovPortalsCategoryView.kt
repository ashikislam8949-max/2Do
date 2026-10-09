package com.example.ui.screens.categories

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GovPortal
import com.example.data.ServiceRequestEntity
import com.example.ui.components.GovPortalCard
import com.example.ui.components.MisaProgressTrackerView
import com.example.ui.components.UsageAnalyticsSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GovPortalsCategoryView(
  portals: List<GovPortal>,
  isArabic: Boolean = false,
  serviceRequests: List<ServiceRequestEntity> = emptyList(),
  onOpenBranchesMap: () -> Unit = {},
  onPortalServiceClick: (GovPortal) -> Unit = {},
  onNavigateRequests: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var selectedPortalCategory by remember { mutableStateOf("All") }
  var searchQuery by remember { mutableStateOf("") }

  val portalCategories = listOf(
    "All",
    "Residency & Passports",
    "Tax & E-Invoicing",
    "Labor & HR",
    "Business & Investment",
    "Municipal"
  )

  val filteredPortals = remember(portals, selectedPortalCategory, searchQuery) {
    portals.filter { p ->
      val matchesCat = selectedPortalCategory == "All" || p.category.equals(selectedPortalCategory, ignoreCase = true)
      val matchesSearch = searchQuery.isBlank() ||
        p.nameEn.contains(searchQuery, ignoreCase = true) ||
        p.nameAr.contains(searchQuery, ignoreCase = true) ||
        p.authorityEn.contains(searchQuery, ignoreCase = true) ||
        p.popularServices.any { it.contains(searchQuery, ignoreCase = true) }
      matchesCat && matchesSearch
    }
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(bottom = 24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // E-Government Portals Hero Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF0A5C36).copy(alpha = 0.12f))
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Color(0xFF0A5C36), modifier = Modifier.size(18.dp))
            Text(
              text = if (isArabic) "البوابات والمنصات الحكومية الرسمية" else "OFFICIAL SAUDI E-GOV PORTALS",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0A5C36)
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (isArabic) "دليل المنصات الحكومية الموحدة" else "Unified KSA Government Digital Directory",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = if (isArabic) "ربط مباشر ومعتمد مع أبشر، مقيم، زاتكا، قوى، وبلدي" else "Direct integration & assisted services for Absher, Muqeem, ZATCA, Qiwa & Balady",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Nearby Passport & Gov Centers Shortcut Button
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(36.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(Icons.Default.Map, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
          }
          Column {
            Text(
              text = if (isArabic) "فروع ومراكز الجوازات القريبة" else "Nearby Passport Offices & Kiosks",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = if (isArabic) "استعراض المراكز الحكومية على الخريطة التفاعلية" else "Locate nearest Jawazat & ZATCA branches with live distance",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        FilledTonalButton(
          onClick = onOpenBranchesMap,
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text(if (isArabic) "الخريطة" else "View Map")
        }
      }
    }

    // Search Box
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      placeholder = { Text(if (isArabic) "البحث عن بوابة (مقيم، أبشر، زاتكا، قوى)..." else "Search portal (Muqeem, Absher, ZATCA, Qiwa)...") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(Icons.Default.Clear, contentDescription = "Clear")
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(12.dp)
    )

    // Category Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      portalCategories.forEach { cat ->
        FilterChip(
          selected = selectedPortalCategory == cat,
          onClick = { selectedPortalCategory = cat },
          label = { Text(cat) },
          shape = RoundedCornerShape(8.dp)
        )
      }
    }

    // Portals List
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Text(
        text = if (isArabic) "🏛️ البوابات المعتمدة (${filteredPortals.size})" else "🏛️ Official E-Government Portals (${filteredPortals.size})",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )

      if (filteredPortals.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (isArabic) "لا توجد بوابات مطابقة" else "No matching government portals found.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      } else {
        filteredPortals.forEach { portal ->
          GovPortalCard(
            portal = portal,
            isArabic = isArabic,
            onRequestAssistance = { onPortalServiceClick(portal) }
          )
        }
      }
    }

    // MISA Licensing Application Progress Tracker with Vico Chart
    MisaProgressTrackerView(
      isArabic = isArabic,
      modifier = Modifier.padding(horizontal = 16.dp)
    )

    // Usage Analytics Section with Vico Interactive Chart
    UsageAnalyticsSection(
      serviceRequests = serviceRequests,
      isArabic = isArabic,
      onNavigateRequests = onNavigateRequests,
      modifier = Modifier.padding(horizontal = 16.dp)
    )
  }
}
