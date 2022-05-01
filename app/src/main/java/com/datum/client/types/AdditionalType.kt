package com.datum.client.types

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

typealias AlertState = MutableState<Boolean>

fun createAlertState(): AlertState = mutableStateOf(false)

fun AlertState.hide(){
    this.value = false
}

fun AlertState.isActive() = value

fun AlertState.show(){
    this.value = true
}

@Composable
fun AlertBinder(state: AlertState, AlertBuilder: @Composable (state: AlertState) -> Unit){
    if(state.isActive()){
        AlertBuilder(state)
    }
}

interface NamedEntity{
    val name: String
}