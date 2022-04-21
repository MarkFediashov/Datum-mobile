package com.datum.client.ui.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.datum.client.types.AlertState
import kotlinx.coroutines.CoroutineScope

class AlertConfiguration(val title: String,
                   val text: String,
                   val okText: String = "Ok",
                   val cancelText: String = "Cancel",
                   val onOk: () -> Unit,
                   val onCancel: () -> Unit)

@Composable
private fun AlertDialogButtons(config: AlertConfiguration){
    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
        Button(onClick = config.onOk) {
            Text(config.okText)
        }
        Box(Modifier.width(20.dp))
        Button(onClick = config.onCancel) {
            Text(config.cancelText)
        }
        Box(Modifier.width(10.dp))
    }
}

@Composable
fun ActionSubmitAlert(state: AlertState, config: AlertConfiguration){
    AlertDialog(
        onDismissRequest = { state.value = false },
        title = { Text(config.title) },
        text = { Text(config.text) },
        buttons = { AlertDialogButtons(config = config) },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}