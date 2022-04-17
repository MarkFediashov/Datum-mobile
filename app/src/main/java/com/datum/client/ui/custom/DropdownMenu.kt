package com.datum.client.ui.custom

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

interface DropdownMenuEntity{
    val id: Int
    val name: String
}

@ExperimentalMaterialApi
@Composable
fun <T: DropdownMenuEntity> ComplexDropdownMenu(selectedClass: MutableState<T>, options: List<T>){

    val expanded = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {
        expanded.value = it
    }) {
        TextField(value = selectedClass.value.name, readOnly = true, onValueChange = { })
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false}) {
            options.forEach {
                DropdownMenuItem(onClick = {
                    selectedClass.value = it
                    expanded.value = false
                }) {
                    Text(it.name)
                }
            }
        }
    }
}