package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DocumentType
import com.example.data.PersonalDocument

@Composable
fun PersonalDocumentCard(
  document: PersonalDocument,
  isArabic: Boolean = false,
  onViewDetails: (PersonalDocument) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  val cardGradient = when (document.docType) {
    DocumentType.IQAMA -> Brush.linearGradient(
      colors = listOf(Color(0xFF0A5C36), Color(0xFF1B824E), Color(0xFF043820))
    )
    DocumentType.COMMERCIAL_REGISTRATION -> Brush.linearGradient(
      colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2), Color(0xFF0A2E63))
    )
    DocumentType.MISA_LICENSE -> Brush.linearGradient(
      colors = listOf(Color(0xFF4A148C), Color(0xFF6A1B9A), Color(0xFF2E0854))
    )
    DocumentType.PASSPORT -> Brush.linearGradient(
      colors = listOf(Color(0xFF1B5E20), Color(0xFF2E7D32), Color(0xFF0E3812))
    )
    DocumentType.DRIVING_LICENSE -> Brush.linearGradient(
      colors = listOf(Color(0xFF263238), Color(0xFF37474F), Color(0xFF1E272C))
    )
    DocumentType.NATIONAL_ADDRESS -> Brush.linearGradient(
      colors = listOf(Color(0xFFE65100), Color(0xFFF57C00), Color(0xFFBF360C))
    )
    DocumentType.GOSI_CERTIFICATE -> Brush.linearGradient(
      colors = listOf(Color(0xFF4A148C), Color(0xFF6A1B9A), Color(0xFF310D5C))
    )
    DocumentType.QIWA_CONTRACT -> Brush.linearGradient(
      colors = listOf(Color(0xFF004D40), Color(0xFF00796B), Color(0xFF00332C))
    )
  }

  val docIcon = when (document.docType) {
    DocumentType.IQAMA -> Icons.Default.Badge
    DocumentType.COMMERCIAL_REGISTRATION -> Icons.Default.Business
    DocumentType.MISA_LICENSE -> Icons.Default.AccountBalance
    DocumentType.PASSPORT -> Icons.Default.FlightTakeoff
    DocumentType.DRIVING_LICENSE -> Icons.Default.DirectionsCar
    DocumentType.NATIONAL_ADDRESS -> Icons.Default.LocationOn
    DocumentType.GOSI_CERTIFICATE -> Icons.Default.Shield
    DocumentType.QIWA_CONTRACT -> Icons.Default.Handshake
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onViewDetails(document) },
    shape = RoundedCornerShape(16.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(cardGradient)
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Surface(
              shape = CircleShape,
              color = Color.White.copy(alpha = 0.2f),
              modifier = Modifier.size(32.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = docIcon,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
            Column {
              Text(
                text = if (isArabic) document.titleAr else document.titleEn,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = if (isArabic) document.issuingAuthorityAr else document.issuingAuthorityEn,
                color = Color(0xFFFFD700),
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (document.isExpiringSoon) Color(0xFFFF9800) else Color(0xFF00C853).copy(alpha = 0.9f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(11.dp)
              )
              Text(
                text = document.status,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Middle Row: Holder Name & Details
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (isArabic) document.holderNameAr else document.holderNameEn,
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            if (isArabic) {
              Text(
                text = document.holderNameEn,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp
              )
            } else {
              Text(
                text = document.holderNameAr,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp
              )
            }
          }

          IconButton(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Document Number", document.documentNumber)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "Copied ID: ${document.documentNumber}", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "Copy Document Number",
              tint = Color.White.copy(alpha = 0.85f),
              modifier = Modifier.size(16.dp)
            )
          }
        }

        // Bottom Details & Number
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Bottom
        ) {
          Column {
            Text(
              text = "DOCUMENT NUMBER / الرقم",
              color = Color.White.copy(alpha = 0.7f),
              fontSize = 8.sp,
              letterSpacing = 0.5.sp
            )
            Text(
              text = document.documentNumber,
              color = Color(0xFFFFD700),
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
              fontFamily = FontFamily.Monospace
            )
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "EXPIRY / تاريخ الصلاحية",
              color = Color.White.copy(alpha = 0.7f),
              fontSize = 8.sp
            )
            Text(
              text = document.expiryDate,
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Action Pill
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White.copy(alpha = 0.15f),
            modifier = Modifier.clickable { onViewDetails(document) }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.QrCode2,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = if (isArabic) "عرض الباركود والتفاصيل" else "View Barcode & Full Card",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun DocumentDetailModalDialog(
  document: PersonalDocument?,
  isArabic: Boolean = false,
  onDismiss: () -> Unit
) {
  if (document == null) return
  val context = LocalContext.current

  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(onClick = onDismiss) {
        Text(if (isArabic) "إغلاق" else "Close")
      }
    },
    dismissButton = {
      OutlinedButton(
        onClick = {
          val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
          val clip = ClipData.newPlainText("Document Number", document.documentNumber)
          clipboard.setPrimaryClip(clip)
          Toast.makeText(context, "Copied ID: ${document.documentNumber}", Toast.LENGTH_SHORT).show()
        }
      ) {
        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(if (isArabic) "نسخ الرقم" else "Copy Number")
      }
    },
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.VerifiedUser,
          contentDescription = null,
          tint = Color(0xFF0A5C36)
        )
        Text(
          text = if (isArabic) document.titleAr else document.titleEn,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = if (isArabic) "الجهة المصدرة:" else "Issuing Authority:",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = if (isArabic) document.issuingAuthorityAr else document.issuingAuthorityEn,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.primary
            )
          }
        }

        // Document Number & Dates
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(text = "Document Number", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = document.documentNumber, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
          }
          Column(horizontalAlignment = Alignment.End) {
            Text(text = "Status", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = document.status, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF166534))
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(text = "Issue Date", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = document.issueDate, fontSize = 12.sp, fontWeight = FontWeight.Medium)
          }
          Column(horizontalAlignment = Alignment.End) {
            Text(text = "Expiry Date", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = document.expiryDate, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }

        // Additional Fields
        if (document.fields.isNotEmpty()) {
          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            document.fields.forEach { (key, value) ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = key, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
              }
            }
          }
        }

        // QR Code Simulated Box
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surface,
          border = CardDefaults.outlinedCardBorder(),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.QrCode2,
              contentDescription = "QR Code",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(90.dp)
            )
            Text(
              text = "Official Cryptographic Digital Stamp",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = document.qrData.take(45) + "...",
              fontSize = 8.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  )
}
