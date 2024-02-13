package com.dreamtech.compose.gmail.feature.threaddetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Forward
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Reply
import androidx.compose.material.icons.outlined.ReplyAll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamtech.compose.gmail.R
import com.dreamtech.compose.gmail.core.domain.model.Message
import com.dreamtech.compose.gmail.feature.mainscreen.ProfileImage
import com.dreamtech.compose.gmail.utils.format
import com.dreamtech.compose.gmail.utils.metadataFormat

@Composable
fun ThreadDetailsScreen(
    viewModel: ThreadDetailsViewModel = hiltViewModel(),
    onReplyClick: (Message) -> Unit = {},
    onReplyAllClick: (Message) -> Unit = {},
    onForwardClick: (Message) -> Unit = {},
    onBackClick: () -> Unit = {}
) {

    val threadUiState by viewModel.threadUiStateFlow.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxHeight()) {
            ThreadDetailsTopBar(onBackClick = onBackClick)
            var isAllVisible: Boolean by remember { mutableStateOf(false) }
            LazyColumn {
                item {
                    ThreadDetailsHeader(title = threadUiState.threadSubject)
                }
                if (threadUiState.messages.isNotEmpty()) {
                    if (threadUiState.messages.size < 4) {
                        isAllVisible = true
                    }
                    if (isAllVisible) {
                        threadUiState.messages.forEachIndexed { index, message ->
                            val isLastItem = index == threadUiState.messages.size - 1
                            threadDetails(
                                message = message,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f),
                                collapsed = threadUiState.messages.size >= 4 || !isLastItem,
                                onReply = {
                                    onReplyClick(message)
                                }
                            )
                            if (!isLastItem) {
                                item {
                                    Divider(thickness = 0.5.dp)
                                }
                            }
                        }
                    } else {

                        val firstMessage = threadUiState.messages.first()
                        threadDetails(
                            message = firstMessage,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            collapsed = true,
                            onReply = {
                                onReplyClick(firstMessage)
                            }
                        )

                        hiddenMessages(
                            count = threadUiState.messages.size - 2,
                            modifier = Modifier.clickable { isAllVisible = true })

                        val lastMessage = threadUiState.messages.last()
                        threadDetails(
                            message = lastMessage,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            collapsed = false,
                            onReply = {
                                onReplyClick(lastMessage)
                            }
                        )

                    }

                    item {
                        val lastMessage = threadUiState.messages.last()
                        ThreadDetailsFooter(
                            onReplyClick = {
                                onReplyClick(lastMessage)
                            },
                            onReplyAllClick = {
                                onReplyAllClick(lastMessage)
                            },
                            onForwardClick = {
                                onForwardClick(lastMessage)
                            }
                        )
                    }

                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadDetailsTopBar(onBackClick: () -> Unit = {}) {
    TopAppBar(title = {}, navigationIcon = {

        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back_button),
            )
        }
    }, actions = {

        IconButton(onClick = {/* TODO */}) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "",
            )
        }

        IconButton(onClick = {/* TODO */}) {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "",
            )
        }

        IconButton(onClick = {/* TODO */}) {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = "",
            )
        }

        IconButton(onClick = {/* TODO */}) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "",
            )
        }
    })
}


@Composable
fun ThreadDetailsHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        ThreadDetailsTitle(
            title, modifier =
            Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.StarOutline,
            modifier = Modifier.padding(8.dp),
            contentDescription = "",
        )
    }
}


@Composable
private fun ThreadDetailsTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier
            .padding(horizontal = 8.dp),
        style = MaterialTheme.typography.headlineSmall,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

