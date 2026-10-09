package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.ui.components.TwoDoTechLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
  onRegisterSuccess: () -> Unit,
  onBackClick: () -> Unit,
  onRegisterSubmit: (UserEntity, (Boolean, String) -> Unit) -> Unit
) {
  var fullName by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var companyName by remember { mutableStateOf("") }
  var crNumber by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }

  var errorMessage by remember { mutableStateOf<String?>(null) }
  var isLoading by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Corporate Partner Registration", fontWeight = FontWeight.SemiBold) },
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
        .background(MaterialTheme.colorScheme.background),
      contentAlignment = Alignment.Center
    ) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .widthIn(max = 520.dp)
          .fillMaxHeight(0.95f),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(28.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          TwoDoTechLogo(width = 160, height = 80)

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "SAUDI GOVTECH ENTERPRISE ONBOARDING",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "Create Corporate Account",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Register with your Saudi Commercial Registration (CR)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(24.dp))

          OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Authorized Representative Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Corporate Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(14.dp))

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

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Company Legal Name (e.g. Al-Riyadh Tech)") },
            leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = crNumber,
            onValueChange = { crNumber = it },
            label = { Text("Commercial Registration (CR) Number") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Business Phone (+966...)") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
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
              if (fullName.isBlank() || email.isBlank() || password.isBlank() || companyName.isBlank()) {
                errorMessage = "Please complete all mandatory enterprise fields"
                return@Button
              }
              isLoading = true
              errorMessage = null
              val newUser = UserEntity(
                userId = "USER-${System.currentTimeMillis()}",
                fullName = fullName,
                email = email,
                password = password,
                companyName = companyName,
                crNumber = crNumber.ifBlank { "CR-1010000000" },
                phone = phone.ifBlank { "+966 50 000 0000" }
              )
              onRegisterSubmit(newUser) { success, msg ->
                isLoading = false
                if (success) {
                  onRegisterSuccess()
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
              Text("Complete Registration", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}
