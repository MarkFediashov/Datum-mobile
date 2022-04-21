package com.datum.client.ui.page.dataset_meta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.dto.DatasetDto
import com.datum.client.dto.DatasetImageClass
import com.datum.client.types.AlertBinder
import com.datum.client.types.AlertState
import com.datum.client.types.createAlertState
import com.datum.client.types.show
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ActionSubmitAlert
import com.datum.client.ui.custom.AlertConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        }

        AlertBinder(state = deleteMeta) {
            DeleteMetaAlert(it, scope = scope)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)) {
            BuildField("Name",  dtoState.name, keyboardType = KeyboardType.Text)
            BuildField("Description", dtoState.description, required = false, keyboardType = KeyboardType.Text)
            BuildField("Train",  dtoState.train, keyboardType = KeyboardType.Number)
            BuildField("Test", dtoState.test, keyboardType = KeyboardType.Number)
            BuildField("Validation",  dtoState.validation, keyboardType = KeyboardType.Number)
            Spacer(modifier = Modifier.weight(1f))
            BuildButtons(scope = scope, deleteMeta)
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
    private fun BuildButtons(scope: CoroutineScope, delete: AlertState){
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                scope.launch {
                    withContext(Dispatchers.IO){

                    }
                }
            }) {
                Text("Submit")
            }
            TextButton(onClick = { delete.show() }) {
                Text("Delete metadata", color = Color.Red)
            }
        }

    }

    @Composable
    private fun DeleteMetaAlert(state: AlertState, scope: CoroutineScope){
        ActionSubmitAlert(state = state, config = AlertConfiguration(
            text = "Deleting metadata is mean that database became inconsistent. Do you really want to delete metadata (image classes also be deleted!)",
            title = "WARNING",
            onOk = {
                scope.launch {

                }
            },
            onCancel = {
                state.value = false
            }
        ))
    }
}