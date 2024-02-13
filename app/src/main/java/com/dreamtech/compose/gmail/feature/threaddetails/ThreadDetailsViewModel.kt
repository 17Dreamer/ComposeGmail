package com.dreamtech.compose.gmail.feature.threaddetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamtech.compose.gmail.core.domain.GetThreadDetailsUseCase
import com.dreamtech.compose.gmail.core.domain.GetThreadUseCase
import com.dreamtech.compose.gmail.core.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ThreadDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getThreadUseCase: GetThreadUseCase,
    getThreadDetailsUseCase: GetThreadDetailsUseCase,
) : ViewModel() {

    private val threadId: String = checkNotNull(savedStateHandle["threadId"])


    private val _threadUiStateFlow = MutableStateFlow<ThreadDetailsUiState>(ThreadDetailsUiState())
    val threadUiStateFlow: StateFlow<ThreadDetailsUiState> = _threadUiStateFlow

    init {
        viewModelScope.launch {
            val threadSubject = getThreadUseCase(threadId = threadId)?.subject ?: ""
            _threadUiStateFlow.value = _threadUiStateFlow.value.copy(
                threadSubject = threadSubject,
                messages = getThreadDetailsUseCase(threadId)
            )
        }
    }
}

data class ThreadDetailsUiState(
    val threadSubject: String = "",
    val messages: List<Message> = emptyList(),
)