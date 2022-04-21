package com.datum.client.ui.page.dataset_meta

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.dto.DatasetDto
import com.datum.client.dto.DatasetImageClass
import com.datum.client.dto.DatasetImageClassDto
import com.datum.client.service.BusinessLogicService
import com.datum.client.types.*
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ActionSubmitAlert
import com.datum.client.ui.custom.AlertConfiguration
import com.datum.client.ui.custom.ProgressIndicator
import com.datum.client.ui.page.image_classes.ImageClassNavHelper
import kotlinx.coroutines.*

class DatasetMetaPage(n: NavController, b: NavBackStackEntry): Page(n, b) {

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {
        val data = DatasetMetaNavHelper().getDatasetMeta(backStackEntry)

        BuildColumnView(data)
    }

    @Composable
    inline fun <reified T> createState(arg:T): MutableState<T> {
        return remember {
            mutableStateOf(arg)
        }
    }

    @Composable
    private fun BuildColumnView(dataset: DatasetDto<DatasetImageClass>){

        val scope = rememberCoroutineScope()
        val deleteMeta = createAlertState()

        val dtoState = object {
            val train = createState(dataset.trainPercentile?.toString())
            val test = createState(dataset.testPercentile?.toString())
            val validation = createState(dataset.validationPercentile?.toString())
            val name = createState(dataset.name)
            val description = createState(dataset.description)
            val listOfImages = remember { mutableStateListOf<DatasetImageClassDto>()}
        }

        val context = LocalContext.current

        AlertBinder(state = deleteMeta) {
            DeleteMetaAlert(it, scope = scope, context)
        }

        val enabled = dtoState.listOfImages.isNotEmpty()

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)) {
            BuildField("Name",  dtoState.name, keyboardType = KeyboardType.Text)
            BuildField("Description", dtoState.description, required = false, keyboardType = KeyboardType.Text)
            BuildField("Train",  dtoState.train, keyboardType = KeyboardType.Number)
            BuildField("Test", dtoState.test, keyboardType = KeyboardType.Number)
            BuildField("Validation",  dtoState.validation, keyboardType = KeyboardType.Number)
            Spacer(modifier = Modifier.weight(0.3f))
            if(!enabled){
                Column(modifier = Modifier.weight(1.0f).padding(horizontal = 10.dp)) {
                    Row(modifier = Modifier
                        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Info")
                        Box(Modifier.width(20.dp))
                        Text("You should initialize class list to continue")
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), horizontalArrangement = Arrangement.Start){
                        TextButton(onClick = {
                            val classList = dtoState.listOfImages.toList()
                            navController.navigate(ImageClassNavHelper().substituteArgument(classList))
                        }) {
                            Text("Add classification")
                        }
                    }
                }

            }
            BuildButtons(scope = scope, deleteMeta, dataset.isFilled(), enabled = enabled)
            Spacer(modifier = Modifier.weight(0.3f))
        }
    }

    @Composable
    private fun BuildField(name: String, arg: MutableState<String?>, required: Boolean = true, keyboardType: KeyboardType){
        val isError = {
            if(keyboardType == KeyboardType.Number){
                val double = arg.value?.toDoubleOrNull()
                required && (double == null ||  double <= 0.0)
            } else {
                required && arg.value.isNullOrEmpty()
            }
        }
        Column(Modifier.padding(vertical = 5.dp, horizontal = 5.dp)){
            Text(name, modifier = Modifier.padding(start = 10.dp))
            TextField(value = (arg.value ?: "").toString(),
                onValueChange = {t -> arg.value = t},
                modifier = Modifier.fillMaxWidth(),
                isError = isError(),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType))
        }
    }

    @Composable
    private fun BuildButtons(scope: CoroutineScope, delete: AlertState, isFilled: Boolean, enabled: Boolean){
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                scope.launch {
                    withContext(Dispatchers.IO){

                    }
                }
            }, enabled = enabled) {
                Text("Submit")
            }
            if(isFilled) {
                TextButton(onClick = { delete.show() }) {
                    Text("Delete metadata", color = Color.Red)
                }
            }
        }

    }

    @Composable
    private fun DeleteMetaAlert(state: AlertState, scope: CoroutineScope, context: Context){
        ActionSubmitAlert(state = state, config = AlertConfiguration(
            text = "Deleting metadata is mean that database became inconsistent. Do you really want to delete metadata (image classes also be deleted!)",
            title = "WARNING",
            onOk = {
                scope.launch {
                    state.hide()
                    val x = async {
                        ProgressIndicator.blockOperation {
                            BusinessLogicService.instance.deleteMetadata()
                        }
                    }
                    x.await()
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
            },
            onCancel = {
                state.value = false
            }
        ))
    }
}