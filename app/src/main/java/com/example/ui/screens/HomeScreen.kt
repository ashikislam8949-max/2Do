package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.TwoDoTechLogo
import com.example.ui.components.CategoryTab
import com.example.ui.components.CategoryTabBar
import com.example.ui.components.UsageAnalyticsSection
import com.example.ui.screens.categories.GovPortalsCategoryView
import com.example.ui.screens.categories.ItServicesCategoryView
import com.example.ui.screens.categories.PersonalDocumentsCategoryView
import com.example.util.NotificationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  featuredServices: List<ServiceEntity>,
  popularGovServices: List<ServiceEntity>,
  allServices: List<ServiceEntity> = emptyList(),
  savedServices: List<SavedServiceEntity> = emptyList(),
  serviceRequests: List<ServiceRequestEntity> = emptyList(),
  selectedMainCategory: CategoryTab = CategoryTab.IT_SERVICES,
  onSelectMainCategory: (CategoryTab) -> Unit = {},
  personalDocuments: List<PersonalDocument> = emptyList(),
  govPortals: List<GovPortal> = emptyList(),
  onOpenDrawer: () -> Unit = {},
  onOcrScanned: (String, String) -> Unit = { _, _ -> },
  currentLanguage: String = "EN",
  onToggleLanguage: () -> Unit = {},
  onServiceClick: (String) -> Unit,
  onSearchClick: () -> Unit,
  onAiChatClick: () -> Unit,
  onNavigateSaved: () -> Unit,
  onNavigateRequests: () -> Unit,
  onCategorySelected: (String) -> Unit = {},
  onOpenBranchesMap: () -> Unit = {}
) {
  val isArabic = currentLanguage == "AR"
  val context = LocalContext.current

  var notificationStatusMsg by remember { mutableStateOf<String?>(null) }
  val notificationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { granted ->
    if (granted) {
      NotificationHelper.sendPushNotification(
        context,
        "🔔 2Do Tech Gov & Iqama Alerts Enabled",
        "You will now receive push notifications for Iqama expiry, Muqeem status updates, and ZATCA announcements."
      )
      notificationStatusMsg = "Push notifications enabled & test alert sent!"
    } else {
      notificationStatusMsg = "Notification permission denied."
    }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // 1. Top Hero Banner with Navigation Drawer Toggle
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(
                Color(0xFF004B29),
                Color(0xFF006C35)
              )
            )
          )
          .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Hamburger Menu Icon to open Category Navigation Drawer
            IconButton(
              onClick = onOpenDrawer,
              modifier = Modifier.testTag("open_category_drawer_button")
            ) {
              Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = if (isArabic) "فتح قائمة الفئات" else "Open Categories Drawer",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
              )
            }

            TwoDoTechLogo(width = 110, height = 60)
          }

          // Top Action Buttons (Language Toggle, AI Chat, Search)
          Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Language Toggle
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)
            ) {
              TextButton(
                onClick = onToggleLanguage,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Language,
                  contentDescription = "Language",
                  tint = Color.White,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = if (isArabic) "EN" else "عربي",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  style = MaterialTheme.typography.labelSmall
                )
              }
            }

            // AI Chatbot Button
            Surface(
              shape = CircleShape,
              color = MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)
            ) {
              IconButton(onClick = onAiChatClick) {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = "Gemini AI Chat",
                  tint = Color.White,
                  modifier = Modifier.size(20.dp)
                )
              }
            }

            // Search Button
            Surface(
              shape = CircleShape,
              color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
            ) {
              IconButton(onClick = onSearchClick) {
                Icon(
                  imageVector = Icons.Default.Search,
                  contentDescription = "Search",
                  tint = Color.White,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = if (isArabic) "خدماتك الحكومية، ببساطة." else "Saudi government services, made simple.",
          style = MaterialTheme.typography.titleLarge,
          color = Color.White,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = if (isArabic) "الإقامة والتأشيرات والجوازات وخدمات الأعمال في مكان واحد." else "Residency, visas, Jawazat, and business services in one place.",
          style = MaterialTheme.typography.bodySmall,
          color = Color.White.copy(alpha = 0.82f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar Bar in Hero
        OutlinedCard(
          modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSearchClick),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = if (isArabic) "ابحث عن الإقامة أو التأشيرات أو الجوازات..." else "Search Iqama, visas, Jawazat, and more...",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // 2. Category Tab Bar (Switch between IT Services, E-Gov Portals, Personal Documents)
    item {
      CategoryTabBar(
        selectedTab = selectedMainCategory,
        onTabSelected = onSelectMainCategory,
        isArabic = isArabic,
        itServicesCount = if (allServices.isNotEmpty()) allServices.size else 37,
        govPortalsCount = if (govPortals.isNotEmpty()) govPortals.size else 9,
        documentsCount = if (personalDocuments.isNotEmpty()) personalDocuments.size else 8
      )
    }

    // 3. Active Category Content Body
    item {
      Spacer(modifier = Modifier.height(12.dp))
      when (selectedMainCategory) {
        CategoryTab.IT_SERVICES -> {
          ItServicesCategoryView(
            allServices = if (allServices.isNotEmpty()) allServices else featuredServices + popularGovServices,
            isArabic = isArabic,
            onServiceClick = onServiceClick,
            onAiChatClick = onAiChatClick
          )
        }
        CategoryTab.GOV_PORTALS -> {
          GovPortalsCategoryView(
            portals = govPortals,
            services = allServices,
            isArabic = isArabic,
            serviceRequests = serviceRequests,
            onOpenBranchesMap = onOpenBranchesMap,
            onServiceClick = onServiceClick,
            onNavigateRequests = onNavigateRequests,
            onPortalServiceClick = { portal ->
              val match = allServices.find { it.title.contains(portal.nameEn.take(6), ignoreCase = true) }
                ?: popularGovServices.firstOrNull()
              if (match != null) {
                onServiceClick(match.id)
              } else {
                onSearchClick()
              }
            }
          )
        }
        CategoryTab.PERSONAL_DOCUMENTS -> {
          PersonalDocumentsCategoryView(
            documents = personalDocuments,
            isArabic = isArabic,
            onOcrScanned = onOcrScanned
          )
        }
      }
    }

    // 4. Recent Activity & Service Requests Widget (If any requests exist)
    if (serviceRequests.isNotEmpty()) {
      item {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Text(
              text = if (isArabic) "⏱️ النشاط الأخير (الطلبات)" else "⏱️ Recent Service Activity",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
          TextButton(onClick = onNavigateRequests) {
            Text(if (isArabic) "عرض الكل (${serviceRequests.size})" else "View All (${serviceRequests.size})")
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          serviceRequests.take(2).forEach { req ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onNavigateRequests),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp),
                  modifier = Modifier.weight(1f)
                ) {
                  Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(34.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Icon(Icons.Default.Assignment, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }
                  }
                  Column {
                    Text(text = req.serviceTitle, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(text = "${req.requestId} • ${req.date}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                }
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = MaterialTheme.colorScheme.primaryContainer
                ) {
                  Text(
                    text = req.status,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
        }
      }
    }

    // 5. My Favorites / Saved Services Horizontal List
    if (savedServices.isNotEmpty()) {
      item {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Text(
              text = if (isArabic) "⭐ خدماتي المفضلة" else "⭐ Bookmarked Services",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
          TextButton(onClick = onNavigateSaved) {
            Text(if (isArabic) "عرض الكل (${savedServices.size})" else "See All (${savedServices.size})")
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(
          modifier = Modifier.fillMaxWidth(),
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(savedServices) { saved ->
            OutlinedCard(
              onClick = { onServiceClick(saved.serviceId) },
              modifier = Modifier
                .width(200.dp)
                .height(105.dp),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxSize()
                  .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = saved.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 2)
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(text = saved.category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                  Text(text = "${saved.fee} SAR", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
              }
            }
          }
        }
      }
    }

    // 6. Usage Analytics Section (Vico Library Visualization)
    item {
      Spacer(modifier = Modifier.height(16.dp))
      UsageAnalyticsSection(
        serviceRequests = serviceRequests,
        isArabic = isArabic,
        onNavigateRequests = onNavigateRequests,
        modifier = Modifier.padding(horizontal = 16.dp)
      )
    }

    // 7. Push Alerts Center Action
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
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
                Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
              }
            }
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = if (isArabic) "تنبيهات الإقامة والجهات الحكومية" else "Iqama & Gov Expiry Alerts",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = if (isArabic) "إشعارات دورية قبل انتهاء الوثائق والتحديثات" else "Automated notifications before document expiry & regulatory updates",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Button(
            onClick = {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
              } else {
                NotificationHelper.sendPushNotification(
                  context,
                  "🔔 2Do Tech Gov & Iqama Alerts",
                  "Test Alert: All your documents are verified and compliant."
                )
                notificationStatusMsg = "Test alert sent!"
              }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isArabic) "تفعيل إشعارات التنبيه" else "Enable & Test Alerts")
          }

          if (notificationStatusMsg != null) {
            Text(
              text = notificationStatusMsg!!,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.primary
            )
          }
        }
      }
    }

    // 7. Portal Trust Badges
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        PortalBadge(icon = Icons.Default.VerifiedUser, title = if (isArabic) "بوابة معتمدة" else "Official Portal")
        PortalBadge(icon = Icons.Default.Gavel, title = if (isArabic) "متوافق حكومياً" else "Gov Compliant")
        PortalBadge(icon = Icons.Default.Security, title = if (isArabic) "شهادة NCA" else "NCA Certified")
      }
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun PortalBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.width(100.dp)
  ) {
    Surface(
      shape = CircleShape,
      color = MaterialTheme.colorScheme.primaryContainer,
      modifier = Modifier.size(44.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(22.dp)
        )
      }
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = title,
      style = MaterialTheme.typography.labelSmall,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center,
      maxLines = 1
    )
  }
}
