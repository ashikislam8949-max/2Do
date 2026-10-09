package com.example.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object AiDocumentClassifier {
  private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .build()

  suspend fun classifyDocument(documentTitle: String, documentContent: String): Map<String, String> = withContext(Dispatchers.IO) {
    val apiKey = try {
      val field = com.example.BuildConfig::class.java.getField("GEMINI_API_KEY")
      field.get(null) as? String ?: ""
    } catch (e: Exception) {
      ""
    }

    if (apiKey.isEmpty()) {
      return@withContext mapOf(
        "category" to "Other",
        "tags" to "General, Unclassified",
        "summary" to "API key not configured."
      )
    }

    val prompt = """
      Analyze the following document title and text content, and classify it into one of these strict categories:
      - Passports
      - Contracts
      - Medical
      - Tax & ZATCA
      - Licensing & CR
      - Other

      Document Title: $documentTitle
      Document Content/Description: $documentContent

      Return ONLY a JSON object with keys: "category", "tags" (comma-separated list), and "summary" (one sentence summary).
    """.trimIndent()

    val jsonBody = JSONObject().apply {
      put("contents", JSONArray().apply {
        put(JSONObject().apply {
          put("parts", JSONArray().apply {
            put(JSONObject().put("text", prompt))
          })
        })
      })
      put("generationConfig", JSONObject().apply {
        put("responseMimeType", "application/json")
      })
    }

    val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
    val request = Request.Builder()
      .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
      .post(requestBody)
      .build()

    try {
      client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) return@withContext mapOf("category" to "Other", "tags" to "Error", "summary" to "Classification failed.")
        val responseBodyString = response.body?.string() ?: return@withContext mapOf("category" to "Other", "tags" to "Empty", "summary" to "Empty response.")
        
        val jsonResponse = JSONObject(responseBodyString)
        val candidates = jsonResponse.optJSONArray("candidates")
        val text = candidates?.optJSONObject(0)
          ?.optJSONObject("content")
          ?.optJSONArray("parts")
          ?.optJSONObject(0)
          ?.optString("text") ?: "{}"

        val resultJson = JSONObject(text)
        val category = resultJson.optString("category", "Other")
        val tags = resultJson.optString("tags", "Document, Vault")
        val summary = resultJson.optString("summary", "AI Classified Document.")

        mapOf(
          "category" to category,
          "tags" to tags,
          "summary" to summary
        )
      }
    } catch (e: Exception) {
      mapOf(
        "category" to "Other",
        "tags" to "Error",
        "summary" to "Exception: ${e.localizedMessage}"
      )
    }
  }
}
