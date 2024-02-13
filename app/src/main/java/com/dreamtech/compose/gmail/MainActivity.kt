package com.dreamtech.compose.gmail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dreamtech.compose.gmail.ui.theme.GmailTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var isLoading by mutableStateOf(true)
        var isLoggedIn by mutableStateOf(false)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        if(it is MainUiState.Success){
                            isLoggedIn = it.isLoggedIn
                            isLoading = false
                        }
                    }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            isLoading
        }

        setContent {
            GmailTheme {
                if(!isLoading)
                GmailApp(isLoggedIn = isLoggedIn)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GmailTheme {
        GmailApp()
    }
}