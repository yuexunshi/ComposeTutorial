package com.asi.navsample.nav

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.asi.navigation.Destination
import com.asi.navsample.model.NavUserType
import com.asi.navsample.model.User

/**
 * @ClassName N.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月23日 14:22:00
 */

object OneDestination : Destination("one")
object TwoDestination : Destination("two")

object ThreeDestination : Destination("three",
    listOf(navArgument("channelId") { type = NavType.StringType })) {
    const val ARG = "channelId"
    fun createRoute(str: String): String {
        return route.replace("{${arguments.first().name}}", str)
    }
}


object FourDestination : Destination("four", listOf(
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

object FiveDestination : Destination("five",
    listOf(navArgument("age") { type = NavType.IntType },
        navArgument("name") { type = NavType.StringType })) {
    const val ARG_AGE = "age"
    const val ARG_NAME = "name"
    fun createRoute(age: Int, name: String): String {
        return route.replace("{${arguments.first().name}}", "$age")
            .replace("{${arguments.last().name}}", name)
    }
}
//sealed class Screen(
//    path: String,
//    val arguments: List<NamedNavArgument> = emptyList(),
//) {
//    val route: String = path.appendArguments(arguments)
//
//    object One : Screen("one")
//    object Two : Screen("two")
//    object Four : Screen("four", listOf(
//        navArgument("user") {
//            type = NavUserType()
//            nullable = false
//        }
//    )) {
//        const val ARG = "user"
//        fun createRoute(user: User): String {
//            return route.replace("{${arguments.first().name}}", user.toString())
//        }
//    }
//
//    object Three : Screen("three",
//        listOf(navArgument("channelId") { type = NavType.StringType })) {
//        const val ARG = "channelId"
//        fun createRoute(str: String): String {
//            return route.replace("{${arguments.first().name}}", str)
//        }
//    }
//}

