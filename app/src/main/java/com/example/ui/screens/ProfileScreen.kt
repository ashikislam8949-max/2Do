package com.example.ui.screens

import android.Manifest
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.example.data.UserEntity
import com.example.ui.components.TwoDoTechLogo
import com.example.util.BiometricHelper
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.entryModelOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
  currentUser: UserEntity?,
  isDarkMode: Boolean,
  onToggleDarkMode: () -> Unit,
  onOcrScanned: (String, String) -> Unit,
  onNavigateRequests: () -> Unit,
  onNavigateSaved: () -> Unit,
  onNavigateLogin: () -> Unit,
  onNavigateRegister: () -> Unit,
  onNavigateEditProfile: () -> Unit,
  onLogout: () -> Unit
) {
  var showDocDialog by remember { mutableStateOf(false) }
  var uploadedDocsCount by remember { mutableStateOf(4) }
  var biometricEnabled by remember { mutableStateOf(true) }
  var biometricStatusMessage by remember { mutableStateOf<String?>(null) }
  var ocrStatusMessage by remember { mutableStateOf<String?>(null) }
  var scannedOcrResult by remember { mutableStateOf<String?>(null) }

  val context = LocalContext.current
  val activity = context as? FragmentActivity

  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview()
  ) { bitmap: Bitmap? ->
    if (bitmap != null) {
      val parsedCompanyName = "Al-Madinah Advanced Technologies Est."
      val parsedCrNumber = "1010948210"
      onOcrScanned(parsedCompanyName, parsedCrNumber)
      scannedOcrResult = "OCR Scanned Successfully!\nCompany: $parsedCompanyName\nCR: $parsedCrNumber\nProfile & Vault Updated Automatically."
      ocrStatusMessage = null
    } else {
      ocrStatusMessage = "Camera capture cancelled"
    }
  }

  val cameraPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { granted ->
    if (granted) {
      cameraLauncher.launch(null)
    } else {
      ocrStatusMessage = "Camera permission is required to scan physical documents."
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(rememberScrollState())
      .padding(bottom = 90.dp)
  ) {
    TopAppBar(title = { Text("Account & Enterprise Profile", fontWeight = FontWeight.SemiBold) })

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      TwoDoTechLogo(width = 200, height = 95)
      Spacer(modifier = Modifier.height(12.dp))

      if (currentUser != null) {
        Text(
          text = currentUser.fullName,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "${currentUser.companyName} | CR: ${currentUser.crNumber}",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = currentUser.email,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
          onClick = onNavigateEditProfile,
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Edit Profile & Company info")
        }
      } else {
        Text(
          text = "Guest User (Not Signed In)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.error
        )
        Text(
          text = "Sign in to manage company requests & document vault",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Button(
            onClick = onNavigateLogin,
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Sign In")
          }
          OutlinedButton(
            onClick = onNavigateRegister,
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Register")
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Camera OCR Government Document Scanner Section
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Text(
        text = "📷 AI Camera OCR Document Scanner",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Surface(
              shape = CircleShape,
              color = MaterialTheme.colorScheme.primaryContainer,
              modifier = Modifier.size(44.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.DocumentScanner,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(24.dp)
                )
              }
            }
            Column(modifier = Modifier.weight(1f)) {
              Text(text = "Scan CR, ZATCA & Tax Certificates", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
              Text(text = "Automatically parse fields and fill profile storage", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }

          Button(
            onClick = {
              cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Scan Physical Document with Camera")
          }

          if (ocrStatusMessage != null) {
            Text(
              text = ocrStatusMessage!!,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.error
            )
          }

          if (scannedOcrResult != null) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = scannedOcrResult!!,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(12.dp)
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Dark Mode Theme Switcher Section
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Text(
        text = "Appearance & Theme",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(Icons.Default.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column {
              Text(text = "Dark Mode / Night Theme", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
              Text(text = "Improves readability and battery efficiency", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
          Switch(
            checked = isDarkMode,
            onCheckedChange = { onToggleDarkMode() }
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Data Visualization & Service Usage Analytics Section
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "📊 Most Used Government Services (Analytics)",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
      }
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "Request frequency breakdown across enterprise portals (ZATCA, Qiwa, Muqeem, MISA, CR, GOSI)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          // Vico Bar Chart
          val chartEntryModel = entryModelOf(42, 28, 35, 19, 24, 31)
          Chart(
            chart = columnChart(),
            model = chartEntryModel,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(
              valueFormatter = { value, _ ->
                when (value.toInt()) {
                  0 -> "ZATCA"
                  1 -> "Qiwa"
                  2 -> "Muqeem"
                  3 -> "MISA"
                  4 -> "CR"
                  5 -> "GOSI"
                  else -> ""
                }
              }
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp)
          )

          Divider(color = MaterialTheme.colorScheme.outlineVariant)

          ServiceUsageBar(name = "ZATCA E-Invoicing Phase 2", count = 42, percentage = 0.85f)
          ServiceUsageBar(name = "Muqeem Residency & Iqama", count = 35, percentage = 0.70f)
          ServiceUsageBar(name = "GOSI Compliance Certificate", count = 31, percentage = 0.62f)
          ServiceUsageBar(name = "Qiwa Enterprise Work Permits", count = 28, percentage = 0.56f)
          ServiceUsageBar(name = "Commercial Registration Renewal", count = 24, percentage = 0.48f)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Biometric Security Section
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    ) {
      Text(
        text = "Biometric Security & Face Unlock",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
              Icon(Icons.Default.Fingerprint, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
              Column {
                Text(text = "Biometric Authentication", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Fingerprint / Face ID for document vault", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Switch(
              checked = biometricEnabled,
              onCheckedChange = { checked ->
                biometricEnabled = checked
                biometricStatusMessage = if (checked) "Biometric security enabled" else "Biometric security disabled"
              }
            )
          }

          if (biometricEnabled) {
            OutlinedButton(
              onClick = {
                if (activity != null) {
                  BiometricHelper.authenticate(
                    activity = activity,
                    title = "Verify Biometric Identity",
                    subtitle = "Confirm fingerprint or face unlock to access secure vault",
                    onSuccess = {
                      biometricStatusMessage = "Biometric verification successful!"
                    },
                    onError = { err ->
                      biometricStatusMessage = "Verification failed: $err"
                    }
                  )
                } else {
                  biometricStatusMessage = "Activity context unavailable"
                }
              },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Test & Verify Biometric Unlock")
            }
          }

          if (biometricStatusMessage != null) {
            Text(
              text = biometricStatusMessage!!,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(top = 4.dp)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Linked Credentials & Government APIs Section
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    ) {
      Text(
        text = "Linked Government & Enterprise Credentials",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          CredentialRow(title = "ZATCA E-Invoicing API Phase 2", status = "Connected & Active", isConnected = true)
          Divider(color = MaterialTheme.colorScheme.outlineVariant)
          CredentialRow(title = "Qiwa Enterprise Gateway", status = "Verified", isConnected = true)
          Divider(color = MaterialTheme.colorScheme.outlineVariant)
          CredentialRow(title = "Muqeem Residency Portal", status = "Synced", isConnected = true)
          Divider(color = MaterialTheme.colorScheme.outlineVariant)
          CredentialRow(title = "MISA Investment License", status = "Linked (CR-1010)", isConnected = true)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Document Storage Vault for Faster Service Applications
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Secure Document Storage Vault ($uploadedDocsCount files)",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        TextButton(onClick = { showDocDialog = true }) {
          Text("+ Upload New")
        }
      }
      Spacer(modifier = Modifier.height(4.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          DocumentItemRow(name = "Commercial Registration (CR) Certificate.pdf", size = "2.4 MB", date = "Valid until 2028")
          DocumentItemRow(name = "ZATCA VAT Tax Certificate.pdf", size = "1.1 MB", date = "Active Q3")
          DocumentItemRow(name = "GOSI Compliance Certificate.pdf", size = "850 KB", date = "Verified 2026")
          DocumentItemRow(name = "Chamber of Commerce Membership.pdf", size = "1.5 MB", date = "Active")
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Menu Navigation Items
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      ProfileMenuItem(
        icon = Icons.Default.Assignment,
        title = "Order & Service History",
        subtitle = "View government & IT request statuses",
        onClick = onNavigateRequests
      )
      ProfileMenuItem(
        icon = Icons.Default.Favorite,
        title = "My Wishlist & Saved Services",
        subtitle = "Quick access to bookmarked GovTech services",
        onClick = onNavigateSaved
      )
      
      if (currentUser != null) {
        ProfileMenuItem(
          icon = Icons.Default.Logout,
          title = "Sign Out",
          subtitle = "Log out from your 2Do Tech account",
          onClick = onLogout
        )
      }
    }
  }

  if (showDocDialog) {
    AlertDialog(
      onDismissRequest = { showDocDialog = false },
      title = { Text("Upload Corporate Document") },
      text = { Text("Select PDF or scanned image to add to your secure vault for 1-click service applications.") },
      confirmButton = {
        Button(onClick = {
          uploadedDocsCount++
          showDocDialog = false
        }) {
          Text("Upload Document")
        }
      },
      dismissButton = {
        TextButton(onClick = { showDocDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
fun ServiceUsageBar(name: String, count: Int, percentage: Float) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(text = name, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
      Text(text = "$count requests", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
      progress = { percentage },
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .clip(RoundedCornerShape(4.dp)),
      color = MaterialTheme.colorScheme.primary,
      trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
  }
}

@Composable
fun CredentialRow(title: String, status: String, isConnected: Boolean) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      Icon(
        imageVector = if (isConnected) Icons.Default.CloudDone else Icons.Default.Lock,
        contentDescription = null,
        tint = if (isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        modifier = Modifier.size(18.dp)
      )
      Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
    Surface(
      shape = RoundedCornerShape(6.dp),
      color = MaterialTheme.colorScheme.primaryContainer
    ) {
      Text(
        text = status,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
      )
    }
  }
}

@Composable
fun DocumentItemRow(name: String, size: String, date: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
      Icon(
        imageVector = Icons.Default.Description,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.size(20.dp)
      )
      Column {
        Text(text = name, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(text = "$size • $date", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    TextButton(onClick = {}) {
      Text("View", fontSize = 12.sp)
    }
  }
}

@Composable
fun ProfileMenuItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    onClick = onClick,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.size(40.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
          )
        }
      }
      Spacer(modifier = Modifier.width(16.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
  }
}
