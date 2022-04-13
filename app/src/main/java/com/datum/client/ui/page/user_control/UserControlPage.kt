package com.datum.client.ui.page.user_control

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ManagementOption
import com.datum.client.ui.custom.Separator

class UserControlPage(n: NavController, b: NavBackStackEntry): Page(n, b) {

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {
        Column(){
            ManagementOption(optionString = "Create user") {
                
            }
            Separator(color = Color.Gray)
            ManagementOption(optionString = "Inspect users") {

            }
        }
    }
}