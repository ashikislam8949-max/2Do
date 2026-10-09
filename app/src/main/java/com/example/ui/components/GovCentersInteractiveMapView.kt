package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GovernmentCenter
import kotlin.math.*

@Composable
fun GovCentersInteractiveMapView(
  userLatitude: Double,
  userLongitude: Double,
  centers: List<GovernmentCenter>,
  selectedCenter: GovernmentCenter?,
  onCenterSelected: (GovernmentCenter) -> Unit,
  modifier: Modifier = Modifier,
  isDarkMode: Boolean = true,
  onNavigateToDirections: ((GovernmentCenter) -> Unit)? = null
) {
  val context = LocalContext.current

  // Map viewport states
  var zoomLevel by remember { mutableFloatStateOf(1.0f) }
  var panOffsetX by remember { mutableFloatStateOf(0f) }
  var panOffsetY by remember { mutableFloatStateOf(0f) }

  // Radar pulse animation
  val infiniteTransition = rememberInfiniteTransition(label = "mapRadar")
  val pulseRadius by infiniteTransition.animateFloat(
    initialValue = 12f,
    targetValue = 48f,
    animationSpec = infiniteRepeatable(
      animation = tween(2200, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "pulseRadius"
  )
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.8f,
    targetValue = 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(2200, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "pulseAlpha"
  )

  // When selected center changes, center pan towards it
  LaunchedEffect(selectedCenter) {
    if (selectedCenter != null) {
      val dLat = selectedCenter.latitude - userLatitude
      val dLng = selectedCenter.longitude - userLongitude
      // Scale coordinates to screen pixels
      val targetX = -(dLng * 1400.0f * zoomLevel).toFloat()
      val targetY = (dLat * 1400.0f * zoomLevel).toFloat()
      panOffsetX = targetX * 0.4f
      panOffsetY = targetY * 0.4f
    }
  }

  // Pre-calculate visual marker positions for hit testing
  var renderedMarkers by remember { mutableStateOf<List<Pair<GovernmentCenter, Offset>>>(emptyList()) }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .background(if (isDarkMode) Color(0xFF0F172A) else Color(0xFFF1F5F9))
  ) {
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
          detectTransformGestures { _, pan, zoom, _ ->
            zoomLevel = (zoomLevel * zoom).coerceIn(0.5f, 3.5f)
            panOffsetX += pan.x
            panOffsetY += pan.y
          }
        }
        .pointerInput(renderedMarkers) {
          detectTapGestures { tapOffset ->
            // Check if user tapped near any government center pin
            val hitRadius = 40.dp.toPx()
            val clicked = renderedMarkers.find { (_, pinOffset) ->
              val dx = tapOffset.x - pinOffset.x
              val dy = tapOffset.y - pinOffset.y
              sqrt((dx * dx + dy * dy).toDouble()) <= hitRadius
            }
            if (clicked != null) {
              onCenterSelected(clicked.first)
            }
          }
        }
    ) {
      val centerCanvas = Offset(size.width / 2f + panOffsetX, size.height / 2f + panOffsetY)
      val scale = 1400.0f * zoomLevel

      // 1. Draw stylized background road network / topography lines
      val gridColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFE2E8F0)
      val majorRoadColor = if (isDarkMode) Color(0xFF334155).copy(alpha = 0.6f) else Color(0xFFCBD5E1)
      val highwayColor = if (isDarkMode) Color(0xFF475569).copy(alpha = 0.5f) else Color(0xFF94A3B8).copy(alpha = 0.6f)

      // Circular Distance Rings (1 km, 3 km, 5 km, 10 km)
      val ringKmList = listOf(1.0, 3.0, 5.0, 8.0)
      for (km in ringKmList) {
        val ringRadiusPx = (km * 0.009 * scale).toFloat()
        drawCircle(
          color = if (isDarkMode) Color(0xFF00C853).copy(alpha = 0.12f) else Color(0xFF0288D1).copy(alpha = 0.15f),
          radius = ringRadiusPx,
          center = centerCanvas,
          style = Stroke(width = 1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f))
        )
      }

      // Compass Axis Lines
      drawLine(
        color = gridColor,
        start = Offset(centerCanvas.x, 0f),
        end = Offset(centerCanvas.x, size.height),
        strokeWidth = 1.dp.toPx()
      )
      drawLine(
        color = gridColor,
        start = Offset(0f, centerCanvas.y),
        end = Offset(size.width, centerCanvas.y),
        strokeWidth = 1.dp.toPx()
      )

      // Stylized Major Roads (King Fahd Rd, Northern Ring Rd mimic)
      drawLine(
        color = highwayColor,
        start = Offset(centerCanvas.x - size.width * 0.6f, centerCanvas.y + size.height * 0.4f),
        end = Offset(centerCanvas.x + size.width * 0.8f, centerCanvas.y - size.height * 0.5f),
        strokeWidth = 4.dp.toPx()
      )
      drawLine(
        color = majorRoadColor,
        start = Offset(centerCanvas.x - size.width * 0.5f, centerCanvas.y - size.height * 0.3f),
        end = Offset(centerCanvas.x + size.width * 0.6f, centerCanvas.y + size.height * 0.5f),
        strokeWidth = 3.dp.toPx()
      )

      // 2. Draw Route Dashed Line if a center is selected
      val markersList = mutableListOf<Pair<GovernmentCenter, Offset>>()

      centers.forEach { center ->
        val dx = ((center.longitude - userLongitude) * scale).toFloat()
        val dy = -((center.latitude - userLatitude) * scale).toFloat()
        val pinPos = Offset(centerCanvas.x + dx, centerCanvas.y + dy)
        markersList.add(Pair(center, pinPos))

        val isSelected = selectedCenter?.id == center.id

        if (isSelected) {
          // Glow and route line from User to Center
          val routePath = Path().apply {
            moveTo(centerCanvas.x, centerCanvas.y)
            // Curved or direct line
            val midX = (centerCanvas.x + pinPos.x) / 2f
            val midY = (centerCanvas.y + pinPos.y) / 2f - 30f
            quadraticTo(midX, midY, pinPos.x, pinPos.y)
          }

          drawPath(
            path = routePath,
            color = Color(0xFF00E676),
            style = Stroke(
              width = 3.5.dp.toPx(),
              pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f), 0f)
            )
          )

          // Distance Indicator on mid-point
          drawCircle(
            color = Color(0xFF00E676),
            radius = 5.dp.toPx(),
            center = Offset((centerCanvas.x + pinPos.x) / 2f, (centerCanvas.y + pinPos.y) / 2f - 15f)
          )
        }

        // Draw Government Center Pin Marker
        val pinColor = when {
          center.type.contains("Passport", ignoreCase = true) || center.type.contains("Jawazat", ignoreCase = true) -> Color(0xFF10B981) // Emerald Green
          center.type.contains("ZATCA", ignoreCase = true) || center.type.contains("Tax", ignoreCase = true) -> Color(0xFF6366F1) // Indigo
          center.type.contains("Commerce", ignoreCase = true) -> Color(0xFFF59E0B) // Amber
          center.type.contains("MISA", ignoreCase = true) -> Color(0xFF06B6D4) // Cyan
          else -> Color(0xFF8B5CF6) // Purple
        }

        // Selected Pin halo
        if (isSelected) {
          drawCircle(
            color = pinColor.copy(alpha = 0.35f),
            radius = 24.dp.toPx(),
            center = pinPos
          )
        }

        // Pin shadow & base circle
        drawCircle(
          color = Color.Black.copy(alpha = 0.35f),
          radius = 16.dp.toPx(),
          center = Offset(pinPos.x, pinPos.y + 3.dp.toPx())
        )
        drawCircle(
          color = pinColor,
          radius = if (isSelected) 17.dp.toPx() else 14.dp.toPx(),
          center = pinPos
        )
        drawCircle(
          color = Color.White,
          radius = if (isSelected) 7.dp.toPx() else 5.5.dp.toPx(),
          center = pinPos
        )
      }

      renderedMarkers = markersList

      // 3. Draw User's Current GPS Location (Blue pulsing radar marker)
      drawCircle(
        color = Color(0xFF2563EB).copy(alpha = pulseAlpha),
        radius = pulseRadius.dp.toPx() * (zoomLevel.coerceIn(0.8f, 1.4f)),
        center = centerCanvas
      )
      drawCircle(
        color = Color(0xFF3B82F6).copy(alpha = 0.3f),
        radius = 18.dp.toPx(),
        center = centerCanvas
      )
      drawCircle(
        color = Color(0xFF1D4ED8),
        radius = 9.dp.toPx(),
        center = centerCanvas
      )
      drawCircle(
        color = Color.White,
        radius = 4.dp.toPx(),
        center = centerCanvas
      )
    }

    // Floating Map HUD Controls (Top-Right: Recenter, Zoom In, Zoom Out)
    Column(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        shadowElevation = 4.dp,
        modifier = Modifier.size(42.dp)
      ) {
        IconButton(
          onClick = {
            panOffsetX = 0f
            panOffsetY = 0f
            zoomLevel = 1.0f
          }
        ) {
          Icon(
            imageVector = Icons.Default.MyLocation,
            contentDescription = "My GPS Location",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        shadowElevation = 4.dp
      ) {
        Column {
          IconButton(
            onClick = { zoomLevel = (zoomLevel + 0.3f).coerceAtMost(3.5f) },
            modifier = Modifier.size(42.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = "Zoom In", modifier = Modifier.size(20.dp))
          }
          Divider(modifier = Modifier.width(32.dp), thickness = 0.5.dp)
          IconButton(
            onClick = { zoomLevel = (zoomLevel - 0.3f).coerceAtLeast(0.5f) },
            modifier = Modifier.size(42.dp)
          ) {
            Icon(Icons.Default.Remove, contentDescription = "Zoom Out", modifier = Modifier.size(20.dp))
          }
        }
      }
    }

    // Top-Left Legend / GPS Status Badge
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
      shadowElevation = 3.dp,
      modifier = Modifier
        .align(Alignment.TopStart)
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFF22C55E))
        )
        Text(
          text = "GPS Live: ${"%.3f".format(userLatitude)}, ${"%.3f".format(userLongitude)}",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.SemiBold
        )
      }
    }

    // Bottom Selected Center Floating Detail Card
    if (selectedCenter != null) {
      Card(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = selectedCenter.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
              )
              if (selectedCenter.nameAr.isNotBlank()) {
                Text(
                  text = selectedCenter.nameAr,
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Text(
                text = selectedCenter.address,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFF10B981).copy(alpha = 0.15f)
            ) {
              Text(
                text = "${selectedCenter.distanceKm} km",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF059669),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = MaterialTheme.colorScheme.secondaryContainer
            ) {
              Text(
                text = selectedCenter.operatingHours,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = MaterialTheme.colorScheme.tertiaryContainer
            ) {
              Text(
                text = selectedCenter.type,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          // Action Buttons: Navigate with Google Maps + Call Center
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = {
                if (onNavigateToDirections != null) {
                  onNavigateToDirections(selectedCenter)
                } else {
                  // Direct Google Maps Intent
                  val gmmIntentUri = Uri.parse("geo:${selectedCenter.latitude},${selectedCenter.longitude}?q=${selectedCenter.latitude},${selectedCenter.longitude}(${Uri.encode(selectedCenter.name)})")
                  val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                    setPackage("com.google.android.apps.maps")
                  }
                  try {
                    context.startActivity(mapIntent)
                  } catch (e: Exception) {
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${selectedCenter.latitude},${selectedCenter.longitude}"))
                    context.startActivity(webIntent)
                  }
                }
              },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Directions", style = MaterialTheme.typography.labelMedium)
            }

            OutlinedButton(
              onClick = {
                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${selectedCenter.phone}"))
                try {
                  context.startActivity(callIntent)
                } catch (e: Exception) {
                  e.printStackTrace()
                }
              },
              modifier = Modifier.weight(0.7f),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Call", style = MaterialTheme.typography.labelMedium)
            }
          }
        }
      }
    }
  }
}
