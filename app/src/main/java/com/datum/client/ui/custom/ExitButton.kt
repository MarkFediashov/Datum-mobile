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
import com.datum.client.types.AlertBinder
import com.datum.client.types.createAlertState
import com.datum.client.types.hide
import com.datum.client.types.show
import com.datum.client.ui.page.domain_enter.DomainEnterNavHelper

private val exitState = createAlertState()

@Composable
fun ExitButton(navController: NavController){
    val state = remember { exitState }
    AlertBinder(state = state) {
        ActionSubmitAlert(state = it, config = AlertConfiguration(
            text = "Do you want to logout? App storage will forget server and your credentials",
            title = "Warning",
            onOk = {
                //BusinessLogicService.instance.deletePersistentData()
                navController.popBackStack()
                navController.navigate(DomainEnterNavHelper().substituteArgument())
                it.hide()
            },
            onCancel = {
                it.hide()
            }
        ))
    }
    IconButton(onClick = {
        state.show()
    }) {
        Icon(Icons.Default.ExitToApp, "Exit", tint = Color.LightGray)
    }
}