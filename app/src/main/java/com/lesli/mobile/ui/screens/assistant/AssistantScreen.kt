package com.lesli.mobile.ui.screens.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lesli.mobile.R
import com.lesli.mobile.data.local.DemoData
import com.lesli.mobile.data.model.ChatMessage
import com.lesli.mobile.data.model.Conversation
import com.lesli.mobile.data.repository.AssistantRepository
import com.lesli.mobile.ui.components.ChatBubble
import com.lesli.mobile.ui.components.LesliMoonLogo
import com.lesli.mobile.ui.components.LesliOutlinedButton
import com.lesli.mobile.ui.components.TypingIndicator
import com.lesli.mobile.ui.theme.LesliMobileTheme
import com.lesli.mobile.viewmodel.AssistantViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

@Composable
fun AssistantScreen(
    viewModel: AssistantViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.messages.collectAsState()
    val currentConvId by viewModel.currentConvId.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val infoNotice by viewModel.infoMessage.collectAsState()

    val currentMessages = messages.filter { it.conversationId == currentConvId }
    val listState = rememberLazyListState()

    // Scroll to the newest message whenever messages change
    LaunchedEffect(currentMessages.size, isGenerating) {
        if (currentMessages.isNotEmpty()) {
            listState.animateScrollToItem(
                index = currentMessages.size + (if (isGenerating) 1 else 0) - 1
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // Header Asistente
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LesliMoonLogo(size = 36.dp, showGlow = true)
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.assistant_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(R.string.assistant_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                LesliOutlinedButton(
                    text = stringResource(R.string.assistant_btn_new),
                    onClick = { viewModel.startNewConversation() },
                    icon = Icons.Default.Add
                )
            }

            // Lista de mensajes de chat
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(
                    items = currentMessages,
                    key = { it.id }
                ) { msg ->
                    ChatBubble(message = msg)
                }

                if (isGenerating) {
                    item {
                        TypingIndicator()
                    }
                }
            }

            // Barra inferior de escritura de mensaje
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, MaterialTheme.colorScheme.outline)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.showFeatureNotice("adjuntar archivos") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = stringResource(R.string.cd_attach_file),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { viewModel.showFeatureNotice("micrófono y reconocimiento de voz") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = stringResource(R.string.cd_microphone),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { viewModel.onInputTextChange(it) },
                        placeholder = {
                            Text(
                                stringResource(R.string.assistant_input_placeholder),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        maxLines = 4,
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = { viewModel.sendMessage() },
                        enabled = inputText.isNotBlank() && !isGenerating
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = stringResource(R.string.cd_send),
                            tint = if (inputText.isNotBlank() && !isGenerating)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }

        // Diálogo informativo para funciones pendientes (Adjuntar / Micrófono)
        if (infoNotice != null) {
            AlertDialog(
                onDismissRequest = { viewModel.clearInfoNotice() },
                title = { Text(stringResource(R.string.assistant_dialog_title)) },
                text = { Text(infoNotice!!) },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearInfoNotice() }) {
                        Text(stringResource(R.string.btn_ok), color = MaterialTheme.colorScheme.primary)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

private class PreviewAssistantRepo : AssistantRepository {
    override val messages: Flow<List<ChatMessage>> = flowOf(DemoData.getInitialMessages())
    override val conversations: Flow<List<Conversation>> = flowOf(DemoData.getInitialConversations())
    override val currentConversationId: StateFlow<String> = MutableStateFlow("conv_1").asStateFlow()
    override suspend fun sendMessage(text: String) {}
    override suspend fun startNewConversation() {}
    override fun selectConversation(id: String) {}
    override suspend fun deleteConversation(id: String) {}
}

@Preview(showBackground = true)
@Composable
fun AssistantScreenPreview() {
    LesliMobileTheme(darkTheme = true) {
        AssistantScreen(
            viewModel = AssistantViewModel(PreviewAssistantRepo())
        )
    }
}
