package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.DualDateResult
import com.example.util.HijriCalendarHelper

enum class ConversionDirection {
  GREGORIAN_TO_HIJRI,
  HIJRI_TO_GREGORIAN
}

@Composable
fun HijriDateConverterCard(
  isArabic: Boolean = false,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var direction by remember { mutableStateOf(ConversionDirection.GREGORIAN_TO_HIJRI) }

  // Gregorian inputs
  var gYear by remember { mutableIntStateOf(2026) }
  var gMonth by remember { mutableIntStateOf(10) }
  var gDay by remember { mutableIntStateOf(8) }

  // Hijri inputs
  var hYear by remember { mutableIntStateOf(1448) }
  var hMonth by remember { mutableIntStateOf(4) } // Rabi' al-Thani
  var hDay by remember { mutableIntStateOf(24) }

  // Converted result
  val conversionResult: DualDateResult = remember(direction, gYear, gMonth, gDay, hYear, hMonth, hDay) {
    if (direction == ConversionDirection.GREGORIAN_TO_HIJRI) {
      HijriCalendarHelper.gregorianToHijri(gYear, gMonth, gDay)
    } else {
      HijriCalendarHelper.hijriToGregorian(hYear, hMonth, hDay)
    }
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("hijri_date_converter_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Header with Official Umm Al-Qura Badge
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
            color = Color(0xFF0A5C36).copy(alpha = 0.15f),
            modifier = Modifier.size(38.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color(0xFF0A5C36),
                modifier = Modifier.size(20.dp)
              )
            }
          }
          Column {
            Text(
              text = if (isArabic) "محوّل التاريخ الهجري والميلادي" else "Hijri ⇄ Gregorian Converter",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = if (isArabic) "تقويم أم القرى الرسمي المعتمد في المعاملات الحكومية" else "Official KSA Umm Al-Qura Calendar Standard",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFF166534).copy(alpha = 0.12f)
        ) {
          Text(
            text = "UMM AL-QURA",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF166534),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      // 2. Conversion Direction Switcher
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (direction == ConversionDirection.GREGORIAN_TO_HIJRI) MaterialTheme.colorScheme.primary else Color.Transparent,
          modifier = Modifier
            .weight(1f)
            .clickable { direction = ConversionDirection.GREGORIAN_TO_HIJRI }
            .testTag("direction_greg_to_hijri")
        ) {
          Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (isArabic) "ميلادي ➔ هجري" else "Gregorian ➔ Hijri",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (direction == ConversionDirection.GREGORIAN_TO_HIJRI) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (direction == ConversionDirection.HIJRI_TO_GREGORIAN) MaterialTheme.colorScheme.primary else Color.Transparent,
          modifier = Modifier
            .weight(1f)
            .clickable { direction = ConversionDirection.HIJRI_TO_GREGORIAN }
            .testTag("direction_hijri_to_greg")
        ) {
          Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (isArabic) "هجري ➔ ميلادي" else "Hijri ➔ Gregorian",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (direction == ConversionDirection.HIJRI_TO_GREGORIAN) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      // 3. Quick Government Presets
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        AssistChip(
          onClick = {
            val today = HijriCalendarHelper.getTodayDualDate()
            gYear = today.gregorian.year
            gMonth = today.gregorian.month
            gDay = today.gregorian.day
            hYear = today.hijri.year
            hMonth = today.hijri.month
            hDay = today.hijri.day
          },
          label = { Text(if (isArabic) "اليوم (تاريخ اليوم)" else "Today's Date", fontSize = 11.sp) },
          leadingIcon = { Icon(Icons.Default.Today, contentDescription = null, modifier = Modifier.size(14.dp)) },
          shape = RoundedCornerShape(8.dp)
        )

        AssistChip(
          onClick = {
            // ZATCA VAT Filing Due Date (End of Month)
            direction = ConversionDirection.GREGORIAN_TO_HIJRI
            gYear = 2026
            gMonth = 10
            gDay = 31
          },
          label = { Text("ZATCA Tax Deadline", fontSize = 11.sp) },
          shape = RoundedCornerShape(8.dp)
        )

        AssistChip(
          onClick = {
            // 1 Year CR Renewal Target
            direction = ConversionDirection.HIJRI_TO_GREGORIAN
            hYear = 1449
            hMonth = 1
            hDay = 1
          },
          label = { Text("1449 AH New Year", fontSize = 11.sp) },
          shape = RoundedCornerShape(8.dp)
        )
      }

      // 4. Date Picker Input Selectors (Animated depending on direction)
      AnimatedContent(
        targetState = direction,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "date_input_fields"
      ) { currentDir ->
        if (currentDir == ConversionDirection.GREGORIAN_TO_HIJRI) {
          // Gregorian Input Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Day selector
            DateNumberStepper(
              label = if (isArabic) "اليوم" else "Day",
              value = gDay,
              range = 1..31,
              onValueChange = { gDay = it },
              modifier = Modifier.weight(1f)
            )

            // Month selector
            DateMonthStepper(
              label = if (isArabic) "الشهر" else "Month",
              monthIndex = gMonth,
              monthName = HijriCalendarHelper.getGregorianMonthName(gMonth, isArabic),
              onValueChange = { gMonth = it },
              modifier = Modifier.weight(1.4f)
            )

            // Year selector
            DateNumberStepper(
              label = if (isArabic) "السنة (م)" else "Year (AD)",
              value = gYear,
              range = 2020..2035,
              onValueChange = { gYear = it },
              modifier = Modifier.weight(1.2f)
            )
          }
        } else {
          // Hijri Input Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Hijri Day selector
            DateNumberStepper(
              label = if (isArabic) "اليوم" else "Day",
              value = hDay,
              range = 1..30,
              onValueChange = { hDay = it },
              modifier = Modifier.weight(1f)
            )

            // Hijri Month selector
            DateMonthStepper(
              label = if (isArabic) "الشهر" else "Month",
              monthIndex = hMonth,
              monthName = HijriCalendarHelper.getHijriMonthName(hMonth, isArabic),
              onValueChange = { hMonth = it },
              modifier = Modifier.weight(1.4f)
            )

            // Hijri Year selector
            DateNumberStepper(
              label = if (isArabic) "السنة (هـ)" else "Year (AH)",
              value = hYear,
              range = 1440..1455,
              onValueChange = { hYear = it },
              modifier = Modifier.weight(1.2f)
            )
          }
        }
      }

      // 5. Converted Result Card
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF0A5C36).copy(alpha = 0.08f),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF0A5C36).copy(alpha = 0.4f))),
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
            Text(
              text = if (isArabic) "النتيجة الموثقة (التاريخان)" else "Official Synchronized Dual Date",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0A5C36),
              letterSpacing = 1.sp
            )

            IconButton(
              onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val textToCopy = if (isArabic) conversionResult.dualDisplayAr else conversionResult.dualDisplayEn
                clipboard.setPrimaryClip(ClipData.newPlainText("Saudi Dual Date", textToCopy))
                Toast.makeText(context, if (isArabic) "تم نسخ التاريخ المزدوج" else "Copied dual date to clipboard", Toast.LENGTH_SHORT).show()
              },
              modifier = Modifier
                .size(28.dp)
                .testTag("copy_converted_date_button")
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = "Copy Date", tint = Color(0xFF0A5C36), modifier = Modifier.size(16.dp))
            }
          }

          // Hijri Date Result Display
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Surface(shape = CircleShape, color = Color(0xFF0A5C36), modifier = Modifier.size(20.dp)) {
                Box(contentAlignment = Alignment.Center) {
                  Text("هـ", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
              }
              Text(
                text = if (isArabic) "التاريخ الهجري:" else "Hijri (Umm Al-Qura):",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Text(
              text = if (isArabic) conversionResult.hijri.formattedAr else conversionResult.hijri.formattedEn,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0A5C36)
            )
          }

          // Gregorian Date Result Display
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Surface(shape = CircleShape, color = Color(0xFF1565C0), modifier = Modifier.size(20.dp)) {
                Box(contentAlignment = Alignment.Center) {
                  Text("م", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
              }
              Text(
                text = if (isArabic) "التاريخ الميلادي:" else "Gregorian:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Text(
              text = if (isArabic) conversionResult.gregorian.formattedAr else conversionResult.gregorian.formattedEn,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1565C0)
            )
          }

          HorizontalDivider(color = Color(0xFF0A5C36).copy(alpha = 0.2f))

          // Unified Legal Format
          Text(
            text = "Official Format: ${if (isArabic) conversionResult.dualDisplayAr else conversionResult.dualDisplayEn}",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }
  }
}

