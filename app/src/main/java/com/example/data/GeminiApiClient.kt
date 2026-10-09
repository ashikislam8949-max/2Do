package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiApiClient {
  private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

  suspend fun sendMessage(userPrompt: String, history: List<Pair<String, String>>): String = withContext(Dispatchers.IO) {
    val apiKey = try {
      val field = com.example.BuildConfig::class.java.getField("GEMINI_API_KEY")
      field.get(null) as? String ?: ""
    } catch (e: Exception) {
      ""
    }

    if (apiKey.isBlank()) {
      return@withContext getFallbackResponse(userPrompt)
    }

    try {
      val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

      val contentsArray = JSONArray()

      val systemContext = JSONObject().apply {
        put("role", "user")
        put("parts", JSONArray().put(JSONObject().put("text", "You are BBCI GovTech AI Assistant, an expert advisor on Saudi Government e-services (ZATCA, Qiwa, Muqeem, MISA, GOSI, Commercial Registration, Enterprise Cloud). Provide professional, helpful, accurate answers in English or Arabic depending on user query.")))
      }
      contentsArray.put(systemContext)

      val systemResponse = JSONObject().apply {
        put("role", "model")
        put("parts", JSONArray().put(JSONObject().put("text", "Understood. I am ready to assist BBCI users with Saudi government e-services and enterprise solutions.")))
      }
      contentsArray.put(systemResponse)

      for ((role, text) in history) {
        val msgObj = JSONObject().apply {
          put("role", if (role == "user") "user" else "model")
          put("parts", JSONArray().put(JSONObject().put("text", text)))
        }
        contentsArray.put(msgObj)
      }

      val currentObj = JSONObject().apply {
        put("role", "user")
        put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
      }
      contentsArray.put(currentObj)

      val requestBodyJson = JSONObject().apply {
        put("contents", contentsArray)
      }

      val body = requestBodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
      val request = Request.Builder()
        .url(url)
        .post(body)
        .build()

      client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
          return@withContext getFallbackResponse(userPrompt)
        }
        val responseString = response.body?.string() ?: return@withContext getFallbackResponse(userPrompt)
        val jsonResponse = JSONObject(responseString)
        val candidates = jsonResponse.optJSONArray("candidates")
        if (candidates != null && candidates.length() > 0) {
          val firstCandidate = candidates.getJSONObject(0)
          val content = firstCandidate.optJSONObject("content")
          val parts = content?.optJSONArray("parts")
          if (parts != null && parts.length() > 0) {
            return@withContext parts.getJSONObject(0).optString("text", "No response generated.")
          }
        }
        return@withContext getFallbackResponse(userPrompt)
      }
    } catch (e: Exception) {
      return@withContext getFallbackResponse(userPrompt)
    }
  }

  private fun getFallbackResponse(query: String): String {
    val q = query.lowercase()
    return when {
      q.contains("rhq") || q.contains("regional headquarter") ->
        "🏢 **MISA Regional Headquarters (RHQ) Program**:\nMultinational enterprises establishing their regional HQ in Riyadh receive:\n• 30-year 0% corporate income tax and withholding tax exemptions\n• Unlimited work visas with spouse work authorization\n• Priority eligibility for Saudi government contracts & megaprojects\n• 10-year Saudization exemptions for RHQ executives.\nYou can request RHQ licensing directly via BBCI."
      q.contains("misa") || q.contains("foreign investment") || q.contains("investor license") -> 
        "🏛️ **Saudi Ministry of Investment (MISA) Services**:\n• **100% Foreign Ownership**: Full investor licensing for trading, consulting, IT, services, and contracting.\n• **Startup License**: Zero minimum capital requirement endorsed by venture capital / incubators.\n• **Branch Office**: Direct branch of overseas parent entity.\n• **Executive Visas**: Fast-track Investor Visas and Premium Residency endorsements.\nSubmit your MISA application through BBCI Services catalog!"
      q.contains("cr") || q.contains("commercial registration") || q.contains("sbc") || q.contains("سجل") -> 
        "📜 **Saudi Commercial Registration (CR) Services**:\n• **Instant CR Issuance**: Generated within 2-4 hours via Saudi Business Center (SBC) with automated 700 Unified Number and Chamber membership.\n• **Multi-Year Renewal**: 1 to 5-year electronic renewals with instant certificate delivery.\n• **Articles of Association (AoA)**: Electronic drafting and MoJ notary attestation for LLCs.\n• **Sub-CR Branches**: Province-wide expansion across Riyadh, Jeddah, Eastern Province.\n• **Trade Name**: Instant name search and validation."
      q.contains("zatca") || q.contains("invoice") || q.contains("فاتورة") -> 
        "🏛️ **ZATCA E-Invoicing (FATOORA)**:\nPhase 2 integration requires XML invoice generation with cryptographic stamps (PIH, UUID, QR code). You can connect your ERP via our ZATCA API gateway in the Services tab."
      q.contains("qiwa") || q.contains("permit") || q.contains("work") || q.contains("قوى") -> 
        "🏛️ **Qiwa Work Permits**: Qiwa platform manages work contracts, instant work permits, and Saudization (Nitaqat) ratios. Processing takes instant to 24 hours through our direct Qiwa integration."
      q.contains("muqeem") || q.contains("residency") || q.contains("iqama") || q.contains("مقيم") -> 
        "🏛️ **Muqeem Residency Portal**: Muqeem provides automated Iqama renewals, instant exit/re-entry visas, profession amendments, and border control updates for enterprise employees."
      else -> 
        "🤖 **BBCI AI Assistant**: I can help you with all Saudi government services, MISA foreign investment licensing (RHQ, 100% ownership, startups), and Commercial Registration (CR instant issuance, renewals, AoA, branches). Please select or ask about any service!"
    }
  }
}
