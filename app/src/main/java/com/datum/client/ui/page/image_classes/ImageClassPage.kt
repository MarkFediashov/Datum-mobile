package com.datum.client.ui.page.image_classes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.dto.DatasetImageClassDto
import com.datum.client.repository.ArgumentRepository
import com.datum.client.types.*
import com.datum.client.ui.Page
import com.datum.client.ui.custom.Separator
import com.datum.client.ui.page.dataset_meta.DatasetMetaNavHelper
import kotlin.coroutines.CoroutineContext

class ImageClassPage(n: NavController, b: NavBackStackEntry): Page(n, b) {

    private fun getImageClassList(): List<DatasetImageClassDto>{
        return ImageClassNavHelper().getList(backStackEntry) ?: mutableListOf()
    }

    private val list = mutableStateListOf(*getImageClassList().toTypedArray())

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {
        val list = remember { list }
        val edit = remember { list.isNullOrEmpty() }

        val state = remember { createAlertState() }

        AlertBinder(state = state) {
            AddClassAlert(state = it, list)
        }

        Box {
            Scaffold(floatingActionButton = {
                if (edit)
                    AddButton(state)
            }) {
                LazyColumn {
                    items(list) {
                        key(it.name) {
                            BuildRow(imageClassDto = it) {
                                list.remove(it)
                            }
                            if (list.last() != it) {
                                Separator(color = Color.Gray)
                            }
                        }
                    }
                }
            }
            Box(Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)) {
                SaveButton()
            }
        }
    }

    @Composable
    private fun BuildRow(imageClassDto: DatasetImageClassDto, onDelete: () -> Unit){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically){
            Text(imageClassDto.name, modifier = Modifier.padding(start = 10.dp), fontSize = 18.sp)
            IconButton(onClick = {
                onDelete()
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
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
        val list = remember { list }
        Button(onClick = {
            navController.previousBackStackEntry?.apply {
                val refId = ArgumentRepository.putArgument(list.toList())
                arguments!!.putInt(DatasetMetaNavHelper.CLASS_LIST_ARG, refId)
                navController.popBackStack()
            }
        }, enabled = list.isNotEmpty()){
            Text("Save classes")
        }
    }
}