package com.dreamtech.compose.gmail.feature.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamtech.compose.gmail.R
import com.dreamtech.compose.gmail.core.data.repository.AccountRepository
import com.dreamtech.compose.gmail.core.domain.GetLabelsUseCase
import com.dreamtech.compose.gmail.core.domain.model.EmailAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainGmailViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val getLabelsUseCase: GetLabelsUseCase,
) :
    ViewModel() {

    private val _navigationStateFlow = MutableStateFlow(
        NavigationUiState(
            items = loadItems(),
            selected = NavigationItem(
                labelRes = R.string.navigation_primary_box,
                iconRes = R.drawable.ic_navigation_inbox
            ),
        )
    )

    val navigationStateFlow: StateFlow<NavigationUiState> = _navigationStateFlow

    private val _accountsStateFlow = MutableStateFlow(AccountsUiState())
    val accountsStateFlow: StateFlow<AccountsUiState> = _accountsStateFlow


    init {
        viewModelScope.launch {
            val accounts = accountRepository.getAccounts()
            val mainAccount = accountRepository.getMainAccount()
            _accountsStateFlow.value = _accountsStateFlow.value.copy(
                currentAccount = mainAccount,
                otherAccounts = accounts.subList(1, accounts.size)
            )
            _navigationStateFlow.value =
                _navigationStateFlow.value.copy(showAll = accounts.size > 1)
            _accountsStateFlow.value.currentAccount?.let { currentAccount ->
                val labels = getLabelsUseCase(currentAccount.address)
                val labelItems = labels.map {
                    val labelText = getLabelText(it.name)
                    if(-1 == labelText) {
                        NavigationItem(label = it.name, iconRes = getLabelIcon(it.name))
                    }else{
                        NavigationItem(labelRes = labelText, iconRes = getLabelIcon(it.name))
                    }
                }
                _navigationStateFlow.value = _navigationStateFlow.value.copy(labels = labelItems)
            }
        }
    }

    private fun loadItems(): List<NavigationItem> {
        return listOf(
            NavigationItem(
                labelRes = R.string.navigation_primary_box,
                iconRes = R.drawable.ic_navigation_inbox,
                badger = 326
            ),
            NavigationItem(
                labelRes = R.string.navigation_promotions,
                iconRes = R.drawable.ic_navigation_promotions
            ),
            NavigationItem(
                labelRes = R.string.navigation_social,
                iconRes = R.drawable.ic_navigation_social
            )

        )
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

    fun select(selected: NavigationItem) {
        _navigationStateFlow.value = _navigationStateFlow.value.copy(selected = selected)
    }


    fun selectAccount(account: EmailAccount) {
        if (_accountsStateFlow.value.currentAccount != account) {
            viewModelScope.launch {
                val accounts = accountRepository.getAccounts()
                val otherAccounts = accounts.minus(account)
                _accountsStateFlow.value = _accountsStateFlow.value.copy(
                    currentAccount = account,
                    otherAccounts = otherAccounts
                )
            }
        }
    }

}


data class NavigationUiState(
    val items: List<NavigationItem> = emptyList(),
    val labels: List<NavigationItem> = emptyList(),
    val recentLabels: List<NavigationItem> = emptyList(),
    val selected: NavigationItem? = null,
    val showAll: Boolean = false
)

data class AccountsUiState(
    val currentAccount: EmailAccount? = null,
    val otherAccounts: List<EmailAccount> = emptyList()
)

data class NavigationItem(
    val labelRes: Int = 0,
    val label: String? = null,
    val iconRes: Int,
    val badger: Int = 0
)