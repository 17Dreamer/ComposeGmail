package com.dreamtech.compose.gmail.feature.signin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dreamtech.compose.gmail.feature.setup.setUpEmailNavigationRoute

const val sinInNavigationRoute = "signIn"


fun NavController.navigateToSignInScreen(navOptions: NavOptions? = null) {
    this.navigate(sinInNavigationRoute, navOptions)
}

fun NavGraphBuilder.signInScreen(navigateToMainScreen: () -> Unit) {
    composable(
        route = sinInNavigationRoute,
    ) {
        SignInScreen{
            navigateToMainScreen()
        }
    }
}