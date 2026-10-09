package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.ui.components.BBCILogoView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
  currentUser: UserEntity?,
  onBackClick: () -> Unit,
  onSaveProfile: (UserEntity) -> Unit
) {
  var fullName by remember { mutableStateOf(currentUser?.fullName ?: "") }
  var companyName by remember { mutableStateOf(currentUser?.companyName ?: "") }
  var crNumber by remember { mutableStateOf(currentUser?.crNumber ?: "") }
  var phone by remember { mutableStateOf(currentUser?.phone ?: "") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Edit Profile") },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Text("←")
          }
        }
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(rememberScrollState())
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      BBCILogoView(width = 160, height = 80)

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Update Company & User Info",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(24.dp))

      OutlinedTextField(
        value = fullName,
        onValueChange = { fullName = it },
        label = { Text("Full Name") },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = companyName,
        onValueChange = { companyName = it },
        label = { Text("Company Name") },
        leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = crNumber,
        onValueChange = { crNumber = it },
        label = { Text("CR Number") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        label = { Text("Phone Number") },
        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(32.dp))

      Button(
        onClick = {
          if (currentUser != null) {
            val updated = currentUser.copy(
              fullName = fullName,
              companyName = companyName,
              crNumber = crNumber,
              phone = phone
            )
            onSaveProfile(updated)
            onBackClick()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
      }
    }
  }
}
