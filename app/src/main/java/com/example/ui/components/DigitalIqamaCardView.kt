package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun DigitalIqamaCardView(
  iqamaNumber: String = "2489310245",
  fullNameEn: String = "Muhammad Tariq Khan",
  fullNameAr: String = "محمد طارق خان",
  profession: String = "SOFTWARE ENGINEER",
  nationality: String = "PAKISTANI",
  expiryDate: String = "2028-10-14",
  isExpired: Boolean = false,
  modifier: Modifier = Modifier
) {
  val cardGradient = Brush.linearGradient(
    colors = listOf(
      Color(0xFF0A5C36), // Saudi Green
      Color(0xFF1B824E),
      Color(0xFF043820)
    )
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .height(210.dp),
    shape = RoundedCornerShape(16.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(cardGradient)
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        // Top Header: Kingdom of Saudi Arabia / Ministry of Interior
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "المملكة العربية السعودية",
              color = Color.White.copy(alpha = 0.9f),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "KINGDOM OF SAUDI ARABIA",
              color = Color.White.copy(alpha = 0.8f),
              fontSize = 9.sp,
              letterSpacing = 1.sp
            )
            Text(
              text = "Ministry of Interior • Muqeem",
              color = Color(0xFFFFD700), // Gold accent
              fontSize = 8.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (isExpired) Color.Red.copy(alpha = 0.9f) else Color(0xFF00C853).copy(alpha = 0.9f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
              )
              Text(
                text = if (isExpired) "EXPIRED" else "ACTIVE / VALID",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Middle Row: Photo + Details
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Worker / Holder Avatar
          AsyncImage(
            model = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80",
            contentDescription = "Holder Photo",
            modifier = Modifier
              .size(68.dp, 84.dp)
              .clip(RoundedCornerShape(8.dp))
              .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(8.dp))
          )

          Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
          ) {
            Text(
              text = fullNameAr,
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = fullNameEn,
              color = Color.White.copy(alpha = 0.9f),
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(text = "PROFESSION", color = Color.White.copy(alpha = 0.7f), fontSize = 8.sp)
                Text(text = profession, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }
              Column {
                Text(text = "NATIONALITY", color = Color.White.copy(alpha = 0.7f), fontSize = 8.sp)
                Text(text = nationality, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        // Bottom Row: Iqama Number & Expiry Date
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Bottom
        ) {
          Column {
            Text(text = "IQAMA NUMBER / رقم الإقامة", color = Color.White.copy(alpha = 0.7f), fontSize = 8.sp)
            Text(
              text = iqamaNumber,
              color = Color(0xFFFFD700),
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.5.sp
            )
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(text = "EXPIRY DATE / تاريخ الانتهاء", color = Color.White.copy(alpha = 0.7f), fontSize = 8.sp)
            Text(
              text = expiryDate,
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
