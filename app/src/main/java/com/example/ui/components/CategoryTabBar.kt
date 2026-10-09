package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class CategoryTab(
  val titleEn: String,
  val titleAr: String,
  val shortLabelEn: String,
  val shortLabelAr: String,
  val icon: ImageVector,
  val testTag: String
) {
  IT_SERVICES(
    titleEn = "IT Services",
    titleAr = "خدمات تقنية المعلومات",
    shortLabelEn = "IT Services",
    shortLabelAr = "خدمات التقنية",
    icon = Icons.Default.Dns,
    testTag = "tab_it_services"
  ),
  GOV_PORTALS(
    titleEn = "E-Gov Portals",
    titleAr = "البوابات الحكومية",
    shortLabelEn = "Gov Portals",
    shortLabelAr = "البوابات",
    icon = Icons.Default.AccountBalance,
    testTag = "tab_gov_portals"
  ),
  PERSONAL_DOCUMENTS(
    titleEn = "Personal Documents",
    titleAr = "المستندات الشخصية",
    shortLabelEn = "My Documents",
    shortLabelAr = "المستندات",
    icon = Icons.Default.Badge,
    testTag = "tab_personal_documents"
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTabBar(
  selectedTab: CategoryTab,
  onTabSelected: (CategoryTab) -> Unit,
  isArabic: Boolean = false,
  itServicesCount: Int = 16,
  govPortalsCount: Int = 9,
  documentsCount: Int = 7,
  modifier: Modifier = Modifier
) {
  val tabs = CategoryTab.values()
  val selectedIndex = selectedTab.ordinal

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    shadowElevation = 3.dp
  ) {
    TabRow(
      selectedTabIndex = selectedIndex,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = MaterialTheme.colorScheme.primary,
      indicator = { tabPositions ->
        if (selectedIndex < tabPositions.size) {
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
            color = MaterialTheme.colorScheme.primary,
            height = 3.5.dp
          )
        }
      },
      divider = {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
      }
    ) {
      tabs.forEachIndexed { index, tab ->
        val isSelected = selectedTab == tab
        val tabColor by animateColorAsState(
          targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
          animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
          label = "tab_color"
        )

        val count = when (tab) {
          CategoryTab.IT_SERVICES -> itServicesCount
          CategoryTab.GOV_PORTALS -> govPortalsCount
          CategoryTab.PERSONAL_DOCUMENTS -> documentsCount
        }

        Tab(
          selected = isSelected,
          onClick = { onTabSelected(tab) },
          modifier = Modifier
            .testTag(tab.testTag)
            .padding(vertical = 8.dp),
          selectedContentColor = MaterialTheme.colorScheme.primary,
          unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = tab.icon,
                contentDescription = if (isArabic) tab.titleAr else tab.titleEn,
                tint = tabColor,
                modifier = Modifier.size(20.dp)
              )

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
              ) {
                Text(
                  text = count.toString(),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Text(
              text = if (isArabic) tab.shortLabelAr else tab.shortLabelEn,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = tabColor,
              maxLines = 1
            )
          }
        }
      }
    }
  }
}
