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
    navController.handleComposeNavigationCommand(NavFlow.destination.value)
}

private fun NavController.handleComposeNavigationCommand(navigationCommand: RouterCommand) {
    when (navigationCommand) {
        is RouterCommand.To -> navigate(navigationCommand.route, navigationCommand.options)

        is RouterCommand.PopUpTo -> navigate(
            navigationCommand.route,
        ) {
            currentBackStackEntry?.destination?.route?.let {
                popBackStack()
            }
        }
        is RouterCommand.Back -> popBackStack()
        is RouterCommand.OffUntilTo -> navigate(navigationCommand.route) {
            popBackStack(navigationCommand.untilRoute,
                inclusive = navigationCommand.inclusive)
        }

        is RouterCommand.OffAllTo -> navigate(navigationCommand.route) {
            popUpTo(0)
        }
        is RouterCommand.OffUntil -> popBackStack(route = navigationCommand.route,
            navigationCommand.inclusive,
            navigationCommand.saveState)
        NavFlow.Default -> {}
    }
}
