package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceEntity
import com.example.ui.components.HijriDateConverterCard
import com.example.util.HijriCalendarHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationFormScreen(
  service: ServiceEntity,
  onBackClick: () -> Unit,
  onSubmitRequest: (String, String, Double, String) -> Unit
) {
  var companyName by remember { mutableStateOf("StarBridge Tech Solutions KSA") }
  var crNumber by remember { mutableStateOf("1010987654") }
  var contactPerson by remember { mutableStateOf("Ashik Islam") }
  var phone by remember { mutableStateOf("+966 50 123 4567") }
  var paymentMethod by remember { mutableStateOf("Corporate SADAD Invoice") }
  var showDateConverter by remember { mutableStateOf(false) }
  val todayDualDate = remember { HijriCalendarHelper.getTodayDualDate() }

  val paymentOptions = listOf("Corporate SADAD Invoice", "Mada / Corporate Credit Card", "Government E-Wallet")
  val totalAmount = service.fee + service.governmentFee

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Service Request Application") },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
          }
        }
      )
    },
    bottomBar = {
      Surface(
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Button(
          onClick = {
            onSubmitRequest(companyName, crNumber, totalAmount, paymentMethod)
          },
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp)
        ) {
          Text("Submit Application (${totalAmount} SAR)")
        }
      }
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())
        .padding(padding)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(text = service.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(4.dp))
          Text(text = "Estimated Time: ${service.duration}", style = MaterialTheme.typography.bodyMedium)
        }
      }

      // Official Saudi Filing Date (Hijri Umm Al-Qura & Gregorian)
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A5C36).copy(alpha = 0.08f))
      ) {
        Column(
          modifier = Modifier.padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
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
              Icon(
                Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color(0xFF0A5C36),
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = "OFFICIAL APPLICATION FILING DATE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A5C36)
              )
            }
            TextButton(
              onClick = { showDateConverter = !showDateConverter },
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = if (showDateConverter) "Hide Converter" else "Hijri Converter ⇄",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Text(
            text = todayDualDate.dualDisplayEn,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = todayDualDate.dualDisplayAr,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF0A5C36)
          )

          AnimatedVisibility(visible = showDateConverter) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
              HijriDateConverterCard()
            }
          }
        }
      }

      Text(
        text = "Enterprise & CR Information",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )

      OutlinedTextField(
        value = companyName,
        onValueChange = { companyName = it },
        label = { Text("Company Legal Name") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
      )

      OutlinedTextField(
        value = crNumber,
        onValueChange = { crNumber = it },
        label = { Text(if (service.category.contains("MISA")) "MISA License No. / Unified 700 / CR" else "Commercial Registration (CR) Number") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = contactPerson,
          onValueChange = { contactPerson = it },
          label = { Text("Authorized Person") },
          modifier = Modifier.weight(1f),
          singleLine = true
        )
        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("Mobile (+966)") },
          modifier = Modifier.weight(1f),
          singleLine = true
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Billing & Payment Method",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )

      paymentOptions.forEach { method ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (paymentMethod == method) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
          ),
          onClick = { paymentMethod = method }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = method,
              fontWeight = FontWeight.Bold,
              color = if (paymentMethod == method) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
            )
            RadioButton(
              selected = paymentMethod == method,
              onClick = { paymentMethod = method }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}
