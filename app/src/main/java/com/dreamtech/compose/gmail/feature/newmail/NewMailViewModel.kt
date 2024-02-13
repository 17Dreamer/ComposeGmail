package com.dreamtech.compose.gmail.feature.newmail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamtech.compose.gmail.core.data.repository.AccountRepository
import com.dreamtech.compose.gmail.core.data.repository.MessageRepository
import com.dreamtech.compose.gmail.core.data.repository.ThreadsRepository
import com.dreamtech.compose.gmail.core.domain.Result
import com.dreamtech.compose.gmail.core.domain.SendMailUseCase
import com.dreamtech.compose.gmail.core.domain.SignInUseCase
import com.dreamtech.compose.gmail.core.domain.model.EmailAccount
import com.dreamtech.compose.gmail.core.domain.model.EmailThread
import com.dreamtech.compose.gmail.core.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewMailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository,
    private val messageRepository: MessageRepository,
    private val threadRepository: ThreadsRepository,
    private val sendMailUseCase: SendMailUseCase,
) : ViewModel() {

    private val entranceType: String = checkNotNull(savedStateHandle["EntranceType"])
    private val messageId: String = checkNotNull(savedStateHandle["message"])

    private var currentThread : EmailThread? = null

    private val _accountStateFlow = MutableStateFlow<EmailAccount?>(null)
    val accountStateFlow: StateFlow<EmailAccount?> = _accountStateFlow

    private val _newMailUiStateFlow = MutableStateFlow(NewMailUiState())
    val newMailUiStateFlow: StateFlow<NewMailUiState> = _newMailUiStateFlow

    init {
        viewModelScope.launch {
            accountRepository.getMainAccount()?.let { mainAccount ->
                _accountStateFlow.value = mainAccount
            }
            val message = messageRepository.getMessage(messageId)
            var subject = ""
            var destination = ""
            var ccDestination = ""
            var bccDestination = ""
            message?.let {
                threadRepository.getThread(it.thread)?.let { thread ->
                    currentThread = thread
                    if(EntranceType.Reply.value == entranceType || EntranceType.ReplyAll.value == entranceType) {
                        subject = "Re: ${thread.subject}"
                    }else if(EntranceType.Forward.value == entranceType){
                        subject = "Fwd: ${thread.subject}"
                    }
                }
                if(EntranceType.Reply.value == entranceType || EntranceType.ReplyAll.value == entranceType) {
                    destination = if(_accountStateFlow.value?.address == it.from){
                        it.destination
                    }else{
                        it.from
                    }

                    if (EntranceType.ReplyAll.value == entranceType) {
                        ccDestination = it.ccDestination
                    }
                }

            }
            _newMailUiStateFlow.value =
                _newMailUiStateFlow.value.copy(
                    sender = _accountStateFlow.value?.address?:"",
                    destination = destination,
                    cc = ccDestination,
                    bcc = bccDestination,
                    subject = subject
                )
        }
    }

    fun sendMail(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val ccDestination = if (_newMailUiStateFlow.value.cc.isEmpty()) {
                emptyList()
            } else {
                listOf(_newMailUiStateFlow.value.cc)
            }
            val bccDestination = if (_newMailUiStateFlow.value.bcc.isEmpty()) {
                emptyList()
            } else {
                listOf(_newMailUiStateFlow.value.bcc)
            }
            _accountStateFlow.value?.let { sender ->
                val result = sendMailUseCase(
                    thread = currentThread?.id,
                    from = sender,
                    destination = listOf(_newMailUiStateFlow.value.destination),
                    cc = ccDestination,
                    bcc = bccDestination,
                    subject = _newMailUiStateFlow.value.subject,
                    content = _newMailUiStateFlow.value.content
                )
                if (result is Result.Success) {
                    onSuccess()
                }
            }

        }
    }

    fun updateDestination(destination: String) {
        _newMailUiStateFlow.value = _newMailUiStateFlow.value.copy(destination = destination)
    }

    fun updateSubject(subject: String) {
        _newMailUiStateFlow.value = _newMailUiStateFlow.value.copy(subject = subject)
    }

    fun updateContent(content: String) {
        _newMailUiStateFlow.value = _newMailUiStateFlow.value.copy(content = content)
    }

    fun updateBcc(bcc: String) {
        _newMailUiStateFlow.value = _newMailUiStateFlow.value.copy(bcc = bcc)
    }

    fun updateCc(cc: String) {
        _newMailUiStateFlow.value = _newMailUiStateFlow.value.copy(cc = cc)
    }

}

data class NewMailUiState(
    val sender: String = "",
    val destination: String = "",
    val cc: String = "",
    val bcc: String = "",
    val subject: String = "",
    val content: String = "",
)