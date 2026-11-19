package com.lesli.mobile.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.lesli.mobile.ui.components.ConversationCard
import com.lesli.mobile.ui.components.EmptyStateView
import com.lesli.mobile.ui.components.LesliSearchBar
import com.lesli.mobile.ui.theme.LesliMobileTheme
import com.lesli.mobile.viewmodel.HistoryViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onSelectConversation: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val conversations by viewModel.conversations.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val filteredConversations = conversations.filter { conv ->
        val query = searchQuery.trim().lowercase()
        query.isEmpty() ||
                conv.title.lowercase().contains(query) ||
                conv.lastMessage.lowercase().contains(query)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.history_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Barra de búsqueda
        LesliSearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.onSearchQueryChange(it) },
            placeholder = stringResource(R.string.history_search_placeholder)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredConversations.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                EmptyStateView(
                    icon = Icons.Default.History,
                    title = if (searchQuery.isNotBlank())
                        stringResource(R.string.history_empty_search_title)
                    else
                        stringResource(R.string.history_empty_title),
                    description = if (searchQuery.isNotBlank())
                        stringResource(R.string.history_empty_search_desc, searchQuery)
                    else
                        stringResource(R.string.history_empty_desc)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = filteredConversations,
                    key = { it.id }
                ) { conv ->
                    ConversationCard(
                        conversation = conv,
                        onClick = {
                            viewModel.selectConversation(conv.id)
                            onSelectConversation(conv.id)
                        },
                        onDelete = { viewModel.deleteConversation(conv.id) }
                    )
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

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    LesliMobileTheme(darkTheme = true) {
        HistoryScreen(
            viewModel = HistoryViewModel(PreviewAssistantRepo()),
            onSelectConversation = {}
        )
    }
}
