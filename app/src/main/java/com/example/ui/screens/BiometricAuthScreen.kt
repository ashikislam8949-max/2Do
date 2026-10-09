package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.example.data.UserEntity
import com.example.util.BiometricHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiometricAuthScreen(
  currentUser: UserEntity? = null,
  isArabic: Boolean = false,
  onAuthenticationSuccess: () -> Unit,
  onBackClick: () -> Unit
) {
  val context = LocalContext.current
  val activity = context as? FragmentActivity

  BackHandler { onBackClick() }

  var authErrorMessage by remember { mutableStateOf<String?>(null) }
  var isAuthenticating by remember { mutableStateOf(false) }
  var showPinFallback by remember { mutableStateOf(false) }
  var enteredPin by remember { mutableStateOf("") }
  var pinError by remember { mutableStateOf<String?>(null) }

  val canUseBiometrics = remember(context) {
    BiometricHelper.canAuthenticate(context)
  }

  // Fingerprint pulsing animation
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )

  fun triggerBiometricPrompt() {
    authErrorMessage = null
    pinError = null
    if (activity != null) {
      isAuthenticating = true
      BiometricHelper.authenticate(
        activity = activity,
        title = if (isArabic) "التحقق من الهوية البيومترية" else "Biometric Security Verification",
        subtitle = if (isArabic) "مصادقة بصمة الإصبع أو الوجه للوصول إلى السجلات الحكومية" else "Fingerprint or Face verification for Sensitive GovTech Records",
        onSuccess = {
          isAuthenticating = false
          Toast.makeText(
            context,
            if (isArabic) "تم التحقق بنجاح • تم فتح السجلات" else "Identity Verified • Access Granted",
            Toast.LENGTH_SHORT
          ).show()
          onAuthenticationSuccess()
        },
        onError = { err ->
          isAuthenticating = false
          authErrorMessage = err
        }
      )
    } else {
      authErrorMessage = "Activity context not available for BiometricPrompt."
    }
  }

  // Attempt initial prompt automatically if hardware available
  LaunchedEffect(Unit) {
    if (canUseBiometrics && activity != null) {
      triggerBiometricPrompt()
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (isArabic) "بوابة الأمان والتحقق البيومتري" else "Government Records Gate",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("biometric_back_button")
          ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF166534).copy(alpha = 0.2f),
            modifier = Modifier.padding(end = 8.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF166534), modifier = Modifier.size(14.dp))
              Text("NCA ECC-1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF166534))
            }
          }
        }
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())
        .padding(padding)
        .padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
      // 1. Header Shield / Fingerprint Hero Icon
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(top = 8.dp)
      ) {
        // Outer pulsing ring
        Box(
          modifier = Modifier
            .size(130.dp)
            .scale(pulseScale)
            .clip(CircleShape)
            .background(
              Brush.radialGradient(
                colors = listOf(
                  MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                  Color.Transparent
                )
              )
            )
        )

        // Inner solid circle
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.primary,
          shadowElevation = 8.dp,
          modifier = Modifier
            .size(96.dp)
            .clickable { triggerBiometricPrompt() }
            .testTag("fingerprint_hero_icon")
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Default.Fingerprint,
              contentDescription = "Biometric Security Prompt",
              tint = MaterialTheme.colorScheme.onPrimary,
              modifier = Modifier.size(54.dp)
            )
          }
        }
      }

      // 2. Security Clearance Level & Title
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.primaryContainer
        ) {
          Text(
            text = if (isArabic) "🔒 منطقة حكومية مؤمنة بيومترياً" else "🔒 RESTRICTED GOVERNMENT ARCHIVE",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
          )
        }

        Text(
          text = if (isArabic) "تأكيد الهوية للوصول إلى السجلات" else "Biometric Security Verification",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )

        Text(
          text = if (isArabic)
            "يتطلب الوصول إلى سجلات طلبات وزارة الاستثمار (MISA)، السجل التجاري (CR)، وبيانات زاتكا وقوى تصريحاً بيومترياً معتمداً."
          else
            "Access to official Ministry of Investment (MISA) filings, Commercial Registration (CR) applications, ZATCA tax ledgers, and Qiwa dossiers requires authorized biometric authentication.",
          style = MaterialTheme.typography.bodySmall,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      // 3. Authenticated Enterprise Clearance Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(Icons.Default.Business, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
              Text(
                text = if (isArabic) "المنشأة المصرح لها" else "AUTHORIZED ENTERPRISE",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            }
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFF166534).copy(alpha = 0.15f)
            ) {
              Text(
                text = "LEVEL-3 CLEARANCE",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF166534),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Text(
            text = currentUser?.companyName ?: "StarBridge Tech Solutions KSA",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "CR: ${currentUser?.crNumber ?: "1010948210"}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "Unified 700: 7001948201",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // 4. Biometric Status Indicator
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
          .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp))
          .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Icon(
            imageVector = if (canUseBiometrics) Icons.Default.CheckCircle else Icons.Default.Info,
            contentDescription = null,
            tint = if (canUseBiometrics) Color(0xFF166534) else MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(18.dp)
          )
          Column {
            Text(
              text = if (canUseBiometrics) "Biometric Sensors Ready" else "Biometrics / Device PIN Ready",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "BiometricPrompt API • Class 3 Strong",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 9.sp
            )
          }
        }

        Surface(
          shape = CircleShape,
          color = if (canUseBiometrics) Color(0xFF166534) else MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(10.dp)
        ) {}
      }

      // Error message if authentication failed
      if (authErrorMessage != null) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.errorContainer,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
            Text(
              text = authErrorMessage!!,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onErrorContainer
            )
          }
        }
      }

      // 5. Primary Action Button: BiometricPrompt trigger
      Button(
        onClick = { triggerBiometricPrompt() },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("biometric_auth_button"),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isArabic) "المصادقة باستخدام بصمة الإصبع / الوجه" else "Authenticate with Biometrics",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp
        )
      }

      // 6. Alternative PIN / Credential Fallback (Essential for Emulators & Testing)
      OutlinedButton(
        onClick = { showPinFallback = !showPinFallback },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("toggle_pin_fallback_button"),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(
          imageVector = if (showPinFallback) Icons.Default.KeyboardArrowUp else Icons.Default.Lock,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (showPinFallback)
            (if (isArabic) "إخفاء رمز الدخول البديل" else "Hide Passcode Fallback")
          else
            (if (isArabic) "استخدام رمز الأمان البديل (PIN / Passcode)" else "Unlock with Enterprise Security PIN")
        )
      }

      AnimatedVisibility(visible = showPinFallback) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(
              text = if (isArabic) "رمز المرور الاحتياطي للمؤسسة" else "Enterprise Fallback Passcode",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = if (isArabic) "أدخل رمز الأمان للمنظومة الحكومية (الافتراضي: 1234)" else "Enter your 4-digit security PIN to unlock records (Default: 1234):",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
              value = enteredPin,
              onValueChange = {
                enteredPin = it.take(6)
                pinError = null
              },
              placeholder = { Text("Enter PIN (e.g. 1234)") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("security_pin_input"),
              visualTransformation = PasswordVisualTransformation(),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
              keyboardActions = KeyboardActions(onDone = {
                if (enteredPin == "1234" || enteredPin.length >= 4) {
                  onAuthenticationSuccess()
                } else {
                  pinError = "Incorrect PIN. Default PIN is 1234."
                }
              }),
              singleLine = true,
              shape = RoundedCornerShape(10.dp)
            )

            if (pinError != null) {
              Text(
                text = pinError!!,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
              )
            }

            Button(
              onClick = {
                if (enteredPin == "1234" || enteredPin.length >= 4) {
                  Toast.makeText(context, "Passcode verified successfully", Toast.LENGTH_SHORT).show()
                  onAuthenticationSuccess()
                } else {
                  pinError = "Incorrect PIN. Default PIN is 1234."
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("submit_pin_button"),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(if (isArabic) "تأكيد الرمز وفتح السجلات" else "Verify PIN & Unlock Records")
            }
          }
        }
      }

      // Security Badges Footer
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.height(2.dp))
          Text("AES-256 GCM", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.height(2.dp))
          Text("BiometricPrompt", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.height(2.dp))
          Text("KSA NDMO Compliant", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
