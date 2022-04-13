package com.datum.client.ui.page.dataset_control

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ManagementOption
import com.datum.client.ui.custom.Separator

class DatasetControlPage(n: NavController, b: NavBackStackEntry): Page(n, b) {

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {

        Column() {
            Text(text = "Dataset control", fontSize = 24.sp)
            ManagementOption(optionString = "Dataset meta") {

            }
            Separator(color = Color.Gray)
            ManagementOption(optionString = "Image classes") {
                
            }
            Separator(color = Color.Gray)
            ManagementOption(optionString = "View dataset") {

            }
            Separator(color = Color.Gray)
            ManagementOption(optionString = "Generate archive") {

            }
        }
    }

}