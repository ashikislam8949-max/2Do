package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity

@Composable
fun CategoryDrawerSheet(
  selectedCategory: CategoryTab,
  onSelectCategory: (CategoryTab) -> Unit,
  onNavigateRoute: (String) -> Unit,
  currentUser: UserEntity?,
  isArabic: Boolean = false,
  isDarkMode: Boolean = false,
  onToggleLanguage: () -> Unit = {},
  onToggleDarkMode: () -> Unit = {},
  requestCount: Int = 0,
  savedCount: Int = 0,
  modifier: Modifier = Modifier
) {
  val headerGradient = Brush.verticalGradient(
    colors = listOf(
      Color(0xFF0A5C36), // Deep Saudi Green
      Color(0xFF147A47),
      Color(0xFF063B22)
    )
  )

  ModalDrawerSheet(
    modifier = modifier
      .width(320.dp)
      .fillMaxHeight(),
    drawerContainerColor = MaterialTheme.colorScheme.surface
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
      // Top Drawer Header
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(headerGradient)
          .padding(20.dp)
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            TwoDoTechLogo(width = 130, height = 65)

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFFFFD700).copy(alpha = 0.2f)
            ) {
              Text(
                text = "KSA 🇸🇦",
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Text(
            text = if (isArabic) "الخدمات الحكومية والإقامة والأعمال" else "Government, residency & business services",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )

          // User info card inside header
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White.copy(alpha = 0.15f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(34.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF0A5C36),
                    modifier = Modifier.size(20.dp)
                  )
                }
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = currentUser?.fullName ?: if (isArabic) "أحمد المنصور" else "Muhammad Tariq Khan",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  maxLines = 1
                )
                Text(
                  text = currentUser?.companyName ?: "Al-Madinah Tech • CR-1010948210",
                  color = Color.White.copy(alpha = 0.8f),
                  fontSize = 10.sp,
                  maxLines = 1
                )
              }

              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Section 1: Core Category Switcher (The 3 Main Pillars)
      Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Text(
          text = if (isArabic) "الأقسام الرئيسية للبوابة" else "MAIN SERVICE PILLARS",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary,
          letterSpacing = 1.sp,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )

        // Pillar 1: IT Services
        CategoryDrawerItem(
          title = if (isArabic) "💻 خدمات التقنية والاستثمار MISA والسجل" else "💻 IT, MISA & CR Services",
          subtitle = if (isArabic) "وزارة الاستثمار، السجلات التجارية، السحابة والأمن" else "MISA, Commercial Registration, Cloud & ERP",
          badge = "37+ Services",
          isSelected = selectedCategory == CategoryTab.IT_SERVICES,
          testTag = "drawer_item_it_services",
          onClick = { onSelectCategory(CategoryTab.IT_SERVICES) }
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Pillar 2: E-Government Portals
        CategoryDrawerItem(
          title = if (isArabic) "🏛️ البوابات الحكومية الرقمية" else "🏛️ E-Government Portals",
          subtitle = if (isArabic) "أبشر، مقيم، زاتكا، قوى، بلدي، الاستثمار" else "Absher, Muqeem, ZATCA, Qiwa, MISA",
          badge = "9 Portals",
          isSelected = selectedCategory == CategoryTab.GOV_PORTALS,
          testTag = "drawer_item_gov_portals",
          onClick = { onSelectCategory(CategoryTab.GOV_PORTALS) }
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Pillar 3: Personal Documents
        CategoryDrawerItem(
          title = if (isArabic) "🪪 المستندات المؤسسية والشخصية" else "🪪 Corporate & Personal Vault",
          subtitle = if (isArabic) "رخصة الاستثمار، السجل التجاري، الإقامة، الجواز" else "MISA License, CR, Iqama, Passport",
          badge = "8 Documents",
          isSelected = selectedCategory == CategoryTab.PERSONAL_DOCUMENTS,
          testTag = "drawer_item_personal_documents",
          onClick = { onSelectCategory(CategoryTab.PERSONAL_DOCUMENTS) }
        )
      }

      Spacer(modifier = Modifier.height(12.dp))
      HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
      )
      Spacer(modifier = Modifier.height(12.dp))

      // Section 2: Quick Features & Navigation
      Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Text(
          text = if (isArabic) "روابط سريعة" else "QUICK ACCESS",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          letterSpacing = 1.sp,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )

        NavigationDrawerItem(
          label = { Text(if (isArabic) "الطلبات والمتابعة" else "Service Requests") },
          icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
          badge = {
            if (requestCount > 0) {
              Badge { Text(requestCount.toString()) }
            }
          },
          selected = false,
          onClick = { onNavigateRoute("requests") },
          modifier = Modifier.testTag("drawer_nav_requests")
        )

        NavigationDrawerItem(
          label = { Text(if (isArabic) "الخدمات المحفوظة" else "Saved Services") },
          icon = { Icon(Icons.Default.Bookmark, contentDescription = null) },
          badge = {
            if (savedCount > 0) {
              Badge { Text(savedCount.toString()) }
            }
          },
          selected = false,
          onClick = { onNavigateRoute("saved") },
          modifier = Modifier.testTag("drawer_nav_saved")
        )

        NavigationDrawerItem(
          label = { Text(if (isArabic) "خريطة فروع الجوازات والمراكز" else "Gov Centers & Passport Map") },
          icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
          selected = false,
          onClick = { onNavigateRoute("branches_map") },
          modifier = Modifier.testTag("drawer_nav_map")
        )

        NavigationDrawerItem(
          label = { Text(if (isArabic) "مساعد الذكاء الاصطناعي (Gemini)" else "Ask GovTech AI Assistant") },
          icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
          selected = false,
          onClick = { onNavigateRoute("chatbot") },
          modifier = Modifier.testTag("drawer_nav_chatbot")
        )

        NavigationDrawerItem(
          label = { Text(if (isArabic) "الملف المؤسسي والوثائق" else "Enterprise Profile & Vault") },
          icon = { Icon(Icons.Default.Business, contentDescription = null) },
          selected = false,
          onClick = { onNavigateRoute("profile") },
          modifier = Modifier.testTag("drawer_nav_profile")
        )
      }

      Spacer(modifier = Modifier.height(12.dp))
      HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
      )
      Spacer(modifier = Modifier.height(8.dp))

      // Section 3: App Settings & Toggles inside Drawer
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Language Toggle
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggleLanguage)
            .padding(vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Text(
              text = if (isArabic) "اللغة / Language" else "Language (العربية / English)",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.Medium
            )
          }
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer
          ) {
            Text(
              text = if (isArabic) "عربي 🇸🇦" else "English 🇬🇧",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onPrimaryContainer,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }

        // Dark Mode Toggle
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(Icons.Default.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Text(
              text = if (isArabic) "المظهر الداكن" else "Dark Theme",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.Medium
            )
          }
          Switch(
            checked = isDarkMode,
            onCheckedChange = { onToggleDarkMode() },
            modifier = Modifier.height(28.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Footer: Trust & Version
      Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(
          modifier = Modifier.padding(10.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = "🇸🇦 Saudi business services",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0A5C36)
          )
          Text(
            text = "NCA ECC-1:2018 Cybersecurity Certified • Muqeem & ZATCA Integrated",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 9.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun CategoryDrawerItem(
  title: String,
  subtitle: String,
  badge: String,
  isSelected: Boolean,
  testTag: String,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
    modifier = Modifier
      .fillMaxWidth()
      .testTag(testTag)
      .clip(RoundedCornerShape(12.dp))
      .clickable(onClick = onClick)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
          color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.labelSmall,
          color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 10.sp,
          maxLines = 1
        )
      }

      Spacer(modifier = Modifier.width(6.dp))

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
      ) {
        Text(
          text = badge,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }
  }
}
