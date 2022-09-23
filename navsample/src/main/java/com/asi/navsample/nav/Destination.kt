package com.asi.navsample.nav

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.asi.navsample.model.NavUserType
import com.asi.navsample.model.User

/**
 * @ClassName N.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月23日 14:22:00
 */


sealed class Screen(
    path: String,
    val arguments: List<NamedNavArgument> = emptyList(),
) {
    val route: String = path.appendArguments(arguments)

    object One : Screen("one")
    object Two : Screen("two")
    object Four : Screen("four", listOf(
        navArgument("user") {
            type = NavUserType()
            nullable = false
        }
    )) {
        const val ARG = "user"
        fun createRoute(user: User): String {
            return route.replace("{${arguments.first().name}}", user.toString())
        }
    }

    object Three : Screen("three",
        listOf(navArgument("channelId") { type = NavType.StringType })) {
        const val ARG = "channelId"
        fun createRoute(str: String): String {
            return route.replace("{${arguments.first().name}}", str)
        }
    }
}

private fun String.appendArguments(navArguments: List<NamedNavArgument>): String {
    val mandatoryArguments = navArguments.filter { it.argument.defaultValue == null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "/", prefix = "/") { "{${it.name}}" }
        .orEmpty()
    val optionalArguments = navArguments.filter { it.argument.defaultValue != null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "&", prefix = "?") { "${it.name}={${it.name}}" }
        .orEmpty()
    return "$this$mandatoryArguments$optionalArguments"
}