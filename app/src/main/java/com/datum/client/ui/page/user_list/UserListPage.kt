package com.datum.client.ui.page.user_list

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.ui.Page

class UserListPage(navController: NavController, backStackEntry: NavBackStackEntry): Page(navController, backStackEntry) {

    @Composable
    @ExperimentalMaterialApi
    override fun BuildContent() {
        val argument = UserListNavHelper().getList(backStackEntry)
    }

}