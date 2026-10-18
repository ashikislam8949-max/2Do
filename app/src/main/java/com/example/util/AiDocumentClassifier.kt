package com.example.util

import com.example.data.FirebaseGemini
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

object AiDocumentClassifier {
  suspend fun classifyDocument(documentTitle: String, documentContent: String): Map<String, String> = withContext(Dispatchers.IO) {
    if (!FirebaseGemini.isConfigured()) {
      return@withContext unavailable()
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

    try {
      val model = FirebaseGemini.model(
        jsonResponse = true,
        systemInstruction = "You classify documents. Reply with JSON only."
      )
      val text = model.generateContent(prompt).text ?: return@withContext failed()
      parseClassificationJson(text) ?: failed()
    } catch (error: Exception) {
      failed()
    }
  }

  private fun unavailable() = mapOf(
    "category" to "Other",
    "tags" to "General, Unclassified",
    "summary" to "Classification is unavailable."
  )

  private fun failed() = mapOf(
    "category" to "Other",
    "tags" to "Error",
    "summary" to "Classification failed."
  )
}

internal fun parseClassificationJson(raw: String): Map<String, String>? {
  val trimmed = raw.trim()
    .removePrefix("```json")
    .removePrefix("```JSON")
    .removePrefix("```")
    .removeSuffix("```")
    .trim()
  val jsonText = if (trimmed.startsWith("{")) {
    trimmed
  } else {
    val start = trimmed.indexOf('{')
    val end = trimmed.lastIndexOf('}')
    if (start >= 0 && end > start) trimmed.substring(start, end + 1) else return null
  }
  return try {
    val resultJson = JSONObject(jsonText)
    mapOf(
      "category" to resultJson.optString("category", "Other").ifBlank { "Other" },
      "tags" to resultJson.optString("tags", "Document, Vault").ifBlank { "Document, Vault" },
      "summary" to resultJson.optString("summary", "AI Classified Document.").ifBlank { "AI Classified Document." }
    )
  } catch (error: Exception) {
    null
  }
}
