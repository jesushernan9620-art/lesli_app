package com.lesli.mobile.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lesli.mobile.R
import com.lesli.mobile.data.local.DemoData
import com.lesli.mobile.data.model.ChatMessage
import com.lesli.mobile.data.model.Conversation
import com.lesli.mobile.data.model.TaskItem
import com.lesli.mobile.data.model.UserProfile
import com.lesli.mobile.data.repository.AssistantRepository
import com.lesli.mobile.data.repository.TaskRepository
import com.lesli.mobile.data.repository.UserPreferencesRepository
import com.lesli.mobile.ui.components.LesliCard
import com.lesli.mobile.ui.components.LesliMoonLogo
import com.lesli.mobile.ui.theme.LesliMobileTheme
import com.lesli.mobile.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToAssistant: (initialQuery: String?) -> Unit,
    onNavigateToTasks: () -> Unit,
    onNavigateToHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val conversations by viewModel.conversations.collectAsState()

    var quickQuery by remember { mutableStateOf("") }
    val pendingTasksCount = tasks.count { !it.isCompleted }
    val recentConversations = conversations.take(2)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Saludo personalizado con identidad cósmica
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(R.string.home_greeting, userProfile.name),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.home_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tarjeta principal para conversar con Lesli (Moon Hero Card)
        LesliCard(
            modifier = Modifier.fillMaxWidth(),
            padding = 16.dp
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LesliMoonLogo(size = 46.dp, showGlow = true)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = stringResource(R.string.home_chat_card_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.home_chat_card_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = quickQuery,
                onValueChange = { quickQuery = it },
                placeholder = {
                    Text(
                        stringResource(R.string.home_quick_query_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            val textToSend = quickQuery.ifBlank { null }
                            quickQuery = ""
                            onNavigateToAssistant(textToSend)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = stringResource(R.string.cd_send),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Accesos rápidos (Tareas e Historial)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LesliCard(
                onClick = onNavigateToTasks,
                modifier = Modifier.weight(1f),
                padding = 14.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.home_card_tasks_title),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.home_tasks_pending_count, pendingTasksCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LesliCard(
                onClick = onNavigateToHistory,
                modifier = Modifier.weight(1f),
                padding = 14.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.home_card_history_title),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.home_history_conv_count, conversations.size),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Resumen de Tareas Pendientes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.home_section_pending_tasks),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.home_view_all),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.clickable { onNavigateToTasks() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        val pendingList = tasks.filter { !it.isCompleted }.take(3)
        if (pendingList.isEmpty()) {
            LesliCard(padding = 14.dp) {
                Text(
                    text = stringResource(R.string.home_no_pending_tasks),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                pendingList.forEach { task ->
                    LesliCard(
                        onClick = onNavigateToTasks,
                        padding = 12.dp
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = task.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = stringResource(R.string.home_task_due, task.dueDate),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Actividad Reciente (Conversaciones)
        Text(
            text = stringResource(R.string.home_section_recent_activity),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (recentConversations.isEmpty()) {
            LesliCard(padding = 14.dp) {
                Text(
                    text = stringResource(R.string.home_no_recent_activity),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                recentConversations.forEach { conv ->
                    LesliCard(
                        onClick = { onNavigateToAssistant(null) },
                        padding = 12.dp
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LesliMoonLogo(size = 30.dp, showGlow = false)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = conv.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = conv.lastMessage,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
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

private class PreviewTaskRepo : TaskRepository {
    override val tasks: Flow<List<TaskItem>> = flowOf(DemoData.getInitialTasks())
    override suspend fun addTask(task: TaskItem) {}
    override suspend fun updateTask(task: TaskItem) {}
    override suspend fun toggleTaskCompletion(taskId: String) {}
    override suspend fun deleteTask(taskId: String) {}
}

private class PreviewUserPrefRepo : UserPreferencesRepository {
    override val userProfile: Flow<UserProfile> = flowOf(UserProfile())
    override suspend fun updateProfile(name: String, email: String) {}
    override suspend fun toggleNotifications(enabled: Boolean) {}
    override suspend fun toggleDarkMode(enabled: Boolean) {}
    override suspend fun toggleSound(enabled: Boolean) {}
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    LesliMobileTheme(darkTheme = true) {
        HomeScreen(
            viewModel = HomeViewModel(
                PreviewAssistantRepo(),
                PreviewTaskRepo(),
                PreviewUserPrefRepo()
            ),
            onNavigateToAssistant = {},
            onNavigateToTasks = {},
            onNavigateToHistory = {}
        )
    }
}
