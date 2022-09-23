package com.asi.navigation

import androidx.navigation.NavOptions

/**
 * @ClassName Router.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月22日 17:13:00
 */


object Nav : INav {

    private fun navigate(destination: RouterCommand) {
        NavFlow.navigate(destination)
    }


    override fun to(name: String, option: NavOptions?) {
        navigate(RouterCommand.To(name, option))
    }


    override fun popUpTo(
        name: String,
    ) {
        navigate(RouterCommand.PopUpTo(name))
    }

    override fun back() {
        navigate(RouterCommand.Back)
    }

    override fun offUntilTo(
        route: String,
        untilRoute: String,
        inclusive: Boolean,
        saveState: Boolean,
    ) {
        navigate(RouterCommand.OffUntilTo(route, untilRoute, inclusive, saveState))
    }


    override fun offUntil(
        untilRoute: String,
        inclusive: Boolean,
        saveState: Boolean,
    ) {
        navigate(RouterCommand.OffUntil(untilRoute, inclusive, saveState))
    }

    override fun offAllTo(route: String) {
        navigate(RouterCommand.OffAllTo(route))
    }


}
