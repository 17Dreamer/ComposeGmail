package com.dreamtech.compose.gmail.feature.signin

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamtech.compose.gmail.R
import com.dreamtech.compose.gmail.feature.signin.component.EmailInput
import com.dreamtech.compose.gmail.feature.signin.component.EmailState
import com.dreamtech.compose.gmail.feature.signin.component.EmailStateSaver
import com.dreamtech.compose.gmail.feature.signin.component.TextFieldState
import com.dreamtech.compose.gmail.ui.theme.GmailTheme

@Composable
fun SignInScreen(goToMainScreen: () -> Unit = {}) {

    val viewModel : SignInViewModel =  hiltViewModel()

    val onConfirmed = { email: String, password: String ->
        viewModel.signIn(email, password){
            goToMainScreen()
        }
    }

    val emailState by rememberSaveable(stateSaver = EmailStateSaver) {
        mutableStateOf(EmailState())
    }

    var password by remember { mutableStateOf("") }

    var emailConfirmed by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp), horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.glogo),
                contentDescription = "",
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = stringResource(id = R.string.screen_title_signin),
                modifier = Modifier.padding(vertical = 15.dp),
                fontSize = 34.sp,
                fontWeight = FontWeight.Normal
            )
            Text(text = stringResource(id = R.string.signin_help), fontSize = 18.sp)
            val onSubmit = {
                if (emailState.isValid) {
                    emailConfirmed = true
                } else {
                    emailState.enableShowErrors()
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {

                AnimatedContent(
                    targetState = emailConfirmed,
                    transitionSpec = {
                        // Compare the incoming number with the previous number.
                        if (emailConfirmed) {
                            // If the target number is larger, it slides up and fades in
                            // while the initial (smaller) number slides up and fades out.
                            (slideInHorizontally { height -> height } + fadeIn()).togetherWith(
                                slideOutHorizontally { height -> -height } + fadeOut())
                        } else {
                            // If the target number is smaller, it slides down and fades in
                            // while the initial number slides down and fades out.
                            (slideInHorizontally { height -> -height } + fadeIn()).togetherWith(
                                slideOutHorizontally { height -> height } + fadeOut())
                        }.using(
                            // Disable clipping since the faded slide-in/out should
                            // be displayed out of bounds.
                            SizeTransform(clip = false)
                        )
                    }, label = ""
                ) { confirmed ->
                    if (!confirmed) {
                        EmailField(emailState) {
                            onSubmit()
                        }
                    } else {
                        PasswordField(password = password, onValueChanged = { password = it }) {
                            onConfirmed(emailState.text, password)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.signin_action_create_account),
                    modifier = Modifier.padding(vertical = 15.dp),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Medium
                )
                Button(onClick = {
                    if(!emailConfirmed) {
                        onSubmit()
                    }else{
                        onConfirmed(emailState.text, password)
                    }
                }) {
                    Text(text = stringResource(id = R.string.global_action_next), fontSize = 14.sp)
                }
            }
        }

    }
}

@Composable
private fun EmailField(
    emailState: TextFieldState,
    onSubmit: () -> Unit
) {
    Column {
        EmailInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 8.dp),
            emailState = emailState,
            hint = stringResource(id = R.string.signin_login_hint),
            imeAction = ImeAction.Done,
            onImeAction = onSubmit
        )
        Text(
            text = stringResource(id = R.string.signin_action_forgot_email),
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PasswordField(
    password: String,
    onValueChanged: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column {
        var isChecked by remember { mutableStateOf(false) }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, bottom = 8.dp),
            value = password,
            onValueChange = onValueChanged,
            label = {
                Text(
                    text = stringResource(id = R.string.signin_password_hint),
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSubmit()
                }
            ),
            visualTransformation = if (isChecked) VisualTransformation.None else PasswordVisualTransformation(),

            )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChecked, onCheckedChange = { isChecked = !isChecked })
            Text(
                text = stringResource(id = R.string.signin_action_show_password),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    GmailTheme {
        SignInScreen()
    }
}

@Preview(showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SignInScreenDarkPreview() {
    GmailTheme {
        SignInScreen()
    }
}