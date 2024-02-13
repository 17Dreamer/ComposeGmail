package com.dreamtech.compose.gmail.feature.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamtech.compose.gmail.R
import com.dreamtech.compose.gmail.core.domain.model.EmailAccount

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ThreadList(
    modifier: Modifier = Modifier,
    openAccountSettings: () -> Unit = {},
    openThread: (String) -> Unit = {},
    openNewMail: () -> Unit = {},
    openDrawer: () -> Unit = {}
) {
    val viewModel: ThreadsViewModel = hiltViewModel()
    val emailList by viewModel.threadsStateFlow.collectAsState()

    val listState = rememberLazyListState()
    val expandedFabState = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            EmailTopBar(
                openDrawer = openDrawer
            ) {
                openAccountSettings()
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = openNewMail,
                text = {
                    Text(text = stringResource(id = R.string.action_new_email))
                },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Create,
                        contentDescription = ""
                    )
                },
                modifier = Modifier.padding(5.dp),
                expanded = expandedFabState.value,
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        }
    ) { padding ->

        var refreshing by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullRefreshState(refreshing, {
            refreshing = true
            viewModel.refresh {
                refreshing = false
            }
        })

        Box(Modifier.pullRefresh(pullRefreshState)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding), state = listState
            ) {
//                item {
//                    Filters(viewModel, Modifier.padding(vertical = 5.dp))
//                }
                item {
                    Text(
                        text = stringResource(id = R.string.navigation_all_boxes),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(15.dp)
                    )
                }
                items(emailList) {
                    EmailMessageCard(thread = it, modifier = Modifier.clickable {
                        openThread(it.id)
                    })
                }
            }
            PullRefreshIndicator(
                refreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter),
                contentColor = Color(4, 119, 4, 255)
            )
        }
    }
}

@Composable
private fun Filters(viewModel: ThreadsViewModel, modifier: Modifier = Modifier) {
    val filtersState by viewModel.filtersStateFlow.collectAsState()
    LazyRow(modifier = modifier) {
        item {

            val selectedLabelText = if(filtersState.selectedLabels.isEmpty()){
                stringResource(id = R.string.threads_filter_label)
            }else{
                val firstSelected = filtersState.selectedLabels.first()
                val firstSelectedText = if(firstSelected.labelRes>0){
                    stringResource(id = firstSelected.labelRes)}else{firstSelected.labelString?:firstSelected.labelId.toString()}
                if(filtersState.selectedLabels.size > 1){
                    "$firstSelectedText+${filtersState.selectedLabels.size-1}"
                }else{
                    firstSelectedText
                }
            }
            FilterSpinner(
                options = filtersState.labels,
                item = { label, modifier ->
                    Row(
                        modifier = modifier.padding(vertical = 5.dp, horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val text = if(label.labelRes>0){
                            stringResource(id = label.labelRes)}else{label.labelString?:label.labelId.toString()}
                        Text(text = text, modifier = Modifier.weight(1f))
                        Checkbox(checked = filtersState.selectedLabels.contains(label), onCheckedChange = {}, enabled = false)
                    }
                },
                text = selectedLabelText,
                Modifier.padding(start = 10.dp),
                isSelected = filtersState.selectedLabels.isNotEmpty()
            ) {
                viewModel.selectLabel(it)
            }
        }
        item {
            FilterSpinner(
                options = filtersState.sources,
                item = { sender, modifier ->
                    Row(
                        modifier = modifier.padding(vertical = 5.dp, horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = sender, modifier = Modifier.weight(1f))
                        Checkbox(checked = filtersState.selectedSources.contains(sender), onCheckedChange = {}, enabled = false)
                    }
                },
                text = stringResource(id = R.string.threads_filter_from),
                Modifier.padding(start = 10.dp),
                isSelected = filtersState.selectedSources.isNotEmpty()
            ) {
                viewModel.selectSender(it)
            }
        }
        item {
            //FilterSpinner()
        }
        item {
            //FilterSpinner()
        }
        item {
            //FilterSpinner()
        }
        item {
            //FilterSpinner(Modifier.padding(end = 10.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsDialog(
    accounts: AccountsUiState,
    onAccountClicked: (EmailAccount) -> Unit = {}, onDismiss: () -> Unit = {},
    onActionClick: (Int) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 30.dp),
        content = {
            Column(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = AlertDialogDefaults.shape
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.back_button),
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { onDismiss() }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "",
                        modifier = Modifier.height(24.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(horizontal = 8.dp)
                    )
                }
                LazyColumn {
                    item {
                        AccountsContentDialog(
                            accountsUiState = accounts,
                            modifier = Modifier.padding(horizontal = 7.dp),
                            onAccountClicked = onAccountClicked,
                            onActionClick = onActionClick
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp, horizontal = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = stringResource(id = R.string.action_privacy_policy),
                                fontSize = 12.sp
                            )
                            Text(text = "    .    ", fontWeight = FontWeight.Bold)
                            Text(
                                text = stringResource(id = R.string.action_terms_of_service),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

        },
        onDismissRequest = {}
    )
}

@Composable
fun AccountsContentDialog(
    accountsUiState: AccountsUiState,
    modifier: Modifier = Modifier,
    onAccountClicked: (EmailAccount) -> Unit = {},
    onActionClick: (Int) -> Unit = {}
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        if (accountsUiState.currentAccount != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CurrentAccountHeader(
                    account = accountsUiState.currentAccount,
                    Modifier.padding(10.dp)
                )
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.defaultMinSize(minHeight = 30.dp),
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 5.dp)
                ) {
                    Text(text = stringResource(id = R.string.action_manage_google_account))
                }
                RecommendedActions(Modifier.padding(horizontal = 20.dp, vertical = 7.dp))
                Divider()
                CloudStorageInfo(Modifier.padding(horizontal = 20.dp, vertical = 7.dp))
                Divider()
                if (accountsUiState.otherAccounts.isNotEmpty()) {
                    AccountList(
                        accounts = accountsUiState.otherAccounts,
                        Modifier.padding(horizontal = 20.dp, vertical = 7.dp),
                        onAccountClicked = onAccountClicked
                    )
                }
                MoreActions(
                    Modifier.padding(horizontal = 20.dp, vertical = 7.dp),
                    onActionClick = onActionClick
                )
            }
        }

    }
}

