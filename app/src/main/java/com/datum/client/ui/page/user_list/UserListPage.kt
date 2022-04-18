package com.datum.client.ui.page.user_list

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.dto.UserCreationDto
import com.datum.client.dto.UserDto
import com.datum.client.service.BusinessLogicService
import com.datum.client.service.Role
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ComplexDropdownMenu
import com.datum.client.ui.custom.ProgressIndicator
import com.datum.client.ui.custom.Separator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class UserListPage(navController: NavController, backStackEntry: NavBackStackEntry): Page(navController, backStackEntry) {

    @Composable
    @ExperimentalMaterialApi
    override fun BuildContent() {

        val argument = UserListNavHelper().getList(backStackEntry)
        val scrollState = rememberLazyListState()
        val showDeleteDialogState = remember { mutableStateOf(false) }
        val showAddUserDialogState = remember { mutableStateOf(false) }
        val deleteUserRef = remember { mutableStateOf(argument.first()) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Scaffold(
            floatingActionButton = {
                AddUserFloating(showAddUserDialogState)
            }){
            LazyColumn(state = scrollState, modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)) {
                items(argument){
                    if(showDeleteDialogState.value) {
                        DeleteUserDialog(deleteUserRef.value, showDeleteDialogState)
                    }
                    if(showAddUserDialogState.value){
                        AddUserDialog(showAddUserDialogState) {
                            scope.launch {
                                onUserCreate(it, context)
                            }
                        }
                    }

                    UserRow(it) {
                        showDeleteDialogState.value = true
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
    private fun AddUserFloating(show: MutableState<Boolean>){
        FloatingActionButton(onClick = {
            show.value = true
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
                    Text(user.created)
                }
                Text(Role.by(user.roleId).name, fontSize = 20.sp)
            }
        }
    }

    @Composable
    private fun AlertDialogButtons(onOk: () -> Unit, onCancel: () -> Unit){
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onOk) {
                Text("OK")
            }
            Box(Modifier.width(20.dp))
            Button(onClick = onCancel) {
                Text("Cancel")
            }
            Box(Modifier.width(10.dp))
        }
    }

    @Composable
    private fun DeleteUserDialog(user: UserDto, showAlert: MutableState<Boolean>){
        val scope = rememberCoroutineScope()
        AlertDialog(onDismissRequest = { },
            title = { Text("Warning")},
            text = { Text ("Delete ${user.name}? This user will cannot upload images!") },
            buttons = {
                AlertDialogButtons(
                    onOk = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                ProgressIndicator.blockOperation {
                                    BusinessLogicService.instance.deleteUser(user.id)
                                }
                                withContext(Dispatchers.Main){
                                    showAlert.value = false
                                }
                            }
                        }

                    },
                    onCancel = { showAlert.value = false }
                )
            }, properties = DialogProperties())
    }

    @ExperimentalMaterialApi
    @Composable
    private fun AddUserDialog(showAlert: MutableState<Boolean>, onUserAdd: (UserCreationDto) -> Unit){
        val newUserName = remember { mutableStateOf ("")}
        val newUserRole = remember { mutableStateOf ("user")}
        val newUserEmail = remember { mutableStateOf("") }

        val assign = { value: MutableState<String> -> { it: String -> value.value = it} }

        val currentRole = remember { mutableStateOf(Role.ROLES.last())}

        AlertDialog(onDismissRequest = { showAlert.value = false },
            title = { Text ("Add user") },
            text = {
                Column() {
                    TextField(value = newUserName.value, onValueChange = assign(newUserName))
                    Box(Modifier.height(20.dp))
                    TextField(value = newUserEmail.value, onValueChange = assign(newUserEmail))
                    Box(Modifier.height(20.dp))
                    ComplexDropdownMenu(selectedClass = currentRole, options = Role.ROLES)
                }
            },
            buttons = {
                AlertDialogButtons(onOk = {
                    val userCreationDto = UserCreationDto(newUserName.value, newUserEmail.value, currentRole.value.id)
                    showAlert.value = false
                    onUserAdd(userCreationDto)
                }) {
                    showAlert.value = false
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }

    private suspend fun onUserCreate(userCreationDto: UserCreationDto, context: Context){
        withContext(Dispatchers.IO) {
            val result = when (ProgressIndicator.blockOperation {  BusinessLogicService.instance.addUser(userCreationDto) }!= null) {
                true -> "User created"
                false -> "Bad"
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
            }
        }
    }

}