package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.ServiceEntity
import com.example.ui.components.MisaProgressTrackerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
  service: ServiceEntity,
  isSaved: Boolean,
  onBackClick: () -> Unit,
  onToggleSave: () -> Unit,
  onRequestService: () -> Unit
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(service.category) },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(onClick = onToggleSave) {
            Icon(
              imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = "Save",
              tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      )
    },
    bottomBar = {
      Surface(
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = "Total Service Fee", style = MaterialTheme.typography.labelSmall)
            Text(
              text = "${service.fee + service.governmentFee} SAR",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
          }
          Button(
            onClick = onRequestService,
            modifier = Modifier.height(50.dp)
          ) {
            Text("Request Service")
          }
        }
      }
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(padding)
        .padding(16.dp)
    ) {
      AsyncImage(
        model = service.imageUrl,
        contentDescription = service.title,
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp)
          .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
      )

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = service.category.uppercase(),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold
        )
        Surface(
          color = MaterialTheme.colorScheme.primaryContainer,
          shape = CircleShape
        ) {
          Text(
            text = "Duration: ${service.duration}",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = service.title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Star,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "${service.rating} (${service.reviewCount} client reviews)",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Fee Breakdown Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Fee Structure",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          FeeRow("Gateway Service Fee", "${service.fee} SAR")
          if (service.governmentFee > 0.0) {
            FeeRow("Official Government Fee", "${service.governmentFee} SAR")
          }
          Divider(modifier = Modifier.padding(vertical = 8.dp))
          FeeRow("Total Payable", "${service.fee + service.governmentFee} SAR", isBold = true)
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "Service Scope & Description",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = service.description,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "Required Documents & Prerequisites",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(12.dp))
          Text(
            text = service.requirements,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      if (service.category.contains("MISA", ignoreCase = true) || service.title.contains("MISA", ignoreCase = true)) {
        Spacer(modifier = Modifier.height(20.dp))
        MisaProgressTrackerView()
      }

      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}

@Composable
fun FeeRow(title: String, value: String, isBold: Boolean = false) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = title,
      style = if (isBold) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
      fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
    )
    Text(
      text = value,
      style = if (isBold) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
      fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
      color = if (isBold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    )
  }
}
