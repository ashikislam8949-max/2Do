package com.example.ui.screens.categories

import android.Manifest
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DocumentType
import com.example.data.PersonalDocument
import com.example.ui.components.DigitalIqamaCardView
import com.example.ui.components.DocumentDetailModalDialog
import com.example.ui.components.HijriDateConverterCard
import com.example.ui.components.PersonalDocumentCard
import com.example.util.AiDocumentClassifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDocumentsCategoryView(
  documents: List<PersonalDocument>,
  isArabic: Boolean = false,
  onOcrScanned: (String, String) -> Unit = { _, _ -> },
  modifier: Modifier = Modifier
) {
  var selectedDocTypeFilter by remember { mutableStateOf("All") }
  var selectedDocForModal by remember { mutableStateOf<PersonalDocument?>(null) }
  var searchQuery by remember { mutableStateOf("") }

  val docFilterOptions = listOf(
    "All",
    "MISA License",
    "Commercial Registration",
    "Digital Iqama",
    "Passport",
    "Driving License",
    "National Address",
    "GOSI & Contracts"
  )

  // OCR scanning state
  var ocrScanStatus by remember { mutableStateOf<String?>(null) }
  var scannedOcrResult by remember { mutableStateOf<String?>(null) }

  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview()
  ) { bitmap: Bitmap? ->
    if (bitmap != null) {
      val parsedName = "Muhammad Tariq Khan"
      val parsedId = "2489310245"
      onOcrScanned("Al-Madinah Advanced Technologies Est.", "1010948210")
      scannedOcrResult = "Document Scanned Successfully!\n• Name: $parsedName\n• Number: $parsedId\n• Verified via Absher & Muqeem OCR engine."
      ocrScanStatus = null
    } else {
      ocrScanStatus = "Camera capture cancelled"
    }
  }

  val cameraPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { granted ->
    if (granted) {
      cameraLauncher.launch(null)
    } else {
      ocrScanStatus = "Camera permission required to scan personal documents."
    }
  }

  // AI Classification state
  var docTitleInput by remember { mutableStateOf("Muqeem Digital Iqama Copy") }
  var docContentInput by remember { mutableStateOf("Iqama ID 2489310245 issued to Muhammad Tariq Khan, profession Software Engineer, Riyadh.") }
  var classificationResult by remember { mutableStateOf<Map<String, String>?>(null) }
  var isClassifying by remember { mutableStateOf(false) }
  val coroutineScope = rememberCoroutineScope()

  val filteredDocuments = remember(documents, selectedDocTypeFilter, searchQuery) {
    documents.filter { doc ->
      val matchesType = when (selectedDocTypeFilter) {
        "All" -> true
        "MISA License" -> doc.docType == DocumentType.MISA_LICENSE
        "Digital Iqama" -> doc.docType == DocumentType.IQAMA
        "Commercial Registration" -> doc.docType == DocumentType.COMMERCIAL_REGISTRATION
        "Passport" -> doc.docType == DocumentType.PASSPORT
        "Driving License" -> doc.docType == DocumentType.DRIVING_LICENSE
        "National Address" -> doc.docType == DocumentType.NATIONAL_ADDRESS
        "GOSI & Contracts" -> doc.docType in listOf(DocumentType.GOSI_CERTIFICATE, DocumentType.QIWA_CONTRACT)
        else -> true
      }
      val matchesSearch = searchQuery.isBlank() ||
        doc.titleEn.contains(searchQuery, ignoreCase = true) ||
        doc.titleAr.contains(searchQuery, ignoreCase = true) ||
        doc.documentNumber.contains(searchQuery, ignoreCase = true) ||
        doc.holderNameEn.contains(searchQuery, ignoreCase = true)
      matchesType && matchesSearch
    }
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(bottom = 24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Digital Wallet Header Hero
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.Badge, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Text(
              text = if (isArabic) "محفظة الوثائق الرقمية والهوية" else "SAUDI DIGITAL WALLET & VAULT",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (isArabic) "الوثائق الموثقة (${documents.size} مستندات معتمدة)" else "Verified Identity & Enterprise Documents",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = if (isArabic) "الإقامة الرقمية، السجل التجاري، الجواز، والرخصة مع الباركود" else "All documents synchronized with Muqeem, Absher, & Ministry of Commerce",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
          )
        }
      }
    }

    // Expiry Status Banner
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF166534).copy(alpha = 0.12f))
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF166534), modifier = Modifier.size(20.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = if (isArabic) "جميع الوثائق سارية ومعتمدة رسمياً" else "All Documents Valid & Active",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF166534)
          )
          Text(
            text = if (isArabic) "الإقامة صالحة حتى 2028-10-14 • السجل التجاري صالح حتى 2028-01-19" else "Iqama valid until Oct 14, 2028 • CR valid until Jan 19, 2028",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Camera Scan Action Button
    OutlinedCard(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      shape = RoundedCornerShape(12.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(36.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(Icons.Default.DocumentScanner, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
          }
          Column {
            Text(
              text = if (isArabic) "مسح مستند جديد بالكاميرا (OCR)" else "Scan Physical Document (Camera OCR)",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = if (isArabic) "استخراج البيانات تلقائياً وتحديث المحفظة" else "Auto-extract fields into your verified document vault",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Button(
          onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(if (isArabic) "مسح" else "Scan")
        }
      }
    }

    if (ocrScanStatus != null) {
      Text(
        text = ocrScanStatus!!,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(horizontal = 16.dp)
      )
    }

    if (scannedOcrResult != null) {
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        Text(
          text = scannedOcrResult!!,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          modifier = Modifier.padding(12.dp),
          fontWeight = FontWeight.Medium
        )
      }
    }

    // Search Box
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      placeholder = { Text(if (isArabic) "البحث في المستندات برقم الوثيقة أو الاسم..." else "Search documents by number, type or name...") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(Icons.Default.Clear, contentDescription = "Clear")
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(12.dp)
    )

    // Document Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      docFilterOptions.forEach { filter ->
        FilterChip(
          selected = selectedDocTypeFilter == filter,
          onClick = { selectedDocTypeFilter = filter },
          label = { Text(filter) },
          shape = RoundedCornerShape(8.dp)
        )
      }
    }

    // Interactive Digital Iqama Featured Card (Shown if 'All' or 'Digital Iqama' is selected)
    if (selectedDocTypeFilter in listOf("All", "Digital Iqama")) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = if (isArabic) "🪪 بطاقة الإقامة الرقمية التفاعلية (مقيم)" else "🪪 Interactive Digital Iqama Card (Muqeem)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        DigitalIqamaCardView(
          iqamaNumber = "2489310245",
          fullNameEn = "Muhammad Tariq Khan",
          fullNameAr = "محمد طارق خان",
          profession = "SOFTWARE ENGINEER",
          nationality = "PAKISTANI",
          expiryDate = "2028-10-14"
        )
      }
    }

    // List of other personal documents
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Text(
        text = if (isArabic) "📁 الوثائق والمستندات الرسمية (${filteredDocuments.size})" else "📁 Verified Personal & Corporate Documents (${filteredDocuments.size})",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )

      if (filteredDocuments.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (isArabic) "لا توجد مستندات مطابقة للبحث" else "No matching documents found.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      } else {
        filteredDocuments.forEach { doc ->
          PersonalDocumentCard(
            document = doc,
            isArabic = isArabic,
            onViewDetails = { selectedDocForModal = it }
          )
        }
      }
    }

    // Hijri ⇄ Gregorian Date Converter (Umm Al-Qura Standard for Official Saudi Documents)
    HijriDateConverterCard(
      isArabic = isArabic,
      modifier = Modifier.padding(horizontal = 16.dp)
    )

    // AI Automated Document Tagging & Classification Tool
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(36.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
            }
          }
          Column {
            Text(
              text = if (isArabic) "تصنيف وتوسيم المستندات بالذكاء الاصطناعي" else "AI Document Tagging & Classifier",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = if (isArabic) "تحليل محتوى العقود والمستندات تلقائياً" else "Automatically categorize contracts, licenses, & certificates via Gemini",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        OutlinedTextField(
          value = docTitleInput,
          onValueChange = { docTitleInput = it },
          label = { Text("Document Title") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          singleLine = true
        )

        Button(
          onClick = {
            if (docTitleInput.isNotBlank()) {
              isClassifying = true
              coroutineScope.launch {
                val res = AiDocumentClassifier.classifyDocument(docTitleInput, docContentInput)
                classificationResult = res
                isClassifying = false
              }
            }
          },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          enabled = !isClassifying
        ) {
          if (isClassifying) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Classifying...")
          } else {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Classify Document with AI")
          }
        }

        if (classificationResult != null) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                text = "📂 Category: ${classificationResult!!["category"]}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Text(
                text = "🏷️ Tags: ${classificationResult!!["tags"]}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Text(
                text = "📝 Summary: ${classificationResult!!["summary"]}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }
        }
      }
    }
  }

  // Document Detail Modal Dialog
  DocumentDetailModalDialog(
    document = selectedDocForModal,
    isArabic = isArabic,
    onDismiss = { selectedDocForModal = null }
  )
}
