package com.asi.navsample.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.asi.navigation.Nav
import com.asi.navsample.model.User
import com.asi.navsample.nav.FourDestination
import com.asi.navsample.nav.OneDestination


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
            Nav.offUntil(OneDestination.route)
        }) {
            Text(text = "回到OneScreen")
        }
        Button(onClick = {
            Nav.to(FourDestination.createRoute(User("来自Three", "110")))
        }) {
            Text(text = "去FourScreen")
        }
        Button(onClick = {
            Nav.popUpTo(FourDestination.createRoute(User("replace来自Three", "110")))
        }) {
            Text(text = "replaceFourScreen")
        }


    }
}