@Composable
private fun DateNumberStepper(
  label: String,
  value: Int,
  range: IntRange,
  onValueChange: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
    border = CardDefaults.outlinedCardBorder(),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
      Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        IconButton(
          onClick = { if (value > range.first) onValueChange(value - 1) },
          modifier = Modifier.size(26.dp)
        ) {
          Icon(Icons.Default.Remove, contentDescription = "Decrement", modifier = Modifier.size(14.dp))
        }

        Text(
          text = value.toString(),
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace
        )

        IconButton(
          onClick = { if (value < range.last) onValueChange(value + 1) },
          modifier = Modifier.size(26.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = "Increment", modifier = Modifier.size(14.dp))
        }
      }
    }
  }
}

@Composable
private fun DateMonthStepper(
  label: String,
  monthIndex: Int,
  monthName: String,
  onValueChange: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
    border = CardDefaults.outlinedCardBorder(),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
      Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        IconButton(
          onClick = {
            val newM = if (monthIndex > 1) monthIndex - 1 else 12
            onValueChange(newM)
          },
          modifier = Modifier.size(26.dp)
        ) {
          Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month", modifier = Modifier.size(16.dp))
        }

        Text(
          text = monthName,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          textAlign = TextAlign.Center
        )

        IconButton(
          onClick = {
            val newM = if (monthIndex < 12) monthIndex + 1 else 1
            onValueChange(newM)
          },
          modifier = Modifier.size(26.dp)
        ) {
          Icon(Icons.Default.ChevronRight, contentDescription = "Next Month", modifier = Modifier.size(16.dp))
        }
      }
    }
  }
}
