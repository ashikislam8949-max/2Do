package com.example

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.AnnouncementsRepository
import com.example.ui.components.CategoryDrawerSheet
import com.example.ui.components.GatewayBottomNavBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.GatewayViewModel
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
  private val viewModel: GatewayViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val navController = rememberNavController()
      val navBackStackEntry by navController.currentBackStackEntryAsState()
      val currentRoute = navBackStackEntry?.destination?.route ?: "home"

      val featuredServices by viewModel.featuredServices.collectAsState()
      val popularGovServices by viewModel.popularGovServices.collectAsState()
      val allServices by viewModel.allServices.collectAsState()
      val filteredServices by viewModel.filteredServices.collectAsState()
      val serviceRequests by viewModel.serviceRequests.collectAsState()
      val savedServices by viewModel.savedServices.collectAsState()
      val currentUser by viewModel.currentUser.collectAsState()

      val selectedMainCategory by viewModel.selectedMainCategory.collectAsState()
      val personalDocuments by viewModel.personalDocuments.collectAsState()
      val govPortals by viewModel.govPortals.collectAsState()

      val searchQuery by viewModel.searchQuery.collectAsState()
      val selectedCategory by viewModel.selectedCategory.collectAsState()
      val currentLanguage by viewModel.currentLanguage.collectAsState()
      val isDarkMode by viewModel.isDarkMode.collectAsState()
      val isBiometricUnlocked by viewModel.isBiometricUnlocked.collectAsState()
      val announcements = AnnouncementsRepository.announcements

      val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
      val coroutineScope = rememberCoroutineScope()

      val showBottomNav = currentRoute in listOf("home", "catalog", "requests", "saved", "profile")

      MyApplicationTheme(darkTheme = isDarkMode) {
        ModalNavigationDrawer(
          drawerState = drawerState,
          gesturesEnabled = showBottomNav,
          drawerContent = {
            CategoryDrawerSheet(
              selectedCategory = selectedMainCategory,
              onSelectCategory = { tab ->
                viewModel.setSelectedMainCategory(tab)
                coroutineScope.launch { drawerState.close() }
                if (currentRoute != "home") {
                  navController.navigate("home") {
                    popUpTo("home") { inclusive = false }
                  }
                }
              },
              onNavigateRoute = { route ->
                coroutineScope.launch { drawerState.close() }
                navController.navigate(route) { popUpTo("home") }
              },
              currentUser = currentUser,
              isArabic = currentLanguage == "AR",
              isDarkMode = isDarkMode,
              onToggleLanguage = { viewModel.toggleLanguage() },
              onToggleDarkMode = { viewModel.toggleDarkMode() },
              requestCount = serviceRequests.size,
              savedCount = savedServices.size
            )
          }
        ) {
          Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
              if (showBottomNav) {
                GatewayBottomNavBar(
                  currentRoute = currentRoute,
                  onNavigate = { route -> navController.navigate(route) { popUpTo("home") } },
                  requestCount = serviceRequests.size,
                  savedCount = savedServices.size
                )
              }
            }
          ) { innerPadding ->
          NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
          ) {
            composable("home") {
              HomeScreen(
                featuredServices = featuredServices,
                popularGovServices = popularGovServices,
                allServices = allServices,
                savedServices = savedServices,
                serviceRequests = serviceRequests,
                selectedMainCategory = selectedMainCategory,
                onSelectMainCategory = { viewModel.setSelectedMainCategory(it) },
                personalDocuments = personalDocuments,
                govPortals = govPortals,
                onOpenDrawer = { coroutineScope.launch { drawerState.open() } },
                onOcrScanned = { comp, cr -> viewModel.updateProfileFromOcr(comp, cr) },
                currentLanguage = currentLanguage,
                onToggleLanguage = { viewModel.toggleLanguage() },
                onServiceClick = { serviceId -> navController.navigate("detail/$serviceId") },
                onSearchClick = { navController.navigate("catalog") },
                onAiChatClick = { navController.navigate("chatbot") },
                onNavigateSaved = { navController.navigate("saved") },
                onNavigateRequests = { navController.navigate("requests") },
                onCategorySelected = { cat ->
                  viewModel.setSelectedCategory(cat)
                  navController.navigate("catalog")
                },
                onOpenBranchesMap = { navController.navigate("branches_map") }
              )
            }

            composable("branches_map") {
              GovBranchesMapScreen(
                onBackClick = { navController.popBackStack() },
                currentLanguage = currentLanguage,
                isDarkMode = isDarkMode
              )
            }

            composable("chatbot") {
              AiChatScreen(
                onBackClick = { navController.popBackStack() }
              )
            }

            composable("announcements") {
              AnnouncementsScreen(
                announcements = announcements,
                isArabic = currentLanguage == "AR",
                onBackClick = { navController.popBackStack() }
              )
            }

            composable("catalog") {
              CatalogScreen(
                services = filteredServices,
                searchQuery = searchQuery,
                selectedCategory = selectedCategory,
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                onCategoryChange = { viewModel.setSelectedCategory(it) },
                onServiceClick = { serviceId -> navController.navigate("detail/$serviceId") }
              )
            }

            composable("requests") {
              if (!isBiometricUnlocked) {
                BiometricAuthScreen(
                  currentUser = currentUser,
                  isArabic = currentLanguage == "AR",
                  onAuthenticationSuccess = { viewModel.unlockBiometric() },
                  onBackClick = { navController.popBackStack() }
                )
              } else {
                RequestsScreen(
                  requests = serviceRequests,
                  isArabic = currentLanguage == "AR",
                  onLockRecords = { viewModel.lockBiometric() }
                )
              }
            }

            composable("saved") {
              SavedServicesScreen(
                savedServices = savedServices,
                onServiceClick = { serviceId -> navController.navigate("detail/$serviceId") }
              )
            }

            composable("profile") {
              ProfileScreen(
                currentUser = currentUser,
                isDarkMode = isDarkMode,
                onToggleDarkMode = { viewModel.toggleDarkMode() },
                onOcrScanned = { comp, cr -> viewModel.updateProfileFromOcr(comp, cr) },
                onNavigateRequests = { navController.navigate("requests") },
                onNavigateSaved = { navController.navigate("saved") },
                onNavigateLogin = { navController.navigate("login") },
                onNavigateRegister = { navController.navigate("register") },
                onNavigateEditProfile = { navController.navigate("edit_profile") },
                onLogout = { viewModel.logout() }
              )
            }

            composable("login") {
              LoginScreen(
                onLoginSuccess = { navController.popBackStack() },
                onNavigateRegister = { navController.navigate("register") },
                onBackClick = { navController.popBackStack() },
                onLoginSubmit = { email, pass, cb -> viewModel.login(email, pass, cb) }
              )
            }

            composable("register") {
              RegisterScreen(
                onRegisterSuccess = { navController.popBackStack("profile", false) },
                onBackClick = { navController.popBackStack() },
                onRegisterSubmit = { user, cb -> viewModel.register(user, cb) }
              )
            }

            composable("edit_profile") {
              EditProfileScreen(
                currentUser = currentUser,
                onBackClick = { navController.popBackStack() },
                onSaveProfile = { updatedUser -> viewModel.updateProfile(updatedUser) }
              )
            }

            composable(
              route = "detail/{serviceId}",
              arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
            ) { backStackEntry ->
              val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
              val service = allServices.find { it.id == serviceId } ?: featuredServices.find { it.id == serviceId } ?: popularGovServices.find { it.id == serviceId }

              if (service != null) {
                val isSavedFlow = viewModel.isSaved(service.id)
                val isSaved by isSavedFlow.collectAsState(initial = false)

                ServiceDetailScreen(
                  service = service,
                  isSaved = isSaved,
                  onBackClick = { navController.popBackStack() },
                  onToggleSave = { viewModel.toggleSaved(service, isSaved) },
                  onRequestService = { navController.navigate("apply/${service.id}") }
                )
              }
            }

            composable(
              route = "apply/{serviceId}",
              arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
            ) { backStackEntry ->
              val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
              val service = allServices.find { it.id == serviceId } ?: featuredServices.find { it.id == serviceId } ?: popularGovServices.find { it.id == serviceId }

              if (service != null) {
                ApplicationFormScreen(
                  service = service,
                  onBackClick = { navController.popBackStack() },
                  onSubmitRequest = { company, cr, total, payment ->
                    viewModel.submitServiceRequest(service.title, company, cr, total, payment) { requestId ->
                      navController.navigate("success/$requestId") {
                        popUpTo("home")
                      }
                    }
                  }
                )
              }
            }

            composable(
              route = "success/{requestId}",
              arguments = listOf(navArgument("requestId") { type = NavType.StringType })
            ) { backStackEntry ->
              val requestId = backStackEntry.arguments?.getString("requestId") ?: "REQ-0000"
              RequestSuccessScreen(
                requestId = requestId,
                onViewRequests = { navController.navigate("requests") { popUpTo("home") } },
                onGoHome = { navController.navigate("home") { popUpTo("home") { inclusive = true } } }
              )
            }
          }
        }
      }
    }
  }
}
}
