package com.datum.client.ui.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IntegerSlider(pos: MutableState<Int>){
    val innerState = remember { mutableStateOf(100.0f) }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
        Text(pos.value.toString(), modifier = Modifier.weight(0.2f))
        Slider(
            value = innerState.value,
            onValueChange = {
                pos.value = it.toInt()
                innerState.value = it
            }, valueRange = 1.0f..100.0f, modifier = Modifier.weight(0.8f))
    }

}