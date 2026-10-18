package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck

class TwoDoApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    installAppCheck()
  }

  private fun installAppCheck() {
    val configured = try {
      FirebaseApp.initializeApp(this) != null || FirebaseApp.getApps(this).isNotEmpty()
    } catch (error: Exception) {
      Log.i(TAG, "Firebase is not configured.", error)
      false
    }
    if (!configured) {
      Log.i(TAG, "Firebase is not configured. Add app/google-services.json to enable Firebase AI Logic.")
      return
    }
    installAppCheckProvider(FirebaseAppCheck.getInstance())
  }

  private companion object {
    const val TAG = "TwoDoApplication"
  }
}
