package com.dreamtech.compose.gmail.feature.newmail

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dreamtech.compose.gmail.core.domain.model.Message

private const val navArgMessage = "message"
private const val navArgEntranceType = "EntranceType"
private const val newMailNavigationRoute = "newMail/{$navArgEntranceType}/{$navArgMessage}"


fun NavController.navigateToNewMailScreen(
    entranceType: EntranceType = EntranceType.New,
    message: String = " ",
    navOptions: NavOptions? = null
) {
    this.navigate("newMail/${entranceType.value}/$message", navOptions)
}

fun NavGraphBuilder.newMailScreen(onBackClick: () -> Unit = {}) {
    composable(
        route = newMailNavigationRoute,
        arguments = listOf(
            navArgument(navArgEntranceType) { type = NavType.StringType },
            navArgument(navArgMessage) { type = NavType.StringType },
        ),
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
        val viewModel: NewMailViewModel = hiltViewModel()
        NewMailScreen(viewModel = viewModel, onBackClick = onBackClick)
    }
}

sealed class EntranceType(val value: String){
    object New : EntranceType("New")
    object Reply : EntranceType("Reply")
    object ReplyAll : EntranceType("ReplyAll")
    object Forward : EntranceType("Forward")
}