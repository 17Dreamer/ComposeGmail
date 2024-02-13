package com.dreamtech.compose.gmail.feature.mainscreen

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamtech.compose.gmail.R
import com.dreamtech.compose.gmail.feature.mainscreen.component.EmailDrawerContent
import kotlinx.coroutines.launch

@Composable
fun MainGmailScreen(
    modifier: Modifier = Modifier,
    viewModel: MainGmailViewModel = hiltViewModel(),
    openThread: (String) -> Unit = {},
    openNewMail: () -> Unit = {},
    openAccountSetUp: () -> Unit = {},
    openSettingsScreen: () -> Unit = {},
) {
    val uiState by viewModel.navigationStateFlow.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            EmailDrawerContent(
                uiState = uiState,
                onDrawerClicked = {
                    if (R.string.navigation_settings == it.labelRes) {
                        openSettingsScreen()
                    } else {
                        viewModel.select(it)
                    }
                    scope.launch {
                        drawerState.close()
                    }
                })
        },
        modifier = modifier
    ) {
        val accountsUiState by viewModel.accountsStateFlow.collectAsState()
        var showAccountsDialog by rememberSaveable {
            mutableStateOf(false)
        }
        if (showAccountsDialog) {
            AccountsDialog(
                accounts = accountsUiState,
                onAccountClicked = {
                    viewModel.selectAccount(it)
                    showAccountsDialog = false
                },
                onActionClick = {
                    when (it) {
                        R.string.action_add_account -> {
                            showAccountsDialog = false
                            openAccountSetUp()
                        }

                        R.string.action_manage_accounts -> {

                        }
                    }
                },
                onDismiss = { showAccountsDialog = false },
            )
        }
        ThreadList(
            openAccountSettings = {
                showAccountsDialog = true
            },
            openThread = openThread,
            openNewMail = openNewMail
        ) {
            scope.launch {
                drawerState.open()
            }
        }
    }
}