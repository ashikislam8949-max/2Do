package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GeminiApiClient
import kotlinx.coroutines.launch

data class ChatMessage(
  val text: String,
  val isUser: Boolean,
  val timestamp: String = "Just now"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(
  onBackClick: () -> Unit
) {
  val scope = rememberCoroutineScope()
  val listState = rememberLazyListState()

  var messageInput by remember { mutableStateOf("") }
  var isTyping by remember { mutableStateOf(false) }

  val chatMessages = remember {
    mutableStateListOf(
      ChatMessage(
        text = "Hello! I am your BBCI GovTech AI Assistant powered by Gemini. How can I assist you today with Saudi government services, ZATCA e-invoicing, Qiwa permits, or company licensing?",
        isUser = false
      )
    )
  }

  val quickPrompts = listOf(
    "MISA Investment License",
    "Regional HQ (RHQ) 30-Yr Tax Relief",
    "Instant CR Issuance (SBC)",
    "CR Renewal & Chamber Fee",
    "Articles of Association (AoA)",
    "ZATCA E-Invoicing Phase 2",
    "Qiwa Permits & Muqeem"
  )

  fun sendMessage(text: String) {
    if (text.isBlank()) return
    val userMsg = text.trim()
    chatMessages.add(ChatMessage(text = userMsg, isUser = true))
    messageInput = ""
    isTyping = true

    scope.launch {
      listState.animateScrollToItem(chatMessages.size - 1)
      val history = chatMessages.dropLast(1).map { (if (it.isUser) "user" else "model") to it.text }
      val aiResponse = GeminiApiClient.sendMessage(userMsg, history)
      isTyping = false
      chatMessages.add(ChatMessage(text = aiResponse, isUser = false))
      listState.animateScrollToItem(chatMessages.size - 1)
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
              shape = CircleShape,
              color = MaterialTheme.colorScheme.primaryContainer,
              modifier = Modifier.size(36.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
            Column {
              Text("BBCI GovTech AI Assistant", fontWeight = FontWeight.Bold, fontSize = 16.sp)
              Text("Powered by Gemini", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
            }
          }
        },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Text("←", fontSize = 20.sp)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      )
    },
    bottomBar = {
      Surface(
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(8.dp)) {
          // Quick prompt chips
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState())
              .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            quickPrompts.forEach { prompt ->
              AssistChip(
                onClick = { sendMessage(prompt) },
                label = { Text(prompt, fontSize = 12.sp) },
                shape = RoundedCornerShape(12.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(4.dp))

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = messageInput,
              onValueChange = { messageInput = it },
              modifier = Modifier
                .weight(1f)
                .heightIn(min = 52.dp),
              placeholder = { Text("Ask about ZATCA, Qiwa, CR, or Muqeem...") },
              shape = RoundedCornerShape(24.dp),
              maxLines = 3
            )
            Spacer(modifier = Modifier.width(8.dp))
            FloatingActionButton(
              onClick = { sendMessage(messageInput) },
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary,
              modifier = Modifier.size(52.dp),
              shape = CircleShape
            ) {
              Icon(Icons.Default.Send, contentDescription = "Send")
            }
          }
        }
      }
    }
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .background(MaterialTheme.colorScheme.background)
    ) {
      LazyColumn(
        state = listState,
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
      ) {
        items(chatMessages) { msg ->
          ChatBubble(message = msg)
        }

        if (isTyping) {
          item {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(32.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                }
              }
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
              ) {
                Text(
                  text = "Gemini is thinking...",
                  modifier = Modifier.padding(12.dp),
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun ChatBubble(message: ChatMessage) {
  val isUser = message.isUser
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
  ) {
    if (!isUser) {
      Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.size(32.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
        }
      }
      Spacer(modifier = Modifier.width(8.dp))
    }

    Surface(
      shape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isUser) 16.dp else 4.dp,
        bottomEnd = if (isUser) 4.dp else 16.dp
      ),
      color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
      modifier = Modifier.widthIn(max = 300.dp)
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text(
          text = message.text,
          style = MaterialTheme.typography.bodyMedium,
          color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    if (isUser) {
      Spacer(modifier = Modifier.width(8.dp))
      Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.size(32.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
        }
      }
    }
  }
}
