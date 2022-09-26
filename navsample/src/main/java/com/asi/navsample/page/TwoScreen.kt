package com.asi.navsample.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.asi.navigation.Nav
import com.asi.navsample.nav.ThreeDestination


/**
 * @ClassName OneScreen.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月21日 11:00:00
 */

@Composable
fun TwoScreen() {

    Column() {
        Text(text = "TwoScreen")

        Button(onClick = {
            Nav.to(ThreeDestination.createRoute("来自Two"))
        }) {
            Text(text = "去ThreeScreen")
        }
        Button(onClick = {
            Nav.popUpTo(ThreeDestination.createRoute("replace来自Two"))
        }) {
            Text(text = "replace去ThreeScreen")
        }
    }

}