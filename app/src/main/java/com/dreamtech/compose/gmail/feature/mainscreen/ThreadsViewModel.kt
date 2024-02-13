package com.dreamtech.compose.gmail.feature.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamtech.compose.gmail.R
import com.dreamtech.compose.gmail.core.data.repository.AccountRepository
import com.dreamtech.compose.gmail.core.domain.GetLabelsUseCase
import com.dreamtech.compose.gmail.core.domain.GetThreadListUseCase
import com.dreamtech.compose.gmail.core.domain.model.EmailAccount
import com.dreamtech.compose.gmail.core.domain.model.EmailThread
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThreadsViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val threadListUseCase: GetThreadListUseCase,
    private val getLabelsUseCase: GetLabelsUseCase,
) :
    ViewModel() {

    private var currentAccount: EmailAccount? = null

    private val _threadsStateFlow = MutableStateFlow(emptyList<EmailThread>())
    val threadsStateFlow: StateFlow<List<EmailThread>> = _threadsStateFlow

    private val _filtersStateFlow = MutableStateFlow(FiltersUIState())
    val filtersStateFlow: StateFlow<FiltersUIState> = _filtersStateFlow

    init {
        viewModelScope.launch {
            currentAccount = accountRepository.getMainAccount()
            currentAccount?.address?.let {
                val labels = getLabelsUseCase(it)
                val labelUIStates = labels.map { label->
                    LabelUIState(labelId = label.id, labelRes = getLabelText(label.name), labelString = label.name, icon = getLabelIcon(label.name))
                }
                _filtersStateFlow.value = _filtersStateFlow.value.copy(labels = labelUIStates)
            }
        }
        refresh()

    }

    fun selectAccount(account: EmailAccount) {
        if (currentAccount != account) {
            viewModelScope.launch {
                //reload threads
            }
        }
    }

    fun refresh(onFinish: () -> Unit = {}) {
        viewModelScope.launch {
            val selectedLabels = _filtersStateFlow.value.selectedLabels.map { it.labelId }
            threadListUseCase(selectedLabels).collectLatest {
                _threadsStateFlow.value = it
                onFinish()
            }
        }
    }

    fun selectLabel(label: LabelUIState) {
        var selectedLabels = _filtersStateFlow.value.selectedLabels
        selectedLabels = if (selectedLabels.contains(label)) {
            selectedLabels.minus(label)
        } else {
            selectedLabels.plus(label)
        }
        _filtersStateFlow.value = _filtersStateFlow.value.copy(
            selectedLabels = selectedLabels
        )

        refresh()
    }

    fun selectSender(it: String) {

    }


    private fun getLabelIcon(label: String): Int {
        return when (label.uppercase()) {
            "STARRED" -> R.drawable.ic_navigation_starred
            "SNOOZED" -> R.drawable.ic_navigation_snoozed
            "IMPORTANT" -> R.drawable.ic_navigation_important
            "SENT" -> R.drawable.ic_navigation_send
            "SCHEDULED" -> R.drawable.ic_navigation_scheduled
            "OUTBOX" -> R.drawable.ic_navigation_outbox
            "DRAFT" -> R.drawable.ic_navigation_drafts
            "ALL MAIL" -> R.drawable.ic_navigation_all_mail
            "SPAM" -> R.drawable.ic_navigation_spam
            "TRASH" -> R.drawable.ic_navigation_trash
            else -> R.drawable.ic_navigation_label
        }
    }

    private fun getLabelText(label: String): Int {
        return when (label.uppercase()) {
            "STARRED" -> R.string.navigation_starred
            "SNOOZED" -> R.string.navigation_snoozed
            "IMPORTANT" -> R.string.navigation_important
            "SENT" -> R.string.navigation_sent
            "SCHEDULED" -> R.string.navigation_scheduled
            "OUTBOX" -> R.string.navigation_outbox
            "DRAFT" -> R.string.navigation_drafts
            "ALL MAIL" -> R.string.navigation_all_mail
            "SPAM" -> R.string.navigation_spam
            "TRASH" -> R.string.navigation_trash
            else -> -1
        }
    }


}

data class FiltersUIState(
    val labels: List<LabelUIState> = emptyList(),
    val selectedLabels: List<LabelUIState> = emptyList(),
    val sources: List<String> = emptyList(),
    val selectedSources: List<String> = emptyList(),
    val destinations: List<String> = emptyList(),
    val selectedDestinations: List<String> = emptyList(),
    val dates: List<Int> = emptyList(),
    val selectedDate: Int = R.string.threads_filter_date,
)

data class LabelUIState(
    val labelId: Int,
    val labelRes: Int = 0,
    val labelString: String? = null,
    val icon: Int = R.drawable.ic_navigation_label,
    val isSelected: Boolean = false

)


