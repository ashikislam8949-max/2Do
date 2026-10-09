package com.example.ui.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceEntity
import com.example.ui.components.ServiceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItServicesCategoryView(
  allServices: List<ServiceEntity>,
  isArabic: Boolean = false,
  onServiceClick: (String) -> Unit,
  onAiChatClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var selectedItFilter by remember { mutableStateOf("All") }
  var searchQuery by remember { mutableStateOf("") }

  val itFilters = listOf(
    "All",
    "MISA & Investment",
    "Commercial Registration",
    "IT & Cloud",
    "Cybersecurity"
  )

  val itServices = remember(allServices, selectedItFilter, searchQuery) {
    allServices.filter { s ->
      val matchesFilter = when (selectedItFilter) {
        "All" -> true
        "MISA & Investment" -> s.category == "MISA & Investment" || s.title.contains("MISA", ignoreCase = true)
        "Commercial Registration" -> s.category == "Commercial Registration (CR)" || s.category == "Licensing & CR" || s.title.contains("Commercial Registration", ignoreCase = true) || s.title.contains("CR", ignoreCase = true)
        else -> s.category.equals(selectedItFilter, ignoreCase = true)
      }
      val matchesSearch = searchQuery.isBlank() ||
          s.title.contains(searchQuery, ignoreCase = true) ||
          s.description.contains(searchQuery, ignoreCase = true) ||
          s.requirements.contains(searchQuery, ignoreCase = true)
      matchesFilter && matchesSearch
    }
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(bottom = 24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Enterprise IT Banner
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
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
            Icon(Icons.Default.Dns, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Text(
              text = if (isArabic) "البنية التحتية والحلول التقنية" else "ENTERPRISE CLOUD & IT",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (isArabic) "حلول السحابة المعتمدة والأمن السيبراني" else "Cloud, Cybersecurity & ZATCA Integration",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = if (isArabic) "أنظمة متوافقة مع متطلبات هيئة الاتصالات والأمن السيبراني NCA" else "Compliant with CST data residency & NCA ECC-1:2018 guidelines",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
          )
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
      placeholder = { Text(if (isArabic) "البحث في خدمات وزارة الاستثمار والسجل التجاري والسحابة..." else "Search MISA, CR, ZATCA ERP, cloud, cybersecurity...") },
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

    // Filter Chips Row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      itFilters.forEach { filter ->
        FilterChip(
          selected = selectedItFilter == filter,
          onClick = { selectedItFilter = filter },
          label = { Text(filter) },
          shape = RoundedCornerShape(8.dp)
        )
      }
    }

    // Featured IT Solutions Section
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (isArabic) "⚡ الخدمات التقنية المتاحة (${itServices.size})" else "⚡ Enterprise IT Services (${itServices.size})",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (itServices.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (isArabic) "لا توجد خدمات تقنية مطابقة" else "No matching IT services found.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      } else {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          itServices.forEach { service ->
            ServiceCard(
              service = service,
              onClick = { onServiceClick(service.id) }
            )
          }
        }
      }
    }

    // IT Consultation Callout Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(42.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(22.dp))
            }
          }
          Column {
            Text(
              text = if (isArabic) "استشارة فنية وأمن سيبراني" else "Enterprise Architecture & Audit",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = if (isArabic) "احصل على تدقيق امتثال مجاني للأنظمة السحابية" else "Schedule an NCA compliance assessment with our certified team",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Button(
          onClick = onAiChatClick,
          shape = RoundedCornerShape(10.dp)
        ) {
          Text(if (isArabic) "استشر الآن" else "Consult AI")
        }
      }
    }
  }
}
