package com.asi.navsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import com.asi.navigation.RouterHost
import com.asi.navsample.model.User
import com.asi.navsample.nav.Screen
import com.asi.navsample.page.FourScreen
import com.asi.navsample.page.OneScreen
import com.asi.navsample.page.ThreeScreen
import com.asi.navsample.page.TwoScreen
import com.asi.navsample.ui.theme.ComposeTutorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTutorialTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    RouterHost(Screen.One.route) {
                        composable(Screen.One.route) { OneScreen() }
                        composable(Screen.Two.route) { TwoScreen() }
                        composable(Screen.Four.route, arguments = Screen.Four.arguments) {
                            val user = it.arguments?.getParcelable<User>(Screen.Four.ARG)
                                ?: return@composable
                            FourScreen(user)
                        }
                        composable(Screen.Three.route, arguments = Screen.Three.arguments) {
                            val channelId =
                                it.arguments?.getString(Screen.Three.ARG) ?: return@composable
                            ThreeScreen(channelId)
                        }
                    }
                }
            }
        }
    }
}

