package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

sealed class GatewayScreen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
  object Home : GatewayScreen("home", "Gateway", Icons.Default.Home)
  object Catalog : GatewayScreen("catalog", "Services", Icons.Default.Search)
  object Requests : GatewayScreen("requests", "Requests", Icons.Default.Assignment)
  object Saved : GatewayScreen("saved", "Saved", Icons.Default.Bookmark)
  object Profile : GatewayScreen("profile", "Enterprise", Icons.Default.Business)
}

@Composable
fun GatewayBottomNavBar(currentRoute: String, onNavigate: (String) -> Unit, requestCount: Int, savedCount: Int) {
  val items = listOf(GatewayScreen.Home, GatewayScreen.Catalog, GatewayScreen.Requests, GatewayScreen.Saved, GatewayScreen.Profile)

  NavigationBar {
    items.forEach { screen ->
      val selected = currentRoute == screen.route
      NavigationBarItem(
        icon = {
          if (screen == GatewayScreen.Requests && requestCount > 0) {
            BadgedBox(badge = { Badge { Text(requestCount.toString()) } }) {
              Icon(screen.icon, contentDescription = screen.title)
            }
          } else if (screen == GatewayScreen.Saved && savedCount > 0) {
            BadgedBox(badge = { Badge { Text(savedCount.toString()) } }) {
              Icon(screen.icon, contentDescription = screen.title)
            }
          } else {
            Icon(screen.icon, contentDescription = screen.title)
          }
        },
        label = { Text(screen.title) },
        selected = selected,
        onClick = {
          if (currentRoute != screen.route) {
            onNavigate(screen.route)
          }
        }
      )
    }
  }
}
