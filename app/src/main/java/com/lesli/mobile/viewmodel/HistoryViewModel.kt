package com.lesli.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesli.mobile.data.model.Conversation
import com.lesli.mobile.data.repository.AssistantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val assistantRepo: AssistantRepository
) : ViewModel() {

    val conversations: StateFlow<List<Conversation>> = assistantRepo.conversations.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun selectConversation(id: String) {
        assistantRepo.selectConversation(id)
    }

    fun deleteConversation(id: String) {
        viewModelScope.launch {
            assistantRepo.deleteConversation(id)
        }
    }
}
