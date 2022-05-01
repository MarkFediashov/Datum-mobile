package com.datum.client.ui.page.image_classes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.dto.DatasetImageClass
import com.datum.client.dto.DatasetImageClassDto
import com.datum.client.service.BusinessLogicService
import com.datum.client.types.*
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ProgressIndicator
import com.datum.client.ui.custom.Separator
import kotlinx.coroutines.launch

class ImageClassPage(n: NavController, b: NavBackStackEntry): Page(n, b) {

    private fun getExistedClassList(): List<DatasetImageClass>{
        return ImageClassNavHelper().getExistedClasses(backStackEntry)
    }

    private val existedClasses = mutableStateListOf(*getExistedClassList().toTypedArray())
    private val list = mutableStateListOf<DatasetImageClassDto>()

    companion object{
        @Composable
        fun <T: NamedEntity> BuildSeparatedList(list: MutableList<T>, itemComposableBuilder: @Composable (arg: T) -> Unit){
            LazyColumn {
                items(list) {
                    key(it.name) {
                        itemComposableBuilder(arg = it)
                        if (list.last() != it) {
                            Separator(color = Color.Gray)
                        }
                    }
                }
            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {
        val newList = remember { list }
        val oldList = remember { getExistedClassList().toMutableList() }
        val edit = remember { newList.isNullOrEmpty() }

        val state = remember { createAlertState() }

        AlertBinder(state = state) {
            AddClassAlert(state = it, newList)
        }

        Box {
            Scaffold(floatingActionButton = {
                if (edit)
                    AddButton(state)
            }) {
                Column(Modifier.fillMaxSize()) {
                    Text("Old classes")
                    Box(Modifier.weight(0.5f)){
                        BuildSeparatedList(list = oldList) { arg ->
                            BuildRow(imageClassDto = arg)
                        }
                    }
                    Text("New classes")
                    Box(Modifier.weight(0.5f)){
                        BuildSeparatedList(list = newList) { arg ->
                            BuildRow(imageClassDto = arg) {
                                newList.remove(arg)
                            }
                        }
                    }
                }
            }
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)) {
                SaveButton()
            }
        }
    }

    @Composable
    private fun BuildRow(imageClassDto: NamedEntity, onDelete: (() -> Unit)? = null){
        Row(modifier = Modifier.fillMaxWidth().height(50.dp), horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically){
            Text(imageClassDto.name, modifier = Modifier.padding(start = 10.dp), fontSize = 18.sp)
            onDelete?.let {
                IconButton(onClick = {
                    it()
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }

        }
    }

    @Composable
    private fun AddButton(state: AlertState) {
        FloatingActionButton(onClick = state::show) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add image dataset")
        }
    }

    @Composable
    private fun AddClassAlert(state: AlertState, list: SnapshotStateList<DatasetImageClassDto>){
        val text = remember {mutableStateOf("")}
        AlertDialog(
            onDismissRequest = { state.hide() },
            title = { Text("New image class") },
            text = {
                Column() {
                    TextField(value = text.value, onValueChange = {text.value = it})
                    Box(Modifier.height(15.dp))
                }
            },
            buttons = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(onClick = {
                        if(list.none { it.name == text.value }) {
                            list.add(DatasetImageClassDto(text.value, text.value))
                            state.hide()
                        }
                    }, enabled = text.value.isNotBlank()) {
                        Text("Add class")
                    }
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }

    @Composable
    private fun SaveButton() {
        val scope = rememberCoroutineScope()
        val list = remember { list }
        Button(onClick = {
            scope.launch {
                ProgressIndicator.blockOperation {
                    BusinessLogicService.instance.addClasses(list.toList())
                }
                navController.popBackStack()
            }
        }, enabled = list.isNotEmpty()){
            Text("Save classes")
        }
    }
}