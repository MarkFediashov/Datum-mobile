package com.datum.client.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ManagementOption(optionString: String, onSelect: () -> Unit){
    Box(Modifier.fillMaxWidth().height(60.dp).clickable {
        onSelect()
    }) {
        Text(optionString, Modifier.align(Alignment.CenterStart).padding(10.dp), fontSize = 22.sp)
    }
}

@Composable
fun Separator(color: Color){
    Box(Modifier.fillMaxWidth().height(1.dp).background(color))
}