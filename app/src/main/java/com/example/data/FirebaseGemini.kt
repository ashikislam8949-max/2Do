package com.example.data

import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig

/**
 * Gemini calls go through Firebase AI Logic, which holds the model credential
 * on Google's side. The app proves itself with App Check instead of a client API key.
 */
internal object FirebaseGemini {
  const val MODEL_NAME = "gemini-3.5-flash"

  fun isConfigured(): Boolean = try {
    FirebaseApp.getInstance()
    true
  } catch (error: Exception) {
    false
  }

  fun model(jsonResponse: Boolean = false, systemInstruction: String? = null) =
    Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
      modelName = MODEL_NAME,
      generationConfig = if (jsonResponse) {
        generationConfig { responseMimeType = "application/json" }
      } else {
        null
      },
      systemInstruction = systemInstruction?.let { instruction ->
        content { text(instruction) }
      }
    )
}
