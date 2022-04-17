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
import com.datum.client.service.BusinessLogicService
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ManagementOption
import com.datum.client.ui.custom.ProgressIndicator
import com.datum.client.ui.custom.Separator
import com.datum.client.ui.page.dataset_control.DatasetControlNavHelper
import com.datum.client.ui.page.user_list.UserListNavHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserPage(n: NavController, b: NavBackStackEntry): Page(n, b) {
    companion object {
        const val PATH = "user-page"
    }

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {

        val scope = rememberCoroutineScope()

        Column {
            val currentScope = rememberCoroutineScope()
            Text("Project control", fontSize = 24.sp)
            ManagementOption(optionString = "Manage users") {
                scope.launch {
                    val data = withContext(Dispatchers.IO) {
                        ProgressIndicator.blockOperation {
                            BusinessLogicService.instance.getUserList()
                        }
                    }
                    navController.navigate(UserListNavHelper().substituteArgument(data))
                }

            }
            Separator(color = Color.Gray)
            ManagementOption(optionString = "Manage dataset") {
                navController.navigate(DatasetControlNavHelper().substituteArgument())
            }
        }
    }
}