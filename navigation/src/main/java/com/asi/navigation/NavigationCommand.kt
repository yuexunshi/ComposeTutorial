package com.asi.navigation

import androidx.navigation.NavOptions


/**
 * @ClassName Navigator.java
 * @author usopp
 * @version 1.0.0
 * @Description 导航命令
 * @createTime 2022年09月21日 17:10:00
 */

sealed class NavigationCommand {

    data class To(val route: String, val options: NavOptions? = null) :
        NavigationCommand()

    data class OffAllTo(
        val route: String,
    ) : NavigationCommand()

    data class OffUntil(
        val route: String,
        val inclusive: Boolean,
        val saveState: Boolean = false,
    ) : NavigationCommand()

    data class PopUpTo(
        val route: String,
        val inclusive: Boolean,
        val saveState: Boolean = false,
    ) : NavigationCommand()

    object Back : NavigationCommand()

    data class OffUntilTo(
        val route: String,
        val untilRoute: String,
        val inclusive: Boolean,
        val saveState: Boolean = false,
    ) : NavigationCommand()

}
