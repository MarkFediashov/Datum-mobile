package com.datum.client.ui.page.dataset_meta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.dto.DatasetDto
import com.datum.client.ui.Page

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
    private fun BuildColumnView(dataset: DatasetDto){
        val immutable = false

        val dtoState = object {
            val train = createState(dataset.trainPercent?.toString())
            val test = createState(dataset.testPercent?.toString())
            val validation = createState(dataset.validationPercent?.toString())
            val name = createState(dataset.name)
            val description = createState(dataset.description)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)) {
            BuildField("Name", immutable, dtoState.name, keyboardType = KeyboardType.Text)
            BuildField("Description", immutable, dtoState.description, required = false, keyboardType = KeyboardType.Text)
            BuildField("Train", immutable, dtoState.train, keyboardType = KeyboardType.Number)
            BuildField("Test", immutable, dtoState.test, keyboardType = KeyboardType.Number)
            BuildField("Validation", immutable, dtoState.validation, keyboardType = KeyboardType.Number)
            Spacer(modifier = Modifier.weight(1f))
            if(!immutable) {
                BuildButton()
            }
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }

    @Composable
    private fun BuildField(name: String, immutable: Boolean, arg: MutableState<String?>, required: Boolean = true, keyboardType: KeyboardType){
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
                readOnly = immutable, isError = isError(),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType))
        }
    }

    @Composable
    private fun BuildButton(){

        Button(onClick = { }){
            Text("Submit")
        }

    }
}