package com.datum.client.ui.page.dataset_control

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.service.BusinessLogicService
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ManagementOption
import com.datum.client.ui.custom.Separator
import com.datum.client.ui.page.dataset_meta.DatasetMetaNavHelper
import com.datum.client.ui.page.dataset_meta.DatasetMetaPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatasetControlPage(n: NavController, b: NavBackStackEntry): Page(n, b) {

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {

        Column {
            Text(text = "Dataset control", fontSize = 24.sp)
            val scope = rememberCoroutineScope()
            ManagementOption(optionString = "Dataset meta") {
                scope.launch {
                    val data = withContext(Dispatchers.IO) {
                        BusinessLogicService.instance.getDatasetMeta()
                    }
                    navController.navigate(DatasetMetaNavHelper().substituteArgument(data))
                }

            }
            Separator(color = Color.Gray)
            ManagementOption(optionString = "Image classes") {
                
            }
            /*Separator(color = Color.Gray)
            ManagementOption(optionString = "View dataset") {

            }*/
            Separator(color = Color.Gray)
            ManagementOption(optionString = "Generate archive") {

            }
        }
    }

}