fun LazyListScope.threadDetails(
    message: Message,
    modifier: Modifier = Modifier,
    collapsed: Boolean = false,
    onReply: () -> Unit = {}
) {

    item {
        var isContentCollapsed by remember { mutableStateOf(collapsed) }
        var isMetaDataCollapsed by remember { mutableStateOf(true) }
        Column(modifier = modifier) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfileImage(
                    drawableResource = R.drawable.account_avatar, description = "",
                    modifier = Modifier
                        .size(70.dp)
                        .padding(15.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier
                    .weight(1f)
                    .clickable { isContentCollapsed = !isContentCollapsed }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = message.from,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = message.dateTime.format(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                    Row(modifier = Modifier.clickable {
                        isMetaDataCollapsed = !isMetaDataCollapsed
                    }) {
                        Text(
                            text = "${stringResource(id = R.string.thread_details_to)} ${message.destination}",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                    }
                }
                if (!isContentCollapsed) {
                    IconButton(onClick = onReply) {
                        Icon(
                            imageVector = Icons.Outlined.Reply,
                            contentDescription = "",
                        )
                    }

                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "",
                        )
                    }
                }
            }
            if (!isMetaDataCollapsed) {
                MetadataBox(message = message)
            }
            if (!isContentCollapsed) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = message.content
                )
            }
        }
    }
}

@Composable
fun MetadataBox(message: Message) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.large
            )
            .padding(10.dp)
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.thread_details_metadata_from),
                modifier = Modifier.weight(0.1f),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = message.from, modifier = Modifier.weight(0.9f),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        if (message.destination.isNotEmpty()) {
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = stringResource(id = R.string.thread_details_metadata_to),
                    modifier = Modifier.weight(0.1f),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = message.destination,
                    modifier = Modifier.weight(0.9f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        if (message.ccDestination.isNotEmpty()) {
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = stringResource(id = R.string.thread_details_metadata_cc),
                    modifier = Modifier.weight(0.1f),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = message.ccDestination,
                    modifier = Modifier.weight(0.9f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        if (message.bccDestination.isNotEmpty()) {
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = stringResource(id = R.string.thread_details_metadata_bcc),
                    modifier = Modifier.weight(0.1f),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = message.bccDestination,
                    modifier = Modifier.weight(0.9f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = stringResource(id = R.string.thread_details_metadata_date),
                modifier = Modifier.weight(0.1f),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = message.dateTime.metadataFormat(),
                modifier = Modifier.weight(0.9f),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = "",
                modifier = Modifier
                    .size(18.dp)
                    .weight(0.1f),
            )
            Text(
                text = "Standard encryption (TLS).",
                modifier = Modifier.weight(0.9f),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

fun LazyListScope.hiddenMessages(count: Int = 2, modifier: Modifier = Modifier) {
    val size = 35.dp
    item {
        Box(modifier = modifier) {
            Column(modifier = Modifier.height(size), verticalArrangement = Arrangement.Center) {
                Divider(modifier = Modifier.padding(vertical = 3.dp), thickness = 2.dp)
                Divider(modifier = Modifier.padding(vertical = 3.dp), thickness = 2.dp)
            }
            Row {
                Spacer(modifier = Modifier.width(15.dp))
                Surface(
                    modifier = Modifier.size(size),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = count.toString(),
                        modifier = Modifier
                            .size(size)
                            .padding(top = 5.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

        }

    }
}


@Composable
fun ThreadDetailsFooter(
    onReplyClick: () -> Unit = {},
    onReplyAllClick: () -> Unit = {},
    onForwardClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton(
            icon = rememberVectorPainter(Icons.Outlined.Reply),
            text = stringResource(id = R.string.action_reply),
            modifier = Modifier
                .padding(end = 5.dp)
                .weight(0.3f),
            onClick = onReplyClick
        )
        ActionButton(
            icon = rememberVectorPainter(Icons.Outlined.ReplyAll),
            text = stringResource(id = R.string.action_reply_all),
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .weight(0.3f),
            onClick = onReplyAllClick
        )
        ActionButton(
            icon = rememberVectorPainter(Icons.Outlined.Forward),
            text = stringResource(id = R.string.action_forward),
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp)
                .weight(0.3f),
            onClick = onForwardClick
        )
        OutlinedIconButton(
            onClick = {},
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Icon(
                imageVector = Icons.Default.TagFaces,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}