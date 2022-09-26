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
import com.asi.navsample.nav.*
import com.asi.navsample.page.*
import com.asi.navsample.ui.theme.ComposeTutorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTutorialTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    RouterHost(OneDestination.route) {
                        composable(OneDestination.route) { OneScreen() }
                        composable(TwoDestination.route) { TwoScreen() }
                        composable(FourDestination.route, arguments = FourDestination.arguments) {
                            val user = it.arguments?.getParcelable<User>(FourDestination.ARG)
                                ?: return@composable
                            FourScreen(user)
                        }
                        composable(ThreeDestination.route, arguments = ThreeDestination.arguments) {
                            val channelId =
                                it.arguments?.getString(ThreeDestination.ARG) ?: return@composable
                            ThreeScreen(channelId)
                        }

                        composable(FiveDestination.route, arguments = FiveDestination.arguments) {
                            val age =
                                it.arguments?.getInt(FiveDestination.ARG_AGE) ?: return@composable
                            val name =
                                it.arguments?.getString(FiveDestination.ARG_NAME)
                                    ?: return@composable
                            FiveScreen(age, name)
                        }
                    }
                }
            }
        }
    }
}

