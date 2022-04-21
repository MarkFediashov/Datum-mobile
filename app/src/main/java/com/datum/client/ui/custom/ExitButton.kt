package com.datum.client.ui.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.datum.client.service.BusinessLogicService
import com.datum.client.ui.page.domain_enter.DomainEnterNavHelper

private val exitState = mutableStateOf(false)

@Composable
fun ExitButton(navController: NavController){
    val state = remember { exitState }
    if(state.value){
        AlertDialog(
            onDismissRequest = { exitState.value = false },
            text = { Text("Do you want to logout? App storage will forget server and your credentials") },
            title = { Text("Warning") },
            buttons = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                    TextButton(onClick = {
                        state.value = false
                        navController.popBackStack()
                        navController.navigate(DomainEnterNavHelper().substituteArgument())

                        BusinessLogicService.instance.deletePersistentData()
                    }) {
                        Text("Ok")
                    }
                    TextButton(onClick = { state.value = false}) {
                        Text("Cancel")
                    }
                    Box(Modifier.width(20.dp))
                }
            },
            properties = DialogProperties()
        )
    }
    IconButton(onClick = {
        state.value = true
    }) {
        Icon(Icons.Default.ExitToApp, "Exit", tint = Color.LightGray)
    }
}