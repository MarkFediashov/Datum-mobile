package com.datum.client.ui.page.archive_page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.ui.Page

class ArchivePage(n: NavController, b: NavBackStackEntry): Page(n, b) {

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {
        val text = ArchivePageNavHelper().getPath(backStackEntry)
        Box(Modifier.fillMaxSize()){
            SelectionContainer(Modifier.align(Alignment.Center)) {
                Text(text.path)
            }

        }
    }

}