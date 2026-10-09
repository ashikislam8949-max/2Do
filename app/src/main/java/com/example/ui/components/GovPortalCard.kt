package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GovPortal

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GovPortalCard(
  portal: GovPortal,
  isArabic: Boolean = false,
  onRequestAssistance: (GovPortal) -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Top Row: Portal Name & Official Status Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Row(
          modifier = Modifier.weight(1f),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = CircleShape,
            color = Color(portal.badgeColorHex).copy(alpha = 0.15f),
            modifier = Modifier.size(42.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                tint = Color(portal.badgeColorHex),
                modifier = Modifier.size(22.dp)
              )
            }
          }

          Column {
            Text(
              text = if (isArabic) portal.nameAr else portal.nameEn,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = if (isArabic) portal.authorityAr else portal.authorityEn,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFF166534).copy(alpha = 0.12f)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = null,
              tint = Color(0xFF166534),
              modifier = Modifier.size(12.dp)
            )
            Text(
              text = portal.status,
              color = Color(0xFF166534),
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Description
      Text(
        text = if (isArabic) portal.descriptionAr else portal.descriptionEn,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2
      )

      // Supported Services Chips
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        portal.popularServices.forEach { service ->
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
          ) {
            Text(
              text = service,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSecondaryContainer,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

      // Bottom Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedButton(
          onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(portal.portalUrl))
            try {
              context.startActivity(intent)
            } catch (e: Exception) {
              e.printStackTrace()
            }
          },
          shape = RoundedCornerShape(10.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(if (isArabic) "فتح البوابة الرسمية" else "Launch Portal", fontSize = 12.sp)
        }

        Button(
          onClick = { onRequestAssistance(portal) },
          shape = RoundedCornerShape(10.dp),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Text(if (isArabic) "طلب الخدمة عبر 2Do Tech" else "Assisted by 2Do Tech", fontSize = 12.sp)
          Spacer(modifier = Modifier.width(4.dp))
          Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
        }
      }
    }
  }
}
