package com.dreamtech.compose.gmail.feature.newmail

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamtech.compose.gmail.R
import com.dreamtech.compose.gmail.core.domain.model.EmailAccount
import com.dreamtech.compose.gmail.ui.theme.GmailTheme

@Composable
fun NewMailScreen(viewModel: NewMailViewModel = hiltViewModel(), onBackClick: () -> Unit = {}) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxHeight()) {

            val newMailUiState by viewModel.newMailUiStateFlow.collectAsState()
            NewMailTopBar(
                onBackClicked = onBackClick,
                onSendClicked = {
                    viewModel.sendMail{
                        onBackClick()
                    }
                },
                onMenuClicked = {
                    //TODO
                })


            val emailAccount by viewModel.accountStateFlow.collectAsState()
            NewMailSenderField(emailAccount)
            NewMailDivider()
            var expanded by remember { mutableStateOf(false) }
            NewMailDestinationFields(
                destination = newMailUiState.destination,
                onDestinationChanged = {
                    viewModel.updateDestination(it)
                },
                cc = newMailUiState.cc,
                onCcChanged = {
                    viewModel.updateCc(it)
                },
                bcc = newMailUiState.bcc,
                onCciChanged = {
                    viewModel.updateBcc(it)
                },
                expanded = expanded,
                onExpand = {
                    expanded = it
                })

            NewMailSubjectField(
                subject = newMailUiState.subject,
                onSubjectChanged = {
                    viewModel.updateSubject(it)
                }, onFocused = {
                    if (expanded && (newMailUiState.cc.isEmpty() || newMailUiState.bcc.isEmpty())) expanded = false
                })
            NewMailDivider()
            NewMailContent(content = newMailUiState.content, onContentChanged = {
                viewModel.updateContent(it)
            }, onFocused = {
                if (expanded && (newMailUiState.cc.isEmpty() || newMailUiState.bcc.isEmpty())) expanded = false
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMailTopBar(
    onBackClicked: () -> Unit = {},
    onSendClicked: () -> Unit = {},
    onMenuClicked: () -> Unit = {},
) {
    TopAppBar(title = {}, navigationIcon = {
        IconButton(onClick = onBackClicked) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back_button),
            )
        }

    }, actions = {
        IconButton(onClick = { /*TODO : Not supported yet*/ }) {
            Icon(
                imageVector = Icons.Default.Attachment,
                contentDescription = "",
            )
        }

        IconButton(onClick = onSendClicked) {
            Icon(
                imageVector = Icons.Outlined.Send,
                contentDescription = "",
            )
        }

        IconButton(onClick = onMenuClicked) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "",
            )
        }
    })
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun NewMailScreenPreview() {
    GmailTheme {
        NewMailScreen()
    }
}

@Composable
fun NewMailDestinationFields(
    destination: String,
    onDestinationChanged: (String) -> Unit = {},
    cc: String,
    onCcChanged: (String) -> Unit = {},
    bcc: String,
    onCciChanged: (String) -> Unit = {},
    expanded: Boolean,
    onExpand: (Boolean) -> Unit = {}
) {


    Row(modifier = Modifier.padding(15.dp)) {
        Text(text = stringResource(id = R.string.new_mail_to))
        Spacer(modifier = Modifier.width(20.dp))
        BasicTextField(
            value = destination,
            onValueChange = onDestinationChanged,
            textStyle = TextStyle.Default.copy(fontSize = 16.sp),
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )
        if (!expanded) {
            Icon(imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "",
                modifier = Modifier.clickable {
                    onExpand(true)
                })
        }
    }
    NewMailDivider()

    if (expanded || cc.isNotEmpty()) {
        Row(modifier = Modifier.padding(15.dp)) {
            Text(text = stringResource(id = R.string.new_mail_cc))
            Spacer(modifier = Modifier.width(20.dp))
            BasicTextField(
                value = cc,
                onValueChange = onCcChanged,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle.Default.copy(fontSize = 16.sp)
            )
        }
        NewMailDivider()
    }
    if (expanded || bcc.isNotEmpty()) {
        Row(modifier = Modifier.padding(15.dp)) {
            Text(text = stringResource(id = R.string.new_mail_bcc))
            Spacer(modifier = Modifier.width(20.dp))
            BasicTextField(
                value = bcc,
                onValueChange = onCciChanged,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle.Default.copy(fontSize = 16.sp)
            )
        }
        NewMailDivider()
    }
}

@Composable
private fun NewMailSubjectField(
    subject: String,
    onSubjectChanged: (String) -> Unit = {},
    onFocused: () -> Unit = {}
) {
    TextField(
        value = subject,
        onValueChange = onSubjectChanged,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.hasFocus) {
                    onFocused()
                }
            },
        textStyle = TextStyle.Default.copy(fontSize = 18.sp),
        placeholder = {
            Text(text = stringResource(id = R.string.new_mail_subject))
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun NewMailContent(
    content: String,
    onContentChanged: (String) -> Unit = {},
    onFocused: () -> Unit = {}
) {

    TextField(
        value = content,
        onValueChange = onContentChanged,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.hasFocus) {
                    onFocused()
                }
            },
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        placeholder = {
            Text(text = stringResource(id = R.string.new_mail_compose_email))
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun NewMailSenderField(emailAccount: EmailAccount?) {
    Row(modifier = Modifier.padding(15.dp)) {
        Text(text = stringResource(id = R.string.new_mail_from))
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = emailAccount?.address ?: "")
    }
}

@Composable
fun NewMailDivider() {
    Divider(modifier = Modifier.padding(vertical = 7.dp), thickness = 0.5.dp)
}