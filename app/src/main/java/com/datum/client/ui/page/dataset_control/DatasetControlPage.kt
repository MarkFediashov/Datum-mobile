package com.datum.client.ui.page.dataset_control

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.service.BusinessLogicService
import com.datum.client.types.*
import com.datum.client.ui.Page
import com.datum.client.ui.custom.*
import com.datum.client.ui.page.archive_page.ArchivePageNavHelper
import com.datum.client.ui.page.image_classes.ImageClassNavHelper
import kotlinx.coroutines.*

class DatasetControlPage(n: NavController, b: NavBackStackEntry): Page(n, b) {

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {
        val deleteState = createAlertState()
        val archiveState = createAlertState()
        val scope = rememberCoroutineScope()

        AlertBinder(state = deleteState) {
            DeleteAlert(state = it, scope = scope)
        }

        AlertBinder(state = archiveState) {
            ArchiveAlert(state = it, scope = scope)
        }

        Column {
            Text(text = "Dataset control", fontSize = 24.sp)
            ManagementOption(optionString = "Dataset image classes") {
                scope.launch {
                    val existedClasses = ProgressIndicator.blockOperation {
                        BusinessLogicService.instance.getActualImageClasses()
                    }
                    navController.navigate(ImageClassNavHelper().substituteArgument(existedClasses))
                }

            }
            Separator(color = Color.Gray)
            ManagementOption(optionString = "Delete dataset") {
                deleteState.show()
            }
            Separator(color = Color.Gray)
            ManagementOption(optionString = "Generate archive") {
                archiveState.show()
            }
        }
    }

    @Composable
    private fun DeleteAlert(state: AlertState, scope: CoroutineScope){
        ActionSubmitAlert(state = state, config = AlertConfiguration(
            title = "WARNING",
            text = "This action will delete all images and metadata about dataset. All information will be lost",
            onOk = {
                scope.launch {
                    state.hide()
                    withContext(Dispatchers.IO) {
                        ProgressIndicator.blockOperation {
                            BusinessLogicService.instance.deleteMetadata()
                        }
                    }
                    withContext(Dispatchers.Main){
                        navController.popBackStack()
                    }

                }
            },
            onCancel = {

            }
        ))
    }

    @Composable
    private fun ArchiveAlert(state: AlertState, scope: CoroutineScope){
        val percent = remember { mutableStateOf(100) }
        AlertDialog(onDismissRequest = state::hide,
            title = { Text("Archive percent") },
            text = {
                IntegerSlider(percent)
            },
            buttons = {
                TextButton(onClick = {
                    scope.launch {
                        state.hide()
                        val path = withContext(Dispatchers.IO) {
                            ProgressIndicator.blockOperation {
                                BusinessLogicService.instance.generateArchive(percent.value)
                            }
                        }

                        navController.navigate(ArchivePageNavHelper().substituteArgument(path))

                    }
                }) {
                    Text("Generate")
                }
            }
        )
    }

}