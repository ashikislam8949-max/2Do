package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceEntity
import com.example.ui.components.ServiceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
  services: List<ServiceEntity>,
  searchQuery: String,
  selectedCategory: String,
  onSearchQueryChange: (String) -> Unit,
  onCategoryChange: (String) -> Unit,
  onServiceClick: (String) -> Unit
) {
  val categories = listOf(
    "All",
    "MISA & Investment",
    "Commercial Registration",
    "Government Services",
    "IT & Cloud",
    "Cybersecurity"
  )
  val quickServiceShortcuts = listOf(
    "MISA License",
    "CR Instant Issuance",
    "CR Renewal",
    "Regional HQ (RHQ)",
    "AoA Notarization",
    "Trade Name Reservation",
    "Sub-CR Branch",
    "ZATCA E-Invoicing",
    "Qiwa Permits"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.BusinessCenter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "KSA GOVTECH DIRECTORY",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }
        Text(
          text = "Services Catalog",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold
        )
      }
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer
      ) {
        Text(
          text = " ${services.size} Available ",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Search Bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onSearchQueryChange,
      modifier = Modifier.fillMaxWidth(),
      placeholder = { Text("Search MISA, CR, ZATCA, Muqeem, Cloud...") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
      singleLine = true,
      shape = RoundedCornerShape(12.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Quick Shortcuts Row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Icon(Icons.Default.FlashOn, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(14.dp))
        Text("Quick:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
      }
      quickServiceShortcuts.forEach { shortcut ->
        AssistChip(
          onClick = { onSearchQueryChange(shortcut) },
          label = { Text(shortcut, fontSize = 11.sp) },
          shape = RoundedCornerShape(8.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Category Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      categories.forEach { cat ->
        FilterChip(
          selected = selectedCategory == cat,
          onClick = { onCategoryChange(cat) },
          label = { Text(cat) },
          shape = RoundedCornerShape(10.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (services.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "No services found matching your query.",
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    } else {
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentPadding = PaddingValues(bottom = 90.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(services) { service ->
          ServiceCard(service = service, onClick = { onServiceClick(service.id) })
        }
      }
    }
  }
}
