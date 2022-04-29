package com.datum.client.ui.page.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.datum.client.DatasetNotInitializedException
import com.datum.client.MainActivity
import com.datum.client.service.BusinessLogicService
import com.datum.client.service.Role
import com.datum.client.types.*
import com.datum.client.ui.Page
import com.datum.client.ui.custom.ActionSubmitAlert
import com.datum.client.ui.custom.AlertConfiguration
import com.datum.client.ui.custom.ProgressIndicator
import com.datum.client.ui.page.maintainer.MaintainerPageNavHelper
import com.datum.client.ui.page.user.UserPageNavHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPage(n: NavController, b: NavBackStackEntry): Page(n, b) {

    var stringProxy: String? = null

    @ExperimentalMaterialApi
    @Composable
    override fun BuildContent() {
        val context = LocalContext.current
        val login = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        val error = createAlertState()

        AlertBinder(state = error) {
            ActionSubmitAlert(state = it, config = AlertConfiguration("ERROR", "Error : $stringProxy", onOk = it::hide, onCancel = it::hide))
        }

        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            Spacer(Modifier.weight(1f))
            TextField(value = login.value, onValueChange = { login.value = it })
            Box(Modifier.padding(top=20.dp))
            TextField(value = password.value, onValueChange = { password.value = it})
            Spacer(Modifier.weight(1f))
            Button(onClick = { onClick(login.value.trim(), password.value.trim(), context, scope, error) }) {
                Text("Login")
            }
            Box(Modifier.padding(top=30.dp))
        }
    }

    private fun onClick(login: String, password: String, context: Context, scope: CoroutineScope, alertState: AlertState) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = ProgressIndicator.blockOperation {
                    BusinessLogicService.instance.login(login, password)
                }
                withContext(Dispatchers.Main) {
                    val message = if (result) "Success" else "Bad"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (result) {
                        val role = BusinessLogicService.instance.getUserRole()
                        navController.popBackStack()
                        if (Role.isUser(role)) {
                            navController.navigate(UserPageNavHelper().substituteArgument())
                        } else {
                            navController.navigate(MaintainerPageNavHelper().substituteArgument())
                        }
                    }
                }
            } catch (e: DatasetNotInitializedException) {
                scope.launch {
                    MainActivity.state.show()
                }
            } catch (e: Exception){
                scope.launch {
                    stringProxy = e.message + e.stackTraceToString()
                    alertState.show()
                }
            }
        }
    }

}