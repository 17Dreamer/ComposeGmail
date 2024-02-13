package com.dreamtech.compose.gmail.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dreamtech.compose.gmail.R

@Composable
fun SettingsScreen(onBackClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            SettingsTopBar(title = stringResource(id = R.string.setting_screen_title), onBackClick = onBackClick)
            Spacer(modifier = Modifier.height(5.dp))
            SettingItem(text = stringResource(id = R.string.setting_item_general_settings))
            SettingItem(text = "aissiyine.atef@gmail.com")
            SettingItem(text = stringResource(id = R.string.setting_item_add_account))
        }

    }
}

@Composable
fun SettingItem(text: String) {
    Text(text = text, modifier = Modifier.padding(horizontal = 25.dp, vertical = 12.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(title: String, onMenuClick: () -> Unit = {}, onBackClick: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button),
                )
            }
        },
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "",
                )
            }
        })
}
