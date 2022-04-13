package com.datum.client.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex

object ProgressIndicator{

    private val state = mutableStateOf(false)

    @Composable
    fun Build(){
        if(state.value) {
            Box(Modifier.fillMaxSize().zIndex(100f).background(Color(100,100,100, 123)).clickable(enabled = false){ }) {
                CircularProgressIndicator(color = Color.Magenta, modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    private fun show() {
        state.value = true
    }

    private fun hide() {
        state.value = false
    }

    suspend fun <T> blockOperation(operation: suspend () -> T): T{
        show()
        try {
            return operation()
        } finally {
            hide()
        }
    }
}

