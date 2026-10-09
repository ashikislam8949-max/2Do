package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RequestSuccessScreen(
  requestId: String,
  onViewRequests: () -> Unit,
  onGoHome: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.size(96.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(56.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      Text(
        text = "Service Request Submitted!",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Your application has been successfully registered with BBCI Gateway. Request ID: $requestId. Our government relations & IT specialists are processing your request.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(32.dp))

      Button(
        onClick = onViewRequests,
        modifier = Modifier.fillMaxWidth().height(50.dp)
      ) {
        Text("Track Requests")
      }

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedButton(
        onClick = onGoHome,
        modifier = Modifier.fillMaxWidth().height(50.dp)
      ) {
        Text("Back to Gateway Home")
      }
    }
  }
}
