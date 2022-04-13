package com.datum.client.ui.page.user

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ManagementOption
import com.datum.client.ui.custom.Separator
import com.datum.client.ui.page.dataset_control.DatasetControlNavHelper
import com.datum.client.ui.page.user_control.UserControlNavHelper
import kotlinx.coroutines.launch

class UserPage(n: NavController, b: NavBackStackEntry): Page(n, b) {
    companion object {
        const val PATH = "user-page"
    }

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {
        Column {
            val currentScope = rememberCoroutineScope()
            Text("Project control", fontSize = 24.sp)
            ManagementOption(optionString = "Manage users") {
                navController.navigate(UserControlNavHelper().substituteArgument())

            }
            Separator(color = Color.Gray)
            ManagementOption(optionString = "Manage dataset") {
                navController.navigate(DatasetControlNavHelper().substituteArgument())
            }
        }
    }
}