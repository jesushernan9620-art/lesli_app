package com.lesli.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesli.mobile.data.model.ChatMessage
import com.lesli.mobile.data.repository.AssistantRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AssistantViewModel(
    private val assistantRepo: AssistantRepository
) : ViewModel() {

    val messages: StateFlow<List<ChatMessage>> = assistantRepo.messages.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val currentConvId: StateFlow<String> = assistantRepo.currentConversationId

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _infoMessage = MutableStateFlow<String?>(null)
    val infoMessage: StateFlow<String?> = _infoMessage.asStateFlow()

    fun onInputTextChange(text: String) {
        _inputText.value = text
    }

    fun sendMessage() {
        val query = _inputText.value.trim()
        if (query.isBlank() || _isGenerating.value) return

        _inputText.value = ""
        viewModelScope.launch {
            _isGenerating.value = true
            delay(800)
            assistantRepo.sendMessage(query)
            _isGenerating.value = false
        }
    }

    fun startNewConversation() {
        viewModelScope.launch {
            assistantRepo.startNewConversation()
        }
    }

    fun showFeatureNotice(featureName: String) {
        _infoMessage.value = "La función de $featureName estará disponible al conectar con el backend de FastAPI."
    }

    fun clearInfoNotice() {
        _infoMessage.value = null
    }
}
