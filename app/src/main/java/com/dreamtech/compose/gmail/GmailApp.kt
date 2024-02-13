package com.dreamtech.compose.gmail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dreamtech.compose.gmail.feature.newmail.EntranceType
import com.dreamtech.compose.gmail.feature.newmail.navigateToNewMailScreen
import com.dreamtech.compose.gmail.feature.newmail.newMailScreen
import com.dreamtech.compose.gmail.feature.settings.navigateToSettingsScreen
import com.dreamtech.compose.gmail.feature.settings.settingsScreen
import com.dreamtech.compose.gmail.feature.setup.navigateToSetUpScreen
import com.dreamtech.compose.gmail.feature.setup.setUpEmailNavigationRoute
import com.dreamtech.compose.gmail.feature.setup.setUpScreen
import com.dreamtech.compose.gmail.feature.signin.navigateToSignInScreen
import com.dreamtech.compose.gmail.feature.signin.signInScreen
import com.dreamtech.compose.gmail.feature.threaddetails.navigateToThreadDetailsScreen
import com.dreamtech.compose.gmail.feature.threaddetails.threadDetailsScreen
import com.dreamtech.compose.gmail.feature.mainscreen.mainNavigationRoute
import com.dreamtech.compose.gmail.feature.mainscreen.mainScreen
import com.dreamtech.compose.gmail.feature.mainscreen.navigateToMainScreen

@Composable
fun GmailApp(modifier: Modifier = Modifier, isLoggedIn: Boolean = false) {


    val startDestination = if (isLoggedIn) mainNavigationRoute else setUpEmailNavigationRoute

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        setUpScreen(
            navigateToSignInScreen = { navController.navigateToSignInScreen() }
        )

        signInScreen(
            navigateToMainScreen = { navController.navigateToMainScreen() }
        )

        mainScreen(
            navigateToNewMail = { navController.navigateToNewMailScreen() },
            navigateToThreadDetails = { navController.navigateToThreadDetailsScreen(it) },
            navigateToAccountSetUp = { navController.navigateToSetUpScreen() },
            navigateToSettings = { navController.navigateToSettingsScreen() }
        )

        newMailScreen(
            onBackClick = navController::popBackStack
        )

        threadDetailsScreen(
            onReplyClick = {
                navController.navigateToNewMailScreen(
                    entranceType = EntranceType.Reply,
                    message = it.id
                )
            },
            onReplyAllClick = {
                navController.navigateToNewMailScreen(
                    entranceType = EntranceType.ReplyAll,
                    message = it.id
                )
            },
            onForwardClick = {
                navController.navigateToNewMailScreen(
                    entranceType = EntranceType.Forward,
                    message = it.id
                )
            },
            onBackClick = navController::popBackStack
        )

        settingsScreen(
            onBackClick = navController::popBackStack
        )
    }

}