@Composable
fun CurrentAccountHeader(account: EmailAccount, modifier: Modifier = Modifier) {
    AccountItem(
        name = account.displayName,
        email = account.address,
        modifier = modifier,
        profileImageSize = 50.dp,
        count = 100
    )
}

@Composable
fun AccountItem(
    name: String,
    email: String,
    modifier: Modifier = Modifier,
    count: Int = 0,
    profileImageSize: Dp = 40.dp,
    onProfilePictureClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(
            drawableResource = R.drawable.account_avatar,
            description = "",
            modifier = Modifier
                .size(profileImageSize)
                .padding(5.dp)
                .clickable { onProfilePictureClicked() }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .weight(1f)
        ) {
            Text(text = name, fontWeight = FontWeight.Bold)
            Text(text = email, fontSize = 12.sp)
        }
        if (count > 0) {
            val countText = if (count >= 100) "99+" else count.toString()
            Text(text = countText, fontSize = 12.sp)
        }

    }
}


@Composable
fun RecommendedActions(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.ic_recommendation),
            contentDescription = "",
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = stringResource(id = R.string.action_recommended_actions),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_warning),
            contentDescription = "",
            modifier = Modifier
                .padding(5.dp),
            tint = Color.Unspecified
        )
    }
}

@Composable
fun CloudStorageInfo(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.ic_cloud_storage),
            contentDescription = "",
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = stringResource(id = R.string.info_cloud_storage, "18%", "15 GB"),
            modifier = Modifier
                .weight(1f)
                .padding(5.dp),
            fontSize = 12.sp
        )
    }
}

@Composable
fun AccountList(
    accounts: List<EmailAccount>,
    modifier: Modifier = Modifier,
    onAccountClicked: (EmailAccount) -> Unit = {}
) {
    Column(modifier = modifier) {
        accounts.forEach { account ->
            AccountItem(
                name = account.displayName,
                email = account.address,
                profileImageSize = 45.dp,
                count = 100,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .clickable { onAccountClicked(account) }
            )
        }
    }
}

@Composable
fun MoreActions(modifier: Modifier = Modifier, onActionClick: (Int) -> Unit = {}) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.clickable { onActionClick(R.string.action_add_account) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_account),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = stringResource(id = R.string.action_add_account),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            )
        }
        Row(
            modifier = Modifier.clickable { onActionClick(R.string.action_manage_accounts) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_manage_accounts),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = stringResource(id = R.string.action_manage_accounts),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            )
        }
    }
}







