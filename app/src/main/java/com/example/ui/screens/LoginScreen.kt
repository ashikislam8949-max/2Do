package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.example.ui.components.TwoDoTechLogo
import com.example.util.BiometricHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
  onLoginSuccess: () -> Unit,
  onNavigateRegister: () -> Unit,
  onBackClick: () -> Unit,
  onLoginSubmit: (String, String, (Boolean, String) -> Unit) -> Unit
) {
  var email by remember { mutableStateOf("ceo@alriyadhtech.sa") }
  var password by remember { mutableStateOf("••••••••") }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var isLoading by remember { mutableStateOf(false) }

  val context = LocalContext.current
  val activity = context as? FragmentActivity

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("2Do Tech Business Portal", fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Text("←", fontSize = 20.sp)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      )
    }
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState()),
      contentAlignment = Alignment.Center
    ) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp)
          .widthIn(max = 480.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(28.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          TwoDoTechLogo(width = 170, height = 85)
          
          Spacer(modifier = Modifier.height(20.dp))
          
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "SECURE GOVERNMENT GATEWAY",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Corporate Sign In",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Access Saudi GovTech & Enterprise Services",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(28.dp))

          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enterprise Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(16.dp))

          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )

          if (errorMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.errorContainer,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(12.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(24.dp))

          Button(
            onClick = {
              if (email.isBlank() || password.isBlank()) {
                errorMessage = "Please fill in all corporate credentials"
                return@Button
              }
              isLoading = true
              errorMessage = null
              onLoginSubmit(email, password) { success, msg ->
                isLoading = false
                if (success) {
                  onLoginSuccess()
                } else {
                  errorMessage = msg
                }
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
          ) {
            if (isLoading) {
              CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
              Text("Authenticate & Sign In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedButton(
            onClick = {
              if (activity != null) {
                BiometricHelper.authenticate(
                  activity = activity,
                  title = "2Do Tech Biometric Authentication",
                  subtitle = "Use Fingerprint or Face Unlock for quick secure sign-in",
                  onSuccess = {
                    onLoginSuccess()
                  },
                  onError = { err ->
                    errorMessage = err
                  }
                )
              } else {
                errorMessage = "Biometric authentication unavailable"
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign In with Biometric (Fingerprint/Face)", fontWeight = FontWeight.Bold)
          }

          Spacer(modifier = Modifier.height(20.dp))

          Divider(color = MaterialTheme.colorScheme.outlineVariant)

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "New Enterprise Partner?",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = onNavigateRegister) {
              Text("Register Account", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
