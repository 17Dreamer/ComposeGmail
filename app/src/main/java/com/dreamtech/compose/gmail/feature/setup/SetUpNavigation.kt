package com.dreamtech.compose.gmail.feature.setup

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.dreamtech.compose.gmail.R

const val setUpEmailNavigationRoute = "set_up_email"


fun NavController.navigateToSetUpScreen(navOptions: NavOptions? = null) {
    this.navigate(setUpEmailNavigationRoute, navOptions)
}

fun NavGraphBuilder.setUpScreen(navigateToSignInScreen: () -> Unit) {
    composable(
        route = setUpEmailNavigationRoute,
    ) {
        val context = LocalContext.current
        SetUpEmailScreen {
            if (it is EmailAccountType.Google) {
                navigateToSignInScreen()
            } else {
                Toast.makeText(
                    context, R.string.not_available, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
