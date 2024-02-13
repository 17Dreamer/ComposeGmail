package com.dreamtech.compose.gmail.feature.mainscreen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val mainNavigationRoute = "main_screen"


fun NavController.navigateToMainScreen(navOptions: NavOptions? = null) {
    this.navigate(mainNavigationRoute, navOptions)
}

fun NavGraphBuilder.mainScreen(
    navigateToNewMail: () -> Unit,
    navigateToThreadDetails: (String) -> Unit,
    navigateToAccountSetUp: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    composable(
        route = mainNavigationRoute,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(400)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(400)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(400)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(400)
            )
        }
    ) {
        MainGmailScreen(
            openNewMail = navigateToNewMail,
            openThread = navigateToThreadDetails,
            openAccountSetUp = navigateToAccountSetUp,
            openSettingsScreen = navigateToSettings
        )

    }
}
