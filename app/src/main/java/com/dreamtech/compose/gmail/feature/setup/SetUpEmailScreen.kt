package com.dreamtech.compose.gmail.feature.setup

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamtech.compose.gmail.R
import com.dreamtech.compose.gmail.ui.theme.GmailTheme

@Composable
fun SetUpEmailScreen(onAccountClicked: (EmailAccountType) -> Unit = {}) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp), horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "",
                modifier = Modifier
                    .size(38.dp)
                    .padding(5.dp)
                    .background(color = Color.White, shape = CircleShape),
            )
            Text(
                text = stringResource(id = R.string.screen_title_setup_email),
                modifier = Modifier.padding(vertical = 15.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(20.dp))
            Divider()
            SetUpAccountItem(R.drawable.glogo, R.string.account_google) {
                onAccountClicked(EmailAccountType.Google)
            }
            Divider()
            SetUpAccountItem(
                R.drawable.microsoft_outlook_logo, R.string.account_microsoft
            ) { onAccountClicked(EmailAccountType.Outlook) }
            Divider()
            SetUpAccountItem(R.drawable.yahoo_logo, R.string.account_yahoo) {
                onAccountClicked(
                    EmailAccountType.Yahoo
                )
            }
            Divider()
            SetUpAccountItem(R.drawable.office_exchange_logo, R.string.account_office) {
                onAccountClicked(
                    EmailAccountType.Exchange
                )
            }
            Divider()
            SetUpAccountItem(R.drawable.other_app_logo, R.string.account_other) {
                onAccountClicked(
                    EmailAccountType.Other
                )
            }
            Divider()
        }
    }
}

@Composable
fun SetUpAccountItem(
    icon: Int, label: Int, modifier: Modifier = Modifier, onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = label),
            modifier = Modifier.size(25.dp)
        )
        Text(
            text = stringResource(id = label),
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SetUpEmailScreenPreview() {
    GmailTheme {
        SetUpEmailScreen()
    }
}

@Preview(showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SetUpEmailScreenDarkPreview() {
    GmailTheme {
        SetUpEmailScreen()
    }
}

sealed interface EmailAccountType {
    object Google : EmailAccountType
    object Outlook : EmailAccountType
    object Yahoo : EmailAccountType
    object Exchange : EmailAccountType
    object Other : EmailAccountType
}