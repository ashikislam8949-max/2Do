package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.GovernmentCenter
import com.example.data.GovernmentCentersRepository
import com.example.ui.components.GovCentersInteractiveMapView
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GovBranchesMapScreen(
  onBackClick: () -> Unit,
  currentLanguage: String = "EN",
  isDarkMode: Boolean = false
) {
  val context = LocalContext.current
  val isArabic = currentLanguage == "AR"

  var selectedCity by remember { mutableStateOf("Riyadh") }
  var selectedCategory by remember { mutableStateOf("All") }
  var searchQuery by remember { mutableStateOf("") }
  var viewMode by remember { mutableStateOf("split") } // "map", "list", "split"

  // Live Location states from Play Services Location
  var userLat by remember { mutableDoubleStateOf(24.7136) }
  var userLng by remember { mutableDoubleStateOf(46.6753) }
  var isGpsActive by remember { mutableStateOf(false) }
  var locationStatusText by remember { mutableStateOf("Default GPS Location (Riyadh)") }
  var isLocating by remember { mutableStateOf(false) }

  // FusedLocationProviderClient from Google Play Services Location
  val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

  fun fetchDeviceLocation() {
    isLocating = true
    try {
      fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
          isLocating = false
          if (location != null) {
            userLat = location.latitude
            userLng = location.longitude
            isGpsActive = true
            // Detect City roughly
            val detected = when {
              userLat in 21.0..22.8 && userLng in 38.8..40.2 -> "Jeddah"
              userLat in 25.5..27.5 && userLng in 49.5..51.0 -> "Dammam"
              userLat in 21.2..21.6 && userLng in 39.6..40.0 -> "Makkah"
              userLat in 24.3..24.7 && userLng in 39.4..39.8 -> "Madinah"
              else -> "Riyadh"
            }
            selectedCity = detected
            locationStatusText = "GPS Active: ${"%.4f".format(userLat)}° N, ${"%.4f".format(userLng)}° E"
          } else {
            locationStatusText = "Play Services location not ready. Retaining $selectedCity."
          }
        }
        .addOnFailureListener {
          isLocating = false
          locationStatusText = "GPS location error: ${it.localizedMessage}"
        }
    } catch (e: SecurityException) {
      isLocating = false
      locationStatusText = "Location permission needed."
    }
  }

  // Location Permission Launcher
  val locationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    if (isGranted) {
      fetchDeviceLocation()
    } else {
      locationStatusText = "Location permission denied. Using manual city selection."
    }
  }

  // Initial trigger
  LaunchedEffect(Unit) {
    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
  }

  // Recalculate centers when coordinates or city changes
  val centers = remember(userLat, userLng, selectedCity, selectedCategory, searchQuery) {
    val base = GovernmentCentersRepository.getCentersWithCalculatedDistance(
      userLat = userLat,
      userLng = userLng,
      cityFilter = selectedCity,
      categoryFilter = if (selectedCategory == "All") null else selectedCategory
    )
    if (searchQuery.isBlank()) {
      base
    } else {
      base.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
          it.nameAr.contains(searchQuery, ignoreCase = true) ||
          it.address.contains(searchQuery, ignoreCase = true) ||
          it.servicesOffered.any { s -> s.contains(searchQuery, ignoreCase = true) }
      }
    }
  }

  var selectedCenter by remember { mutableStateOf<GovernmentCenter?>(null) }

  // Set initial selected center
  LaunchedEffect(centers) {
    if (selectedCenter == null && centers.isNotEmpty()) {
      selectedCenter = centers.first()
    } else if (centers.none { it.id == selectedCenter?.id }) {
      selectedCenter = centers.firstOrNull()
    }
  }

  val cities = listOf("Riyadh", "Jeddah", "Dammam", "Makkah", "Madinah")
  val categories = listOf("All", "Passport Office (Jawazat)", "Tax Bureau (ZATCA)", "Ministry of Commerce", "MISA Investment Center", "Absher Kiosk")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = if (isArabic) "مراكز وفروع الخدمات الحكومية" else "Government Centers & Branches Map",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = locationStatusText,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(
            onClick = {
              locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
          ) {
            if (isLocating) {
              CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
              Icon(
                imageVector = Icons.Default.GpsFixed,
                contentDescription = "Find My GPS Location",
                tint = if (isGpsActive) Color(0xFF10B981) else MaterialTheme.colorScheme.primary
              )
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // 1. Search Bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text(if (isArabic) "بحث بالاسم، الخدمة (مثل: إقامة، بصمة)..." else "Search centers or services (e.g., Iqama, Biometrics)...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Close, contentDescription = "Clear")
            }
          }
        },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
      )

      // 2. City Filter Chips
      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(cities) { city ->
          FilterChip(
            selected = selectedCity == city,
            onClick = {
              selectedCity = city
              val coords = GovernmentCentersRepository.getCityDefaultCoordinates(city)
              userLat = coords.first
              userLng = coords.second
            },
            label = { Text(city) },
            leadingIcon = if (selectedCity == city) {
              { Icon(Icons.Default.LocationCity, contentDescription = null, modifier = Modifier.size(16.dp)) }
            } else null
          )
        }
      }

      // 3. Category Filter Chips
      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(categories) { cat ->
          val label = when (cat) {
            "Passport Office (Jawazat)" -> "🛂 Jawazat (Passports)"
            "Tax Bureau (ZATCA)" -> "📊 ZATCA (Tax)"
            "Ministry of Commerce" -> "🏢 Commerce"
            "MISA Investment Center" -> "💼 MISA Investment"
            "Absher Kiosk" -> "🪪 Absher Kiosks"
            else -> "🌐 All Agencies"
          }
          FilterChip(
            selected = selectedCategory == cat,
            onClick = { selectedCategory = cat },
            label = { Text(label) }
          )
        }
      }

      // 4. View Mode Segmented Controls (Split, Map Only, List Only)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${centers.size} branches found",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.primary
        )

        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(2.dp),
          horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
          IconButton(
            onClick = { viewMode = "split" },
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(if (viewMode == "split") MaterialTheme.colorScheme.primary else Color.Transparent)
          ) {
            Icon(
              imageVector = Icons.Default.ViewAgenda,
              contentDescription = "Split View",
              tint = if (viewMode == "split") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(16.dp)
            )
          }

          IconButton(
            onClick = { viewMode = "map" },
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(if (viewMode == "map") MaterialTheme.colorScheme.primary else Color.Transparent)
          ) {
            Icon(
              imageVector = Icons.Default.Map,
              contentDescription = "Map View",
              tint = if (viewMode == "map") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(16.dp)
            )
          }

          IconButton(
            onClick = { viewMode = "list" },
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(if (viewMode == "list") MaterialTheme.colorScheme.primary else Color.Transparent)
          ) {
            Icon(
              imageVector = Icons.Default.FormatListBulleted,
              contentDescription = "List View",
              tint = if (viewMode == "list") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // 5. Main Content: Map & List based on viewMode
      when (viewMode) {
        "map" -> {
          GovCentersInteractiveMapView(
            userLatitude = userLat,
            userLongitude = userLng,
            centers = centers,
            selectedCenter = selectedCenter,
            onCenterSelected = { selectedCenter = it },
            modifier = Modifier
              .fillMaxSize()
              .padding(12.dp),
            isDarkMode = isDarkMode
          )
        }
        "list" -> {
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
          ) {
            items(centers) { center ->
              BranchItemCard(
                center = center,
                isSelected = selectedCenter?.id == center.id,
                onClick = { selectedCenter = center },
                context = context
              )
            }
          }
        }
        else -> { // Split View
          Column(modifier = Modifier.fillMaxSize()) {
            GovCentersInteractiveMapView(
              userLatitude = userLat,
              userLongitude = userLng,
              centers = centers,
              selectedCenter = selectedCenter,
              onCenterSelected = { selectedCenter = it },
              modifier = Modifier
                .fillMaxWidth()
                .weight(1.1f)
                .padding(horizontal = 12.dp, vertical = 6.dp),
              isDarkMode = isDarkMode
            )

            LazyColumn(
              modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)
                .padding(horizontal = 16.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp),
              contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp)
            ) {
              items(centers) { center ->
                BranchItemCard(
                  center = center,
                  isSelected = selectedCenter?.id == center.id,
                  onClick = { selectedCenter = center },
                  context = context
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun BranchItemCard(
  center: GovernmentCenter,
  isSelected: Boolean,
  onClick: () -> Unit,
  context: Context
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surface
    ),
    border = if (isSelected) CardDefaults.outlinedCardBorder() else null
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = center.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
          if (center.nameAr.isNotBlank()) {
            Text(
              text = center.nameAr,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Text(
            text = center.address,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFF10B981).copy(alpha = 0.15f)
        ) {
          Text(
            text = "${center.distanceKm} km",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF059669),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      // Services offered chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        items(center.servicesOffered.take(3)) { service ->
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
          ) {
            Text(
              text = service,
              style = MaterialTheme.typography.labelSmall,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }

      // Quick Direction and Call Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        TextButton(
          onClick = {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${center.phone}"))
            try {
              context.startActivity(callIntent)
            } catch (e: Exception) {
              e.printStackTrace()
            }
          }
        ) {
          Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Call", style = MaterialTheme.typography.labelSmall)
        }

        Spacer(modifier = Modifier.width(4.dp))

        Button(
          onClick = {
            val gmmIntentUri = Uri.parse("geo:${center.latitude},${center.longitude}?q=${center.latitude},${center.longitude}(${Uri.encode(center.name)})")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
              setPackage("com.google.android.apps.maps")
            }
            try {
              context.startActivity(mapIntent)
            } catch (e: Exception) {
              val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${center.latitude},${center.longitude}"))
              context.startActivity(webIntent)
            }
          },
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Navigate", style = MaterialTheme.typography.labelSmall)
        }
      }
    }
  }
}
