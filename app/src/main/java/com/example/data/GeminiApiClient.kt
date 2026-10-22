package com.example.data

import com.google.firebase.ai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiApiClient {
  private const val SYSTEM_INSTRUCTION =
    "You are the 2Do Tech AI Assistant, an expert advisor on Saudi business and government e-services " +
      "(ZATCA, Qiwa, Muqeem, MISA, GOSI, Commercial Registration, Enterprise Cloud). " +
      "Provide professional, helpful, accurate answers in English or Arabic depending on the user query."

  suspend fun sendMessage(userPrompt: String, history: List<Pair<String, String>>): String = withContext(Dispatchers.IO) {
    if (!FirebaseGemini.isConfigured()) {
      return@withContext getFallbackResponse(userPrompt)
    }

    try {
      val model = FirebaseGemini.model(systemInstruction = SYSTEM_INSTRUCTION)
      val priorTurns = history
        .dropWhile { (role, _) -> role != "user" }
        .map { (role, text) ->
          content(role = if (role == "user") "user" else "model") { text(text) }
        }
      val chat = model.startChat(history = priorTurns)
      chat.sendMessage(userPrompt).text?.takeIf { it.isNotBlank() } ?: getFallbackResponse(userPrompt)
    } catch (error: Exception) {
      getFallbackResponse(userPrompt)
    }
  }

  private fun getFallbackResponse(query: String): String {
    val q = query.lowercase()
    return when {
      q.contains("rhq") || q.contains("regional headquarter") ->
        "🏢 **MISA Regional Headquarters (RHQ) Program**:\nMultinational enterprises establishing their regional HQ in Riyadh receive:\n• 30-year 0% corporate income tax and withholding tax exemptions\n• Unlimited work visas with spouse work authorization\n• Priority eligibility for Saudi government contracts & megaprojects\n• 10-year Saudization exemptions for RHQ executives.\nYou can request RHQ licensing through the 2Do Tech Services catalog."
      q.contains("misa") || q.contains("foreign investment") || q.contains("investor license") ->
        "🏛️ **Saudi Ministry of Investment (MISA) Services**:\n• **100% Foreign Ownership**: Full investor licensing for trading, consulting, IT, services, and contracting.\n• **Startup License**: Zero minimum capital requirement endorsed by venture capital / incubators.\n• **Branch Office**: Direct branch of overseas parent entity.\n• **Executive Visas**: Fast-track Investor Visas and Premium Residency endorsements.\nSubmit your MISA application through the 2Do Tech Services catalog!"
      q.contains("cr") || q.contains("commercial registration") || q.contains("sbc") || q.contains("سجل") ->
        "📜 **Saudi Commercial Registration (CR) Services**:\n• **Instant CR Issuance**: Generated within 2-4 hours via Saudi Business Center (SBC) with automated 700 Unified Number and Chamber membership.\n• **Multi-Year Renewal**: 1 to 5-year electronic renewals with instant certificate delivery.\n• **Articles of Association (AoA)**: Electronic drafting and MoJ notary attestation for LLCs.\n• **Sub-CR Branches**: Province-wide expansion across Riyadh, Jeddah, Eastern Province.\n• **Trade Name**: Instant name search and validation."
      q.contains("zatca") || q.contains("invoice") || q.contains("فاتورة") ->
        "🏛️ **ZATCA E-Invoicing (FATOORA)**:\nPhase 2 integration requires XML invoice generation with cryptographic stamps (PIH, UUID, QR code). You can connect your ERP via our ZATCA API gateway in the Services tab."
      q.contains("qiwa") || q.contains("permit") || q.contains("work") || q.contains("قوى") ->
        "🏛️ **Qiwa Work Permits**: Qiwa platform manages work contracts, instant work permits, and Saudization (Nitaqat) ratios. Processing takes instant to 24 hours through our direct Qiwa integration."
      q.contains("muqeem") || q.contains("residency") || q.contains("iqama") || q.contains("مقيم") ->
        "🏛️ **Muqeem Residency Portal**: Muqeem provides automated Iqama renewals, instant exit/re-entry visas, profession amendments, and border control updates for enterprise employees."
      else ->
        "🤖 **2Do Tech AI Assistant**: I can help you with Saudi government services, MISA foreign investment licensing (RHQ, 100% ownership, startups), and Commercial Registration (CR instant issuance, renewals, AoA, branches). Please select or ask about any service!"
    }
  }
}
