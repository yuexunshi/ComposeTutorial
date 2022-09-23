package com.asi.navsample.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.asi.navigation.Nav
import com.asi.navsample.model.User
import com.asi.navsample.nav.Screen


/**
 * @ClassName OneScreen.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月21日 11:00:00
 */

@Composable
fun ThreeScreen(id: String) {

    Column() {
        Text(text = "ThreeScreen")
        Text(text = "id=$id")
        Button(onClick = {
            Nav.offUntil(Screen.One.route)
        }) {
            Text(text = "回到OneScreen")
        }
        Button(onClick = {
            Nav.popUpTo(Screen.Four.createRoute(User("拜登", "110")))
        }) {
            Text(text = "去FourScreen")
        }

    }
}