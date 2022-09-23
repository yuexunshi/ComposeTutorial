package com.asi.navsample.page

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import com.asi.navigation.Nav
import com.asi.navsample.nav.Screen

/**
 * @ClassName OneScreen.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月21日 11:00:00
 */

@Composable
fun OneScreen() {
    BackHandler(onBack = {
        Log.e("==", "OneScreen:BackHandler ")
    })
    SideEffect {
        Log.e("==", "SideEffect ")
    }

    LaunchedEffect(Unit) {
        Log.e("==", "LaunchedEffect ")
    }
    Column {
        Text(text = "OneScreen")

        Button(onClick = {
            Nav.to(Screen.Two.route)
        }) {
            Text(text = "去TwoScreen")
        }
    }
}

