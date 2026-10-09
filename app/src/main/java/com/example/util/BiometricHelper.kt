package com.example.util

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricHelper {

  fun canAuthenticate(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
      BiometricManager.BIOMETRIC_SUCCESS -> true
      else -> false
    }
  }

  fun authenticate(
    activity: FragmentActivity,
    title: String = "Biometric Authentication",
    subtitle: String = "Verify your identity for the 2Do Tech Business Portal",
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(
      activity,
      executor,
      object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
          super.onAuthenticationSucceeded(result)
          onSuccess()
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
          super.onAuthenticationError(errorCode, errString)
          onError(errString.toString())
        }

        override fun onAuthenticationFailed() {
          super.onAuthenticationFailed()
          onError("Biometric authentication failed. Please try again.")
        }
      }
    )

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
      .setTitle(title)
      .setSubtitle(subtitle)
      .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
      .build()

    try {
      biometricPrompt.authenticate(promptInfo)
    } catch (e: Exception) {
      onError(e.localizedMessage ?: "Unknown biometric error")
    }
  }
}
