package com.dreamtech.compose.gmail.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val settingsNavigationRoute = "settings"


fun NavController.navigateToSettingsScreen(navOptions: NavOptions? = null) {
    this.navigate(settingsNavigationRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(onBackClick: () -> Unit = {}) {
    composable(
        route = settingsNavigationRoute,
    ) {

        SettingsScreen(onBackClick = onBackClick)
    }
}
