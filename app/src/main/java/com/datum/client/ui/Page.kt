package com.datum.client.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.composable


abstract class Page(val navController: NavController, val backStackEntry: NavBackStackEntry) {

    @ExperimentalMaterialApi
    @Composable
    abstract fun BuildContent()

}
