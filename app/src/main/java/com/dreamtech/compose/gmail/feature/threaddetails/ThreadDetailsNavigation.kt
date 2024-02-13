package com.dreamtech.compose.gmail.feature.threaddetails

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dreamtech.compose.gmail.core.domain.model.Message

private const val navArgThreadId = "threadId"
private const val threadDetailsNavigationRoute = "threadDetails/{$navArgThreadId}"


fun NavController.navigateToThreadDetailsScreen(threadId: String, navOptions: NavOptions? = null) {
    this.navigate("threadDetails/$threadId", navOptions)
}

fun NavGraphBuilder.threadDetailsScreen(
    onReplyClick: (Message) -> Unit = {},
    onReplyAllClick: (Message) -> Unit = {},
    onForwardClick: (Message) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    composable(
        route = threadDetailsNavigationRoute,
        arguments = listOf(
            navArgument(navArgThreadId) { type = NavType.StringType },
        )
    ) {
        val viewModel: ThreadDetailsViewModel = hiltViewModel()
        ThreadDetailsScreen(
            viewModel = viewModel,
            onReplyClick = onReplyClick,
            onReplyAllClick = onReplyAllClick,
            onForwardClick = onForwardClick,
            onBackClick = onBackClick
        )
    }
}