package com.asi.navigation

import androidx.navigation.NavController

/**
 * @ClassName RouterController.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月22日 17:45:00
 */

fun handleNavigationCommands(navController: NavController) {
    navController.handleComposeNavigationCommand(Router.destination.value)
}

private fun NavController.handleComposeNavigationCommand(navigationCommand: NavigationCommand) {
    when (navigationCommand) {
        is NavigationCommand.To -> navigate(navigationCommand.route, navigationCommand.options)

        is NavigationCommand.PopUpTo -> navigate(
            navigationCommand.route,
        ) {
            currentBackStackEntry?.destination?.route?.let {
                popBackStack()
            }
        }
        is NavigationCommand.Back -> popBackStack()
        is NavigationCommand.OffUntilTo -> navigate(navigationCommand.route) {
            Router.popUpTo(navigationCommand.untilRoute,
                inclusive = navigationCommand.inclusive)
        }

        is NavigationCommand.OffAllTo -> navigate(navigationCommand.route) {
            popUpTo(0)
        }
        is NavigationCommand.OffUntil -> popBackStack(route = navigationCommand.route,
            false,
            navigationCommand.saveState)
        Router.Default -> {
        }
    }
}
