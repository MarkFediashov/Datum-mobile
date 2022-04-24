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
import com.datum.client.dto.DatasetImageClassDto
import com.datum.client.service.BusinessLogicService
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ManagementOption
import com.datum.client.ui.custom.ProgressIndicator
import com.datum.client.ui.custom.Separator
import com.datum.client.ui.page.archive_page.ArchivePage
import com.datum.client.ui.page.archive_page.ArchivePageNavHelper
import com.datum.client.ui.page.dataset_meta.DatasetMetaNavHelper
import com.datum.client.ui.page.dataset_meta.DatasetMetaPage
import com.datum.client.ui.page.image_classes.ImageClassNavHelper
import kotlinx.coroutines.*

class DatasetControlPage(n: NavController, b: NavBackStackEntry): Page(n, b) {

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {

        Column {
            Text(text = "Dataset control", fontSize = 24.sp)
            val scope = rememberCoroutineScope()
            ManagementOption(optionString = "Dataset meta") {
                scope.launch {

                    val data = ProgressIndicator.blockOperation {
                        withContext(Dispatchers.IO) {
                            BusinessLogicService.instance.getDatasetMeta()
                        }
                    }
                    navController.navigate(DatasetMetaNavHelper().substituteArgument(data))
                }

            }
            Separator(color = Color.Gray)
            /*ManagementOption(optionString = "Image classes") {
                navController.navigate(ImageClassNavHelper().substituteArgument(listOf<DatasetImageClassDto>()))
            }
            Separator(color = Color.Gray)
            ManagementOption(optionString = "View dataset") {

            }*/
            Separator(color = Color.Gray)
            ManagementOption(optionString = "Generate archive") {
                CoroutineScope(Dispatchers.IO).launch {
                    val path = ProgressIndicator.blockOperation {
                        BusinessLogicService.instance.generateArchive()
                    }
                    withContext(Dispatchers.Main) {
                        navController.navigate(ArchivePageNavHelper().substituteArgument(path))
                    }
                }
            }
        }
    }

}