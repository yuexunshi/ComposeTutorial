package com.asi.navigation

import androidx.navigation.NavOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @ClassName Router.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月22日 17:13:00
 */


object Router : IRouter {

    internal object Default : NavigationCommand()

    private var destinationFlow: MutableStateFlow<NavigationCommand> =
        MutableStateFlow(Default)

    var destination = destinationFlow.asStateFlow()

    private fun navigate(destination: NavigationCommand) {
        this.destinationFlow.value = destination
    }


    override fun to(name: String, option: NavOptions?) {
        navigate(NavigationCommand.To(name, option))
    }


    override fun popUpTo(
        name: String, inclusive: Boolean, saveState: Boolean,
    ) {

        navigate(NavigationCommand.PopUpTo(name, inclusive))
    }

    override fun back() {
        navigate(NavigationCommand.Back)
    }

    override fun offUntilTo(
        route: String,
        untilRoute: String,
        inclusive: Boolean,
        saveState: Boolean,
    ) {
        navigate(NavigationCommand.OffUntilTo(route, untilRoute, inclusive, saveState))
    }


    override fun offUntil(
        untilRoute: String,
        inclusive: Boolean,
        saveState: Boolean,
    ) {
        navigate(NavigationCommand.OffUntil(untilRoute, inclusive, saveState))
    }

    override fun offAllTo(route: String) {
        navigate(NavigationCommand.OffAllTo(route))
    }


}
