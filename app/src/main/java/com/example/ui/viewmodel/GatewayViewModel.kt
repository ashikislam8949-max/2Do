package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.GovPortal
import com.example.data.GovPortalsRepository
import com.example.data.PersonalDocument
import com.example.data.PersonalDocumentsRepository
import com.example.data.SavedServiceEntity
import com.example.data.ServiceEntity
import com.example.data.ServiceRepository
import com.example.data.ServiceRequestEntity
import com.example.data.UserEntity
import com.example.ui.components.CategoryTab
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GatewayViewModel(application: Application) : AndroidViewModel(application) {
  private val database = AppDatabase.getDatabase(application)
  private val repository = ServiceRepository(database)

  val allServices: StateFlow<List<ServiceEntity>> =
    repository.allServices.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val featuredServices: StateFlow<List<ServiceEntity>> =
    repository.featuredServices.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val popularGovServices: StateFlow<List<ServiceEntity>> =
    repository.popularGovServices.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val serviceRequests: StateFlow<List<ServiceRequestEntity>> =
    repository.serviceRequests.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val savedServices: StateFlow<List<SavedServiceEntity>> =
    repository.savedServices.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Main Category Tab Selection (IT Services, E-Gov Portals, Personal Documents)
  private val _selectedMainCategory = MutableStateFlow(CategoryTab.GOV_PORTALS)
  val selectedMainCategory: StateFlow<CategoryTab> = _selectedMainCategory.asStateFlow()

  fun setSelectedMainCategory(tab: CategoryTab) {
    _selectedMainCategory.value = tab
  }

  // Personal Documents State
  private val _personalDocuments = MutableStateFlow(PersonalDocumentsRepository.getDefaultDocuments())
  val personalDocuments: StateFlow<List<PersonalDocument>> = _personalDocuments.asStateFlow()

  // Government Portals State
  private val _govPortals = MutableStateFlow(GovPortalsRepository.getDefaultPortals())
  val govPortals: StateFlow<List<GovPortal>> = _govPortals.asStateFlow()

  private val _currentUser = MutableStateFlow<UserEntity?>(null)
  val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

  private val _currentLanguage = MutableStateFlow("EN")
  val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

  private val _isDarkMode = MutableStateFlow(false)
  val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

  // Biometric Protection State for Sensitive Government Service Records
  private val _isBiometricUnlocked = MutableStateFlow(false)
  val isBiometricUnlocked: StateFlow<Boolean> = _isBiometricUnlocked.asStateFlow()

  fun unlockBiometric() {
    _isBiometricUnlocked.value = true
  }

  fun lockBiometric() {
    _isBiometricUnlocked.value = false
  }

  fun toggleLanguage() {
    _currentLanguage.value = if (_currentLanguage.value == "EN") "AR" else "EN"
  }

  fun toggleDarkMode() {
    _isDarkMode.value = !_isDarkMode.value
  }

  // UI filters
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow("All")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  // Filtered services
  val filteredServices: StateFlow<List<ServiceEntity>> =
    combine(allServices, _searchQuery, _selectedCategory) { services, query, cat ->
      services.filter { s ->
        val matchesQuery = query.isBlank() ||
            s.title.contains(query, ignoreCase = true) ||
            s.description.contains(query, ignoreCase = true) ||
            s.category.contains(query, ignoreCase = true) ||
            s.requirements.contains(query, ignoreCase = true)
        val matchesCat = when (cat) {
          "All" -> true
          "MISA & Investment" -> s.category == "MISA & Investment" || s.title.contains("MISA", ignoreCase = true)
          "Commercial Registration" -> s.category == "Commercial Registration (CR)" || s.title.contains("Commercial Registration", ignoreCase = true) || s.title.contains("CR", ignoreCase = true)
          "Licensing & CR" -> s.category == "Commercial Registration (CR)" || s.category == "Licensing & CR" || s.title.contains("CR", ignoreCase = true) || s.title.contains("License", ignoreCase = true)
          else -> s.category.equals(cat, ignoreCase = true)
        }
        matchesQuery && matchesCat
      }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  init {
    viewModelScope.launch {
      repository.initializeDefaultServices()
    }
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setSelectedCategory(category: String) {
    _selectedCategory.value = category
  }

  fun toggleSaved(service: ServiceEntity, isSaved: Boolean) {
    viewModelScope.launch {
      repository.toggleSavedService(service.id, service.title, service.category, service.fee, service.imageUrl, service.rating, isSaved)
    }
  }

  fun isSaved(serviceId: String): Flow<Boolean> {
    return repository.isSaved(serviceId)
  }

  fun submitServiceRequest(serviceTitle: String, companyName: String, crNumber: String, totalAmount: Double, paymentMethod: String, onComplete: (String) -> Unit) {
    viewModelScope.launch {
      val requestId = "REQ-${System.currentTimeMillis().toString().takeLast(6)}"
      val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
      val dateStr = dateFormat.format(Date())
      repository.submitRequest(requestId, dateStr, serviceTitle, companyName, crNumber, totalAmount, paymentMethod)
      onComplete(requestId)
    }
  }

  fun login(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
    viewModelScope.launch {
      val user = repository.loginUser(email, pass)
      if (user != null) {
        _currentUser.value = user
        onResult(true, "Login successful")
      } else {
        onResult(false, "Invalid email or password")
      }
    }
  }

  fun register(user: UserEntity, onResult: (Boolean, String) -> Unit) {
    viewModelScope.launch {
      val existing = repository.getUserById(user.userId)
      if (existing != null) {
        onResult(false, "User ID already exists")
      } else {
        repository.registerUser(user)
        _currentUser.value = user
        onResult(true, "Registration successful")
      }
    }
  }

  fun updateProfile(user: UserEntity) {
    viewModelScope.launch {
      repository.updateUserProfile(user)
      _currentUser.value = user
    }
  }

  fun updateProfileFromOcr(companyName: String, crNumber: String) {
    viewModelScope.launch {
      val current = _currentUser.value
      if (current != null) {
        val updated = current.copy(companyName = companyName, crNumber = crNumber)
        updateProfile(updated)
      } else {
        val defaultUser = UserEntity(
          userId = "u_ocr_1",
          fullName = "Ahmad Al-Mansoor",
          email = "ahmad@riyadhtech.sa",
          password = "password",
          companyName = companyName,
          crNumber = crNumber,
          phone = "+966 50 123 4567"
        )
        repository.registerUser(defaultUser)
        _currentUser.value = defaultUser
      }
    }
  }

  fun logout() {
    _currentUser.value = null
  }
}
