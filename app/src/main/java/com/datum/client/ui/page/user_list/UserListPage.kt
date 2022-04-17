package com.datum.client.ui.page.user_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.dto.UserDto
import com.datum.client.service.Role
import com.datum.client.ui.Page
import com.datum.client.ui.custom.Separator
import java.util.logging.Level
import java.util.logging.Logger

class UserListPage(navController: NavController, backStackEntry: NavBackStackEntry): Page(navController, backStackEntry) {

    @Composable
    @ExperimentalMaterialApi
    override fun BuildContent() {
        val argument = UserListNavHelper().getList(backStackEntry)
        val scrollState = rememberLazyListState()
        val state = remember {  mutableStateOf(false) }
        val deleteUserRef = remember { mutableStateOf(argument.first()) }

        Scaffold(
            floatingActionButton = { AddUserFloating() }){
            LazyColumn(state = scrollState, modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)) {
                items(argument){
                    if(state.value) {
                        DeleteUserDialog(deleteUserRef.value, state)
                    }
                    UserRow(it){
                        state.value = true
                        deleteUserRef.value = it
                    }
                    if(argument.last() != it){
                        Separator(color = Color.Gray)
                    }
                }
            }
        }
    }

    @Composable
    private fun AddUserFloating(){
        FloatingActionButton(onClick = {
            Logger.getGlobal().log(Level.WARNING, "hello")
        }) {
            Icon(painter = rememberVectorPainter(Icons.Default.Add), "Add User")
        }
    }

    @Composable
    private fun UserRow(user: UserDto, onLongTap: () -> Unit){
        Box(Modifier.pointerInput(Unit){
            detectTapGestures(
                onLongPress = {
                    onLongTap()
                }
            )
        }) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Column {
                    Text(user.name, fontSize = 20.sp)
                    Text(user.createdStr)
                }
                Text(Role.nameOfRole(user.roleId), fontSize = 20.sp)
            }
        }
    }

    @Composable
    private fun DeleteUserDialog(user: UserDto, showAlert: MutableState<Boolean>){
        AlertDialog(onDismissRequest = { },
            title = { Text("Warning")},
            text = { Text ("Delete ${user.name}? This user cannot upload images!") },
            buttons = {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = {
                        showAlert.value = false
                    }) {
                        Text("OK")
                    }
                    Box(Modifier.width(20.dp))
                    Button(onClick = {
                        showAlert.value = false
                    }) {
                        Text("Cancel")
                    }
                    Box(Modifier.width(10.dp))
                }
            }, properties = DialogProperties())
    }

    @Composable
    private fun AddUserDialog(){

    }

}