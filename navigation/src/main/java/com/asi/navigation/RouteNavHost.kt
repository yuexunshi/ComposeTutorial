package com.asi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

/**
 * @ClassName RouteNavHost.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月22日 17:48:00
 */

@Composable
fun RouteNavHost(
    startDestination: String, builder: NavGraphBuilder.() -> Unit,
) {
    val navController = rememberNavController()
    val destination by Router.destination.collectAsState()
    LaunchedEffect(destination) {
        handleNavigationCommands(navController)
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = builder
    )
}